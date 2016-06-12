package client;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Timer;

import javax.naming.spi.DirStateFactory.Result;
import javax.swing.*;

import util.AnswerWindow;
import util.ChatMessage;
import util.Make_GamePanel;
import util.PosImageIcon;

import java.awt.*;
import java.awt.event.*;

public class ChatClient {
	//************************일단계 틀린위치와 틀린그림의 크기**************************
	private final int[][] Level1_Point = {{744,1004,1110,1065,615,697,905},
											  {480,430,397,600,634,308,592}};	

	private final int[][] Level1_Point_Size ={{40,40,40,70,70,60,50},
				                                  {40,40,50,50,50,70,50}};
	private Make_GamePanel Level1;
	//************************이단계 틀린위치와 틀린그림의 크기**************************
	private final int[][] Level2_Point = {{938,1044,995,968,957,1094,1117},
				                              {205,503,358,499,713,540,690}};	

	private final int[][] Level2_Point_Size ={{40,50,40,40,40,60,50},
				                                  {50,50,40,40,40,40,50}};
	private Make_GamePanel Level2;

	//************************삼단계 틀린위치와 틀린그림의 크기**************************
	private final int[][] Level3_Point = {{785,1100,770,775,1124,998,919},
				                              {224,247,720,542,137,523,642}};	

	private final int[][] Level3_Point_Size ={{50,40,70,40,30,40,40},
				                                  {50,150,40,40,40,40,50}};
	private Make_GamePanel Level3;
	//************************사단계 틀린위치와 틀린그림의 크기**************************
	private final int[][] Level4_Point = {{695,914,650,1005,810,1086,995},
				                              {270,272,682,420,243,222,693}};	

	private final int[][] Level4_Point_Size ={{50,40,50,40,50,50,100},
				                                  {50,50,40,50,50,80,60}};
	private Make_GamePanel Level4;
	
	
	
	ArrayList<PosImageIcon> imgList = new ArrayList<PosImageIcon>();
	int count = -1;
	
	JFrame frame;
	String frameTitle = "채팅 클라이언트";
	JTextArea incoming;			// 수신된 메시지를 출력하는 곳
	JTextArea outgoing;			// 송신할 메시지를 작성하는 곳
	JList counterParts;			// 현재 로그인한 채팅 상대목록을 나타내는 리스트.
	ObjectInputStream reader;	// 수신용 스트림
	ObjectOutputStream writer;	// 송신용 스트림
	Socket sock;				// 서버 연결용 소켓
	String user;				// 이 클라이언트로 로그인 한 유저의 이름
	JButton loginButton;			// 토글이 되는 로그인/로그아웃 버튼
	JPanel loginPanel,delayPanel;
	Timer timer;
	PosImageIcon LoginPanelImage = new PosImageIcon("로그인화면.jpg", 0, 0,1200 , 850);
	JTextField nameText = new JTextField();
	int Level = 1;
	SelectOpponent so;
	GameStartButton_Panel gameStartPanel;

	public static void main(String[] args) {
		ChatClient client = new ChatClient();
		client.setUpGUI();
		
	}

	private void setUpGUI() {
		setUpNetworking();
		
		imgList.add(new PosImageIcon("1초후게임시작.jpg", 0,0,1200,850));
		imgList.add(new PosImageIcon("2초후게임시작.jpg", 0,0,1200,850));
		imgList.add(new PosImageIcon("3초후게임시작.jpg", 0,0,1200,850));	
		delayPanel = new JPanel(){
			@Override
			protected void paintComponent(Graphics g) {
				if(count == 0) imgList.get(2).draw(g);
				else if(count == 1) imgList.get(1).draw(g);
				else if(count == 2)	imgList.get(0).draw(g);
			}
		};
		// build GUI
		frame = new JFrame(frameTitle + " : 로그인하세요");


		frame.setLayout(null);
		loginPanel = new JPanel(){
			protected void paintComponent(Graphics arg0) {
				LoginPanelImage.draw(arg0);
			}
		};
		loginPanel.setLayout(null);
		
		nameText.setBounds(550, 740, 200, 50);
		loginPanel.add(nameText);

		loginButton = new JButton();
		loginButton.setOpaque(false);
		loginButton.setBorderPainted(false);
		loginButton.setContentAreaFilled(false);
		loginButton.setBounds(1000,700,200,100);
		loginButton.addActionListener(new LogButtonListener());
		loginPanel.add(loginButton);
	
		//frame.setUndecorated(true);
		frame.setBounds(100, 100, 1200, 850);
		loginPanel.setBounds(0, 0, 1200, 850);
		frame.add(loginPanel);	   
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();

	}
	private void setUpNetworking() {  
		try {
			// ck = new cket("220.69.203.11", 5000);		// 오동익의 컴퓨터
			sock = new Socket("127.0.0.1", 5000);			// 소켓 통신을 위한 포트는 5000번 사용키로 함
			reader = new ObjectInputStream(sock.getInputStream());
			writer = new ObjectOutputStream(sock.getOutputStream());
		} catch(Exception ex) {
			JOptionPane.showMessageDialog(null, "서버접속에 실패하였습니다. 접속을 종료합니다.");
			ex.printStackTrace();
			frame.dispose();		// 네트워크가 초기 연결 안되면 클라이언트 강제 종료
		}
	} // close setUpNetworking   

	// 로그인과 아웃을 담당하는 버튼의 감청자. 처음에는 Login 이었다가 일단 로그인 되고나면 Logout을 처리
	private class LogButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			processLogin();
		}
		// 로그인 처리
		private void processLogin() {
			try {
				writer.writeObject(new ChatMessage(ChatMessage.MsgType.LOGIN, nameText.getText(), "", ""));
				writer.flush();
				frame.setTitle(frameTitle + " ( 로그인 : " +nameText.getText() + ")" );
				user = nameText.getText();
		
			} catch(Exception ex) {
				JOptionPane.showMessageDialog(null, "로그인 중 서버접속에 문제가 발생하였습니다.");
				ex.printStackTrace();
			}
		}
		// 로그아웃 처리
		private void processLogout() {
			int choice = JOptionPane.showConfirmDialog(null, "Logout합니다");
			if (choice == JOptionPane.YES_OPTION) {
				try {
					writer.writeObject(new ChatMessage(ChatMessage.MsgType.LOGOUT, user, "", ""));
					writer.flush();
					// 연결된 모든 스트림과 소켓을 닫고 프로그램을 종료 함
					writer.close(); reader.close(); sock.close();
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(null, "로그아웃 중 서버접속에 문제가 발생하였습니다. 강제종료합니다");
					ex.printStackTrace();
				} finally {
					System.exit(100);			// 클라이언트 완전 종료 
				}
			}
		}
	}  // close LoginButtonListener inner class

	public class SendButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			String to = (String) counterParts.getSelectedValue();
			if (to == null) {
				JOptionPane.showMessageDialog(null, "송신할 대상을 선택한 후 메시지를 보내세요");
				return;
			}
			try {
				incoming.append(user + " : " + outgoing.getText() + "\n"); // 나의 메시지 창에 보이기
				writer.writeObject(new ChatMessage(ChatMessage.MsgType.CLIENT_MSG, user, to, outgoing.getText()));
				writer.flush();
				outgoing.setText("");
				outgoing.requestFocus();
			} catch(Exception ex) {
				JOptionPane.showMessageDialog(null, "메시지 전송중 문제가 발생하였습니다.");
				ex.printStackTrace();
			}
		}
	}  // close SendButtonListener inner class

	// 서버에서 보내는 메시지를 받는 스레드 작업을 정의하는 클래스
	public class IncomingReader implements Runnable {
		public void run() {
			ChatMessage message;             
			ChatMessage.MsgType type;
			try {
				while (true) {
					message = (ChatMessage) reader.readObject();     	 // 서버로기 부터의 메시지 대기                   
					type = message.getType();
					if (type == ChatMessage.MsgType.LOGIN_FAILURE) {	 // 로그인이 실패한 경우라면
						JOptionPane.showMessageDialog(null, "Login이 실패하였습니다. 다시 로그인하세요");
						frame.setTitle(frameTitle + " : 로그인 하세요");
						loginButton.setText("Login");
					} else if (type == ChatMessage.MsgType.SERVER_MSG) { // 메시지를 받았다면 보여줌
						System.out.println(message.getContents());
				    	if (message.getSender().equals(user)) continue;  // 내가 보낸 편지면 보일 필요 없음
						AnswerWindow anw = new AnswerWindow(message,writer);
					} 
					else if (type == ChatMessage.MsgType.LOGIN_LIST) {
						// 유저 리스트를 추출 해서 counterParts 리스트에 넣어 줌.
						// 나는  빼고 (""로 만들어 정렬 후 리스트 맨 앞에 오게 함)
						String[] users = message.getContents().split("/");
						for (int i=0; i<users.length; i++) {
							if (user.equals(users[i])) users[i] = "";
						}
						users = sortUsers(users);		// 유저 목록을 쉽게 볼 수 있도록 정렬해서 제공
						users[0] =  ChatMessage.ALL;	// 리스트 맨 앞에 "전체"가 들어가도록 함
						gameStartPanel.so.setCounterParts( users );
					}
					else if (type == ChatMessage.MsgType.NO_ACT){
						// 아무 액션이 필요없는 메시지. 그냥 스킵		 
					}
					else if(type == ChatMessage.MsgType.PASSLOGIN){
						System.out.println("로그인을 하고 다음 화면으로 넘어감");            		
						so = new SelectOpponent(user,frame,writer);
						gameStartPanel = new GameStartButton_Panel(frame,so);
						frame.getContentPane().removeAll(); // 등록된 모든 컨테이너 삭제
						frame.getContentPane().add(gameStartPanel.panel); // 다시 등록
						frame.setContentPane(frame.getContentPane()); // 프레임에 설정 (this : Frame )
					}
					else if(type == ChatMessage.MsgType.SELECTEDPLAYER){
						AnswerWindow selectFrame = new AnswerWindow(message.getSender(),message.getReceiver(),writer);
					}
					else if(type == ChatMessage.MsgType.REJECTED){
						AnswerWindow selectFrame = new AnswerWindow(1);
					}
					else if(type == ChatMessage.MsgType.START){
						Level = 1;
						count = -1;
						Level1 = new Make_GamePanel("일단계 게임화면.jpg",70,"3단계 배경음악.wav",Level1_Point,Level1_Point_Size
								,message.getReceiver(),message.getSender(),writer,0,1);
						startGame();
					}
					else if(type == ChatMessage.MsgType.GETPOINT){
						if(Level == 1) Level1.displayPoint(message.getIndex());
						else if(Level == 2) Level2.displayPoint(message.getIndex()); 
						else if(Level == 3) Level3.displayPoint(message.getIndex()); 
						else Level4.displayPoint(message.getIndex()); 
					}
					else if(type == ChatMessage.MsgType.NEXT){
						
						changePanel(message.getSender(),message.getReceiver(),message.getIndex());
					}
					else if(type == ChatMessage.MsgType.ACCEPTOPSCORE){
						Level4.sendNextState();
						showResult(message);
					}
					else if(type == ChatMessage.MsgType.UPDATELIST){
						String[] users = message.getContents().split("/");
						for (int i=0; i<users.length; i++) {
							if (user.equals(users[i])) users[i] = "";
						}
						users = sortUsers(users);		// 유저 목록을 쉽게 볼 수 있도록 정렬해서 제공
						users[0] =  ChatMessage.ALL;	// 리스트 맨 앞에 "전체"가 들어가도록 함
						so.setCounterParts(users);
					}
					else if(type == ChatMessage.MsgType.HINDRANCE){
						if(Level==1) Level1.hindrance();
						else if(Level==2) Level2.hindrance();
						else if(Level==3) Level3.hindrance();
						else Level4.hindrance();
					}
					else {
						System.out.println(type);
						throw new Exception("서버에서 알 수 없는 메시지 도착했음");
					}
				} // close while
			} catch(Exception ex) {
				ex.printStackTrace();
				System.out.println("클라이언트 스레드 종료");		// 프레임이 종료될 경우 이를 통해 스레드 종료
			}
		} // close rungasgege
		private void showResult(ChatMessage message){
			ResultPanel result;
			System.out.println( message.getScore()+" ? "+ Level4.getScore());
			so = new SelectOpponent(user, frame, writer);
			if(message.getScore() < Level4.getScore()){
				System.out.println("이김");
				result = new ResultPanel("win", frame, message.getReceiver(), writer,so,message.getScore(),Level4.getScore());
			}
			else{
				result = new ResultPanel("lose", frame, message.getReceiver(), writer,so,message.getScore(),Level4.getScore());
			}
			result.setBounds(0, 0, 1200, 850);
			result.setLayout(null);
			
			frame.getContentPane().removeAll(); // 등록된 모든 컨테이너 삭제
			frame.getContentPane().add(result); // 다시 등록
			frame.setContentPane(frame.getContentPane()); // 프레임에 설정 (this : Frame )
		}
		private void changePanel(String sender,String receiver,int index){
			
			if(Level == 1){
				Level2 = new Make_GamePanel("이단계 게임화면.jpg",70,"2단계 배경음악.wav",Level2_Point,Level2_Point_Size,receiver,sender,writer,Level1.getScore(),2);
				Level2.setUp();
				Level2.progressBarTimer.start();
				Level2.Level_BGM.startPlay();
				Level1.Level_BGM.stopPlayer();
				Level1.progressBarTimer.stop();
				frame.getContentPane().removeAll(); // 등록된 모든 컨테이너 삭제
				frame.getContentPane().add(Level2); // 다시 등록
				frame.setContentPane(frame.getContentPane()); // 프레임에 설정 (this : Frame )
				Level++;
			}
			else if(Level == 2){
				Level3 = new Make_GamePanel("삼단계 게임화면.jpg",70,"1단계 배경음악.wav",Level3_Point,Level3_Point_Size,receiver,sender,writer,Level2.getScore(),3);	
				Level3.setUp();
				Level3.progressBarTimer.start();
				Level3.Level_BGM.startPlay();
				Level2.Level_BGM.stopPlayer();
				Level2.progressBarTimer.stop();
				frame.getContentPane().removeAll(); // 등록된 모든 컨테이너 삭제
				frame.getContentPane().add(Level3); // 다시 등록
				frame.setContentPane(frame.getContentPane()); // 프레임에 설정 (this : Frame )
				Level++;
			}
			else if(Level == 3){
				Level4 = new Make_GamePanel("사단계 게임화면.jpg",70,"4단계 배경음악.wav",Level4_Point,Level4_Point_Size,receiver,sender,writer,Level3.getScore(),4);
				Level4.setUp();
				Level4.progressBarTimer.start();
				Level4.Level_BGM.startPlay();
				Level3.Level_BGM.stopPlayer();
				Level3.progressBarTimer.stop();
				frame.getContentPane().removeAll(); // 등록된 모든 컨테이너 삭제
				frame.getContentPane().add(Level4); // 다시 등록
				frame.setContentPane(frame.getContentPane()); // 프레임에 설정 (this : Frame )
				Level++;
			}
		}
		private void startGame(){
			timer = new Timer();
			timer.schedule(new Task(), 0,1500);
		}
		private class Task extends TimerTask{
			
			public Task() {
				delayPanel.setBounds(0, 0, 1200, 850);
				frame.getContentPane().removeAll(); // 등록된 모든 컨테이너 삭제
				frame.getContentPane().add(delayPanel); // 다시 등록
				frame.setContentPane(frame.getContentPane()); // 프레임에 설정 (this : Frame )
			}
			@Override
			public void run() {
				if(count < 3){
					frame.invalidate();
					frame.repaint();
					delayPanel.repaint();
					count ++;
				}
				else if (count == 3){
					Level1.setUp();
					Level1.progressBarTimer.start();
					Level1.Level_BGM.startPlay();
					Level1.setBounds(0, 0, 1200, 850);
					frame.getContentPane().removeAll(); // 등록된 모든 컨테이너 삭제
					frame.getContentPane().add(Level1); // 다시 등록
					frame.setContentPane(frame.getContentPane()); // 프레임에 설정 (this : Frame )
					count ++;
					timer = null;;
				}
			}
		}

		// 주어진 String 배열을 정렬한 새로운 배열 리턴
		private String [] sortUsers(String [] users) {
			String [] outList = new String[users.length];
			ArrayList<String> list = new ArrayList<String>();
			for (String s : users) {
				list.add(s);
			}
			Collections.sort(list);				// Collections.sort를 사용해 한방에 정렬
			for (int i=0; i<users.length; i++) {
				outList[i] = list.get(i);
			}
			return outList;
		}
	} // close inner class     
}
