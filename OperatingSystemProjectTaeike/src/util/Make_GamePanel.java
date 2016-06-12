package util;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import res.SoundPlayer;
import util.ChatMessage.MsgType;

public class Make_GamePanel extends JPanel implements ActionListener{
	private SoundPlayer magicEye_Sound = new SoundPlayer("매직아이.wav");
	SoundPlayer stop_Sound = new SoundPlayer("시간정지.wav");
	private SoundPlayer true_Sound = new SoundPlayer("맞춤.wav");
	private SoundPlayer flase_Sound = new SoundPlayer("못맞춤.wav");
	private JLabel scoreLabel = new JLabel("0점");
	private JLabel countLabel = new JLabel(new ImageIcon("7개남음.jpg"));
	private JButton magicEye = new JButton(new ImageIcon("마술눈.jpg"));
	private JButton stopTime = new JButton(new ImageIcon("시간정지.jpg"));
	public SoundPlayer Level_BGM;
	private PosImageIcon img; //이미를 씌우기 위해서.
	public ProgressiveBar progressBar;
	private JLabel timeLabel;
	public Timer progressBarTimer;
	private Timer hindTimer;
	private LineListener mouseListener = new LineListener();
	private JButton[] btn = new  JButton[7];
	private JButton[] pair_btn = new  JButton[7];
	private Point point;
	public static int totalScore=0;
	private int count=0;
	private int[][] Level_Point;
	private int[][] Level_Point_Size;
	private int[] deduplication = new int[7];
	private int x=0,y=0;
	private int correct=0;
	private boolean[] isTrue = new boolean[7];
	private boolean clear =false;
	private boolean stopTimeBoolean = false;
	private int stopCount=0;
	private int magicEye_Count;
	private boolean magicEye_Boolean = false;
	private String user;
	private String opponentName;
	private ObjectOutputStream writer;
	private int level;
	private int flag=0;
	private boolean[] owner = new boolean[7];
	private JButton hinButton = new JButton(new ImageIcon("먹.jpg"));
	private int hind = 0,opHind = 0;
	private PosImageIcon muck = new PosImageIcon("먹물.jpg", 613, 131, 561, 640);
	//private ArrayList<Boolean> state_Point = new ArrayList<Boolean>();

	public Make_GamePanel(String imgURL,int maxTime,String URL,int[][] Level_Point,int[][] Level_Point_Size,
			String sender,String reciver,ObjectOutputStream writer,int totalScore,int level){
		img = new PosImageIcon(imgURL,0,100,1200,750);
		progressBar = new ProgressiveBar(maxTime);
		timeLabel = new JLabel(maxTime+"초");
		Level_BGM = new SoundPlayer(URL);
		this.Level_Point = Level_Point;
		this.Level_Point_Size=Level_Point_Size;
		point = new Point();
		for(int i=0;i<7;i++){isTrue[i] = false;}
		for(int i=0;i<7;i++){deduplication[i]=0;}
		timeLabel.setForeground(Color.GRAY);
		scoreLabel.setForeground(Color.GRAY);
		this.totalScore = totalScore;
		this.user = sender;
		this.opponentName = reciver;
		this.writer = writer;
		this.level = level;
		//timeLabel.setFont(new Font("1훈화양연화 R",Font.CENTER_BASELINE,15));
		//	scoreLabel.setFont(new Font("1훈화양연화 R",Font.CENTER_BASELINE,15));
	}	

	public void setUp(){

		this.setLayout(null);
		this.setSize(1200,850);

		progressBar.setSize(570,30);
		progressBar.setLocation(10,780);
		this.add(progressBar);

		timeLabel.setSize(130,60);
		timeLabel.setLocation(735,770);
		this.add(timeLabel);

		scoreLabel.setSize(130,60);
		scoreLabel.setLocation(880,770);
		this.add(scoreLabel);


		countLabel.setSize(1200,100);
		countLabel.setLocation(0,0);
		this.add(countLabel);

		magicEye.setSize(100,30);
		magicEye.setLocation(1064,778);
		magicEye.addActionListener(this);
		this.add(magicEye);

		stopTime.setSize(100,30);
		stopTime.setLocation(945,778);
		stopTime.addActionListener(this);
		this.add(stopTime);

		hinButton.setBounds(910,778,30,30);
		hinButton.addActionListener(new HindButtonHandler());
		hinButton.setOpaque(false);
		hinButton.setContentAreaFilled(false);
		hinButton.setBorderPainted(false);
		this.add(hinButton);

		progressBarTimer = new Timer(1000,new TimeBar());
		hindTimer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(opHind != 0){
					opHind--;
					repaint();
				}
				else{
					hindTimer.stop();
				}
			}
		});
		this.addMouseListener(mouseListener);
	}
	public boolean find_Point(int index, Point click) {
		int pointX = click.x;
		int pointY = click.y;
		int distanceX=click.x-Level_Point[0][index];
		int distanceY=click.y-Level_Point[1][index];

		return ((Level_Point[0][index]<click.x)&&(distanceX<Level_Point_Size[0][index])&&(Level_Point[1][index]<click.y)&&(distanceY<Level_Point_Size[0][index]));
	}

	public int false_Point() {
		if(isTrue[0]||isTrue[1]||isTrue[2]||isTrue[3]||isTrue[4]||isTrue[5]||isTrue[6]==false) 
			return 1;
		else 
			return 0;
	}
	public boolean clearLevel(){
		if(isTrue[0]&&isTrue[1]&&isTrue[2]&&isTrue[3]&&isTrue[4]&&isTrue[5]&&isTrue[6]) clear = true;
		return clear;
	}
	public boolean checkState(){
		for(int i=0;i<7;i++){
			if(isTrue[i]==false) return false;
		}
		return true;
	}
	public class LineListener implements MouseListener {		
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {								// 마우스 누를때 마다 실행
			correct = 0;
			point = e.getPoint();	//좌표받아오기
			int x = (int) point.getX();
			int y = (int) point.getY();
			
			System.out.println("" + point.getX() + " " + point.getY());
			if((x>613&&x<1174)&&(y>131&&y<750)){

				if (find_Point(0, point)&&deduplication[0]==0) {											// 미리 틀린곳의 좌표를 배열에 넣고 내가 누른 좌표값을 비교한다.
					sendClearPoint(0);
					true_Sound.startPlay();
					isTrue[0] = true;
					correct++;
					deduplication[0]++;
					totalScore+=10;
				} 
				if (find_Point(1, point)&&deduplication[1]==0) {											// 미리 틀린곳의 좌표를 배열에 넣고 내가 누른 좌표값을 비교한다.
					sendClearPoint(1);
					true_Sound.startPlay();
					isTrue[1] = true;
					correct++;
					deduplication[1]++;
					totalScore+=10;
				} 
				if (find_Point(2, point)&&deduplication[2]==0) {											// 미리 틀린곳의 좌표를 배열에 넣고 내가 누른 좌표값을 비교한다.
					sendClearPoint(2);
					true_Sound.startPlay();
					isTrue[2] = true;
					correct++;
					deduplication[2]++;
					totalScore+=10;
				} 
				if (find_Point(3, point)&&deduplication[3]==0) {											// 미리 틀린곳의 좌표를 배열에 넣고 내가 누른 좌표값을 비교한다.
					sendClearPoint(3);
					true_Sound.startPlay();
					isTrue[3] = true;
					correct++;
					deduplication[3]++;
					totalScore+=10;
				} 
				if (find_Point(4, point)&&deduplication[4]==0) {											// 미리 틀린곳의 좌표를 배열에 넣고 내가 누른 좌표값을 비교한다.
					sendClearPoint(4);
					true_Sound.startPlay();
					isTrue[4] = true;
					correct++;
					deduplication[4]++;
					totalScore+=10;
				} 
				if (find_Point(5, point)&&deduplication[5]==0) {											// 미리 틀린곳의 좌표를 배열에 넣고 내가 누른 좌표값을 비교한다.
					sendClearPoint(5);
					true_Sound.startPlay();
					isTrue[5] = true;
					correct++;
					deduplication[5]++;
					totalScore+=10;
				} 
				if (find_Point(6, point)&&deduplication[6]==0) {											// 미리 틀린곳의 좌표를 배열에 넣고 내가 누른 좌표값을 비교한다.
					sendClearPoint(6);
					true_Sound.startPlay();
					isTrue[6] = true;
					correct++;
					deduplication[6]++;
					totalScore+=10;
				} 
				if(correct==0){ 	//틀릴때
					flase_Sound.startPlay();
					totalScore-=5;
				}
				if(checkState()){
					sendNextState();
				}
				repaint();


			}
		}
	}
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		img.draw(g2d);
		// 정답확인시 체크
		x = (int) point.getX();
		y = (int) point.getY();
		if(opHind == 0){
			g2d.setStroke(new BasicStroke(8));						
			g2d.setColor(Color.GREEN);
			if(magicEye_Boolean){
				g2d.drawOval(Level_Point[0][magicEye_Count],Level_Point[1][magicEye_Count],Level_Point_Size[0][magicEye_Count],Level_Point_Size[1][magicEye_Count]);
				g2d.drawOval(Level_Point[0][magicEye_Count]-600,Level_Point[1][magicEye_Count],Level_Point_Size[0][magicEye_Count],Level_Point_Size[1][magicEye_Count]);	
			}
			g2d.setColor(Color.blue);
			if (isTrue[0] == true) {								
				if(owner[0] == false) g2d.setColor(Color.blue);
				else g2d.setColor(Color.red);
				g2d.drawOval(Level_Point[0][0],Level_Point[1][0],Level_Point_Size[0][0],Level_Point_Size[1][0]);
				g2d.drawOval(Level_Point[0][0]-600,Level_Point[1][0],Level_Point_Size[0][0],Level_Point_Size[1][0]);				
			}
			if (isTrue[1] == true) {
				if(owner[1] == false) g2d.setColor(Color.blue);
				else g2d.setColor(Color.red);
				g2d.drawOval(Level_Point[0][1],Level_Point[1][1],Level_Point_Size[0][1],Level_Point_Size[1][1]);
				g2d.drawOval(Level_Point[0][1]-600,Level_Point[1][1],Level_Point_Size[0][1],Level_Point_Size[1][1]);
			}
			if (isTrue[2] == true) {
				if(owner[2] == false) g2d.setColor(Color.blue);
				else g2d.setColor(Color.red);
				g2d.drawOval(Level_Point[0][2],Level_Point[1][2],Level_Point_Size[0][2],Level_Point_Size[1][2]);
				g2d.drawOval(Level_Point[0][2]-600,Level_Point[1][2],Level_Point_Size[0][2],Level_Point_Size[1][2]);
			}
			if (isTrue[3] == true) {	
				if(owner[3] == false) g2d.setColor(Color.blue);
				else g2d.setColor(Color.red);
				g2d.drawOval(Level_Point[0][3],Level_Point[1][3],Level_Point_Size[0][3],Level_Point_Size[1][3]);
				g2d.drawOval(Level_Point[0][3]-600,Level_Point[1][3],Level_Point_Size[0][3],Level_Point_Size[1][3]);
			}
			if (isTrue[4] == true) {
				if(owner[4] == false) g2d.setColor(Color.blue);
				else g2d.setColor(Color.red);
				g2d.drawOval(Level_Point[0][4],Level_Point[1][4],Level_Point_Size[0][4],Level_Point_Size[1][4]);
				g2d.drawOval(Level_Point[0][4]-600,Level_Point[1][4],Level_Point_Size[0][4],Level_Point_Size[1][4]);
			} 
			if (isTrue[5] == true) {
				if(owner[5] == false) g2d.setColor(Color.blue);
				else g2d.setColor(Color.red);
				g2d.drawOval(Level_Point[0][5],Level_Point[1][5],Level_Point_Size[0][5],Level_Point_Size[1][5]);
				g2d.drawOval(Level_Point[0][5]-600,Level_Point[1][5],Level_Point_Size[0][5],Level_Point_Size[1][5]);
			} 
			if (isTrue[6] == true) {
				if(owner[6] == false) g2d.setColor(Color.blue);
				else g2d.setColor(Color.red);
				g2d.drawOval(Level_Point[0][6],Level_Point[1][6],Level_Point_Size[0][6],Level_Point_Size[1][6]);
				g2d.drawOval(Level_Point[0][6]-600,Level_Point[1][6],Level_Point_Size[0][6],Level_Point_Size[1][6]);
			} 
			if(correct==0){		// 틀린 좌표값을 입력했을때 X 표시					
				g2d.setColor(Color.RED);
				g2d.drawLine(x - 7, y - 8, x + 7, y + 8);
				g2d.drawLine(x - 8, y - 8, x + 8, y + 8);
				g2d.drawLine(x - 8, y + 8, x + 8, y - 7);

				g2d.drawLine(x - 8, y + 7, x + 7, y - 8);
				g2d.drawLine(x - 8, y + 8, x + 8, y - 8);
				g2d.drawLine(x - 7, y + 8, x + 8, y - 7);
			}

			scoreLabel.setText(totalScore+"점");
			count = (isTrue[0] ? 1 : 0) + (isTrue[1] ? 1 : 0) + (isTrue[2] ? 1 : 0) + (isTrue[3] ? 1 : 0) + (isTrue[4] ? 1 : 0) + (isTrue[5] ? 1 : 0) + (isTrue[6] ? 1 : 0);			
			if(count == 1){countLabel.setIcon(new ImageIcon("6개남음.jpg"));}
			else if(count == 2){countLabel.setIcon(new ImageIcon("5개남음.jpg"));}
			else if(count == 3){countLabel.setIcon(new ImageIcon("4개남음.jpg"));}
			else if(count == 4){countLabel.setIcon(new ImageIcon("3개남음.jpg"));}
			else if(count == 5){countLabel.setIcon(new ImageIcon("2개남음.jpg"));}
			else if(count == 6){countLabel.setIcon(new ImageIcon("1개남음.jpg"));}
		}
		else{
			muck.draw(g2d);
		}
	}
	class TimeBar implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(stopTimeBoolean == false){
				try {progressBar.repaint();}// 프로그레스바 repaint()
				catch (Exception e1) {};
				timeLabel.setText( progressBar.getTime()+"초");
			}
			else{	
				if(stopCount==7){
					stopTimeBoolean=false;
					stop_Sound.stopPlayer();
				}
				stopCount++;
			}
		}
	}

	public void actionPerformed(ActionEvent e) {	
		if(e.getSource() == magicEye){
			for(int i=0;i<7;i++){
				if(isTrue[i]==false){
					magicEye_Boolean= true;
					magicEye_Count=i;
					magicEye_Sound.startPlay();
					repaint();
					break;
				}
			}
			magicEye.setEnabled(false);
		}
		else{
			stop_Sound.startPlay();
			stopTimeBoolean=true;
			stopTime.setEnabled(false);
		}

	}//action
	public void sendNextState(){
		if(flag==0){
			flag++;
			if(level != 4){
				Level_BGM.stopPlayer();
				progressBarTimer.stop();

				try {
					writer.writeObject(new ChatMessage(ChatMessage.MsgType.NEXT, user,opponentName,totalScore,level));
					writer.flush();
				} catch(Exception ex) {
					ex.printStackTrace();
				}	
			}
			else{
				try {
					writer.writeObject(new ChatMessage(ChatMessage.MsgType.ACCEPTOPSCORE, user,opponentName,totalScore,level));
					writer.flush();
				} catch(Exception ex) {
					ex.printStackTrace();
				}	
			}
		}
	}
	private void sendClearPoint(int index){
		try {
			writer.writeObject(new ChatMessage(ChatMessage.MsgType.POINT, user,opponentName,index));
			writer.flush();
		} catch(Exception ex) {
			ex.printStackTrace();
		}	
	}
	public void displayPoint(int index){
		isTrue[index] = true;
		owner[index] = true;
		deduplication[index]++;
		this.repaint();
	}
	public int getScore(){
		return totalScore;
	}
	public void hindrance(){
		opHind = 10;
		hindTimer.start();
		System.out.println("ddd");
	}

	private class HindButtonHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(hind == 0){
				try {
					writer.writeObject(new ChatMessage(ChatMessage.MsgType.HINDRANCE, user,opponentName,""));
					writer.flush();
				} catch(Exception ex) {
					ex.printStackTrace();
				}	
				hind++;
			}
		}
	}
}//class


