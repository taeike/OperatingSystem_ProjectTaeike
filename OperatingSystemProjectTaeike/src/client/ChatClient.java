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
	//************************�ϴܰ� Ʋ����ġ�� Ʋ���׸��� ũ��**************************
	private final int[][] Level1_Point = {{744,1004,1110,1065,615,697,905},
											  {480,430,397,600,634,308,592}};	

	private final int[][] Level1_Point_Size ={{40,40,40,70,70,60,50},
				                                  {40,40,50,50,50,70,50}};
	private Make_GamePanel Level1;
	//************************�̴ܰ� Ʋ����ġ�� Ʋ���׸��� ũ��**************************
	private final int[][] Level2_Point = {{938,1044,995,968,957,1094,1117},
				                              {205,503,358,499,713,540,690}};	

	private final int[][] Level2_Point_Size ={{40,50,40,40,40,60,50},
				                                  {50,50,40,40,40,40,50}};
	private Make_GamePanel Level2;

	//************************��ܰ� Ʋ����ġ�� Ʋ���׸��� ũ��**************************
	private final int[][] Level3_Point = {{785,1100,770,775,1124,998,919},
				                              {224,247,720,542,137,523,642}};	

	private final int[][] Level3_Point_Size ={{50,40,70,40,30,40,40},
				                                  {50,150,40,40,40,40,50}};
	private Make_GamePanel Level3;
	//************************��ܰ� Ʋ����ġ�� Ʋ���׸��� ũ��**************************
	private final int[][] Level4_Point = {{695,914,650,1005,810,1086,995},
				                              {270,272,682,420,243,222,693}};	

	private final int[][] Level4_Point_Size ={{50,40,50,40,50,50,100},
				                                  {50,50,40,50,50,80,60}};
	private Make_GamePanel Level4;
	
	
	
	ArrayList<PosImageIcon> imgList = new ArrayList<PosImageIcon>();
	int count = -1;
	
	JFrame frame;
	String frameTitle = "ä�� Ŭ���̾�Ʈ";
	JTextArea incoming;			// ���ŵ� �޽����� ����ϴ� ��
	JTextArea outgoing;			// �۽��� �޽����� �ۼ��ϴ� ��
	JList counterParts;			// ���� �α����� ä�� ������� ��Ÿ���� ����Ʈ.
	ObjectInputStream reader;	// ���ſ� ��Ʈ��
	ObjectOutputStream writer;	// �۽ſ� ��Ʈ��
	Socket sock;				// ���� ����� ����
	String user;				// �� Ŭ���̾�Ʈ�� �α��� �� ������ �̸�
	JButton loginButton;			// ����� �Ǵ� �α���/�α׾ƿ� ��ư
	JPanel loginPanel,delayPanel;
	Timer timer;
	PosImageIcon LoginPanelImage = new PosImageIcon("�α���ȭ��.jpg", 0, 0,1200 , 850);
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
		
		imgList.add(new PosImageIcon("1���İ��ӽ���.jpg", 0,0,1200,850));
		imgList.add(new PosImageIcon("2���İ��ӽ���.jpg", 0,0,1200,850));
		imgList.add(new PosImageIcon("3���İ��ӽ���.jpg", 0,0,1200,850));	
		delayPanel = new JPanel(){
			@Override
			protected void paintComponent(Graphics g) {
				if(count == 0) imgList.get(2).draw(g);
				else if(count == 1) imgList.get(1).draw(g);
				else if(count == 2)	imgList.get(0).draw(g);
			}
		};
		// build GUI
		frame = new JFrame(frameTitle + " : �α����ϼ���");


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
			// ck = new cket("220.69.203.11", 5000);		// �������� ��ǻ��
			sock = new Socket("127.0.0.1", 5000);			// ���� ����� ���� ��Ʈ�� 5000�� ���Ű�� ��
			reader = new ObjectInputStream(sock.getInputStream());
			writer = new ObjectOutputStream(sock.getOutputStream());
		} catch(Exception ex) {
			JOptionPane.showMessageDialog(null, "�������ӿ� �����Ͽ����ϴ�. ������ �����մϴ�.");
			ex.printStackTrace();
			frame.dispose();		// ��Ʈ��ũ�� �ʱ� ���� �ȵǸ� Ŭ���̾�Ʈ ���� ����
		}
	} // close setUpNetworking   

	// �α��ΰ� �ƿ��� ����ϴ� ��ư�� ��û��. ó������ Login �̾��ٰ� �ϴ� �α��� �ǰ��� Logout�� ó��
	private class LogButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			processLogin();
		}
		// �α��� ó��
		private void processLogin() {
			try {
				writer.writeObject(new ChatMessage(ChatMessage.MsgType.LOGIN, nameText.getText(), "", ""));
				writer.flush();
				frame.setTitle(frameTitle + " ( �α��� : " +nameText.getText() + ")" );
				user = nameText.getText();
		
			} catch(Exception ex) {
				JOptionPane.showMessageDialog(null, "�α��� �� �������ӿ� ������ �߻��Ͽ����ϴ�.");
				ex.printStackTrace();
			}
		}
		// �α׾ƿ� ó��
		private void processLogout() {
			int choice = JOptionPane.showConfirmDialog(null, "Logout�մϴ�");
			if (choice == JOptionPane.YES_OPTION) {
				try {
					writer.writeObject(new ChatMessage(ChatMessage.MsgType.LOGOUT, user, "", ""));
					writer.flush();
					// ����� ��� ��Ʈ���� ������ �ݰ� ���α׷��� ���� ��
					writer.close(); reader.close(); sock.close();
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(null, "�α׾ƿ� �� �������ӿ� ������ �߻��Ͽ����ϴ�. ���������մϴ�");
					ex.printStackTrace();
				} finally {
					System.exit(100);			// Ŭ���̾�Ʈ ���� ���� 
				}
			}
		}
	}  // close LoginButtonListener inner class

	public class SendButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			String to = (String) counterParts.getSelectedValue();
			if (to == null) {
				JOptionPane.showMessageDialog(null, "�۽��� ����� ������ �� �޽����� ��������");
				return;
			}
			try {
				incoming.append(user + " : " + outgoing.getText() + "\n"); // ���� �޽��� â�� ���̱�
				writer.writeObject(new ChatMessage(ChatMessage.MsgType.CLIENT_MSG, user, to, outgoing.getText()));
				writer.flush();
				outgoing.setText("");
				outgoing.requestFocus();
			} catch(Exception ex) {
				JOptionPane.showMessageDialog(null, "�޽��� ������ ������ �߻��Ͽ����ϴ�.");
				ex.printStackTrace();
			}
		}
	}  // close SendButtonListener inner class

	// �������� ������ �޽����� �޴� ������ �۾��� �����ϴ� Ŭ����
	public class IncomingReader implements Runnable {
		public void run() {
			ChatMessage message;             
			ChatMessage.MsgType type;
			try {
				while (true) {
					message = (ChatMessage) reader.readObject();     	 // �����α� ������ �޽��� ���                   
					type = message.getType();
					if (type == ChatMessage.MsgType.LOGIN_FAILURE) {	 // �α����� ������ �����
						JOptionPane.showMessageDialog(null, "Login�� �����Ͽ����ϴ�. �ٽ� �α����ϼ���");
						frame.setTitle(frameTitle + " : �α��� �ϼ���");
						loginButton.setText("Login");
					} else if (type == ChatMessage.MsgType.SERVER_MSG) { // �޽����� �޾Ҵٸ� ������
						System.out.println(message.getContents());
				    	if (message.getSender().equals(user)) continue;  // ���� ���� ������ ���� �ʿ� ����
						AnswerWindow anw = new AnswerWindow(message,writer);
					} 
					else if (type == ChatMessage.MsgType.LOGIN_LIST) {
						// ���� ����Ʈ�� ���� �ؼ� counterParts ����Ʈ�� �־� ��.
						// ����  ���� (""�� ����� ���� �� ����Ʈ �� �տ� ���� ��)
						String[] users = message.getContents().split("/");
						for (int i=0; i<users.length; i++) {
							if (user.equals(users[i])) users[i] = "";
						}
						users = sortUsers(users);		// ���� ����� ���� �� �� �ֵ��� �����ؼ� ����
						users[0] =  ChatMessage.ALL;	// ����Ʈ �� �տ� "��ü"�� ������ ��
						gameStartPanel.so.setCounterParts( users );
					}
					else if (type == ChatMessage.MsgType.NO_ACT){
						// �ƹ� �׼��� �ʿ���� �޽���. �׳� ��ŵ		 
					}
					else if(type == ChatMessage.MsgType.PASSLOGIN){
						System.out.println("�α����� �ϰ� ���� ȭ������ �Ѿ");            		
						so = new SelectOpponent(user,frame,writer);
						gameStartPanel = new GameStartButton_Panel(frame,so);
						frame.getContentPane().removeAll(); // ��ϵ� ��� �����̳� ����
						frame.getContentPane().add(gameStartPanel.panel); // �ٽ� ���
						frame.setContentPane(frame.getContentPane()); // �����ӿ� ���� (this : Frame )
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
						Level1 = new Make_GamePanel("�ϴܰ� ����ȭ��.jpg",70,"3�ܰ� �������.wav",Level1_Point,Level1_Point_Size
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
						users = sortUsers(users);		// ���� ����� ���� �� �� �ֵ��� �����ؼ� ����
						users[0] =  ChatMessage.ALL;	// ����Ʈ �� �տ� "��ü"�� ������ ��
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
						throw new Exception("�������� �� �� ���� �޽��� ��������");
					}
				} // close while
			} catch(Exception ex) {
				ex.printStackTrace();
				System.out.println("Ŭ���̾�Ʈ ������ ����");		// �������� ����� ��� �̸� ���� ������ ����
			}
		} // close rungasgege
		private void showResult(ChatMessage message){
			ResultPanel result;
			System.out.println( message.getScore()+" ? "+ Level4.getScore());
			so = new SelectOpponent(user, frame, writer);
			if(message.getScore() < Level4.getScore()){
				System.out.println("�̱�");
				result = new ResultPanel("win", frame, message.getReceiver(), writer,so,message.getScore(),Level4.getScore());
			}
			else{
				result = new ResultPanel("lose", frame, message.getReceiver(), writer,so,message.getScore(),Level4.getScore());
			}
			result.setBounds(0, 0, 1200, 850);
			result.setLayout(null);
			
			frame.getContentPane().removeAll(); // ��ϵ� ��� �����̳� ����
			frame.getContentPane().add(result); // �ٽ� ���
			frame.setContentPane(frame.getContentPane()); // �����ӿ� ���� (this : Frame )
		}
		private void changePanel(String sender,String receiver,int index){
			
			if(Level == 1){
				Level2 = new Make_GamePanel("�̴ܰ� ����ȭ��.jpg",70,"2�ܰ� �������.wav",Level2_Point,Level2_Point_Size,receiver,sender,writer,Level1.getScore(),2);
				Level2.setUp();
				Level2.progressBarTimer.start();
				Level2.Level_BGM.startPlay();
				Level1.Level_BGM.stopPlayer();
				Level1.progressBarTimer.stop();
				frame.getContentPane().removeAll(); // ��ϵ� ��� �����̳� ����
				frame.getContentPane().add(Level2); // �ٽ� ���
				frame.setContentPane(frame.getContentPane()); // �����ӿ� ���� (this : Frame )
				Level++;
			}
			else if(Level == 2){
				Level3 = new Make_GamePanel("��ܰ� ����ȭ��.jpg",70,"1�ܰ� �������.wav",Level3_Point,Level3_Point_Size,receiver,sender,writer,Level2.getScore(),3);	
				Level3.setUp();
				Level3.progressBarTimer.start();
				Level3.Level_BGM.startPlay();
				Level2.Level_BGM.stopPlayer();
				Level2.progressBarTimer.stop();
				frame.getContentPane().removeAll(); // ��ϵ� ��� �����̳� ����
				frame.getContentPane().add(Level3); // �ٽ� ���
				frame.setContentPane(frame.getContentPane()); // �����ӿ� ���� (this : Frame )
				Level++;
			}
			else if(Level == 3){
				Level4 = new Make_GamePanel("��ܰ� ����ȭ��.jpg",70,"4�ܰ� �������.wav",Level4_Point,Level4_Point_Size,receiver,sender,writer,Level3.getScore(),4);
				Level4.setUp();
				Level4.progressBarTimer.start();
				Level4.Level_BGM.startPlay();
				Level3.Level_BGM.stopPlayer();
				Level3.progressBarTimer.stop();
				frame.getContentPane().removeAll(); // ��ϵ� ��� �����̳� ����
				frame.getContentPane().add(Level4); // �ٽ� ���
				frame.setContentPane(frame.getContentPane()); // �����ӿ� ���� (this : Frame )
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
				frame.getContentPane().removeAll(); // ��ϵ� ��� �����̳� ����
				frame.getContentPane().add(delayPanel); // �ٽ� ���
				frame.setContentPane(frame.getContentPane()); // �����ӿ� ���� (this : Frame )
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
					frame.getContentPane().removeAll(); // ��ϵ� ��� �����̳� ����
					frame.getContentPane().add(Level1); // �ٽ� ���
					frame.setContentPane(frame.getContentPane()); // �����ӿ� ���� (this : Frame )
					count ++;
					timer = null;;
				}
			}
		}

		// �־��� String �迭�� ������ ���ο� �迭 ����
		private String [] sortUsers(String [] users) {
			String [] outList = new String[users.length];
			ArrayList<String> list = new ArrayList<String>();
			for (String s : users) {
				list.add(s);
			}
			Collections.sort(list);				// Collections.sort�� ����� �ѹ濡 ����
			for (int i=0; i<users.length; i++) {
				outList[i] = list.get(i);
			}
			return outList;
		}
	} // close inner class     
}
