package client;
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
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import res.SoundPlayer;

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
	//private ArrayList<Boolean> state_Point = new ArrayList<Boolean>();

	public Make_GamePanel(String imgURL,int maxTime,String URL,int[][] Level_Point,int[][] Level_Point_Size){
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
		timeLabel.setFont(new Font("1훈화양연화 R",Font.CENTER_BASELINE,15));
		scoreLabel.setFont(new Font("1훈화양연화 R",Font.CENTER_BASELINE,15));
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

		progressBarTimer = new Timer(1000,new TimeBar());

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
													
			//System.out.println("" + point.getX() + " " + point.getY());
			if((x>613&&x<1174)&&(y>131&&y<750)){
				if (find_Point(0, point)&&deduplication[0]==0) {											// 미리 틀린곳의 좌표를 배열에 넣고 내가 누른 좌표값을 비교한다.
					true_Sound.startPlay();
					isTrue[0] = true;
					correct++;
					deduplication[0]++;
					totalScore+=10;
				} 
				if (find_Point(1, point)&&deduplication[1]==0) {											// 미리 틀린곳의 좌표를 배열에 넣고 내가 누른 좌표값을 비교한다.
					true_Sound.startPlay();
					isTrue[1] = true;
					correct++;
					deduplication[1]++;
					totalScore+=10;
				} 
				if (find_Point(2, point)&&deduplication[2]==0) {											// 미리 틀린곳의 좌표를 배열에 넣고 내가 누른 좌표값을 비교한다.
					true_Sound.startPlay();
					isTrue[2] = true;
					correct++;
					deduplication[2]++;
					totalScore+=10;
				} 
				if (find_Point(3, point)&&deduplication[3]==0) {											// 미리 틀린곳의 좌표를 배열에 넣고 내가 누른 좌표값을 비교한다.
					true_Sound.startPlay();
					isTrue[3] = true;
					correct++;
					deduplication[3]++;
					totalScore+=10;
				} 
				if (find_Point(4, point)&&deduplication[4]==0) {											// 미리 틀린곳의 좌표를 배열에 넣고 내가 누른 좌표값을 비교한다.
					true_Sound.startPlay();
					isTrue[4] = true;
					correct++;
					deduplication[4]++;
					totalScore+=10;
				} 
				if (find_Point(5, point)&&deduplication[5]==0) {											// 미리 틀린곳의 좌표를 배열에 넣고 내가 누른 좌표값을 비교한다.
					true_Sound.startPlay();
					isTrue[5] = true;
					correct++;
					deduplication[5]++;
					totalScore+=10;
				} 
				if (find_Point(6, point)&&deduplication[6]==0) {											// 미리 틀린곳의 좌표를 배열에 넣고 내가 누른 좌표값을 비교한다.
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

		g2d.setStroke(new BasicStroke(8));						
		g2d.setColor(Color.GREEN);
		if(magicEye_Boolean){
			g2d.drawOval(Level_Point[0][magicEye_Count],Level_Point[1][magicEye_Count],Level_Point_Size[0][magicEye_Count],Level_Point_Size[1][magicEye_Count]);
			g2d.drawOval(Level_Point[0][magicEye_Count]-600,Level_Point[1][magicEye_Count],Level_Point_Size[0][magicEye_Count],Level_Point_Size[1][magicEye_Count]);	
		}
		g2d.setColor(Color.blue);
		if (isTrue[0] == true) {								
			g2d.drawOval(Level_Point[0][0],Level_Point[1][0],Level_Point_Size[0][0],Level_Point_Size[1][0]);
			g2d.drawOval(Level_Point[0][0]-600,Level_Point[1][0],Level_Point_Size[0][0],Level_Point_Size[1][0]);				
		}
		if (isTrue[1] == true) {
			g2d.drawOval(Level_Point[0][1],Level_Point[1][1],Level_Point_Size[0][1],Level_Point_Size[1][1]);
			g2d.drawOval(Level_Point[0][1]-600,Level_Point[1][1],Level_Point_Size[0][1],Level_Point_Size[1][1]);
		}
		if (isTrue[2] == true) {	
			g2d.drawOval(Level_Point[0][2],Level_Point[1][2],Level_Point_Size[0][2],Level_Point_Size[1][2]);
			g2d.drawOval(Level_Point[0][2]-600,Level_Point[1][2],Level_Point_Size[0][2],Level_Point_Size[1][2]);
		}
		if (isTrue[3] == true) {		
			g2d.drawOval(Level_Point[0][3],Level_Point[1][3],Level_Point_Size[0][3],Level_Point_Size[1][3]);
			g2d.drawOval(Level_Point[0][3]-600,Level_Point[1][3],Level_Point_Size[0][3],Level_Point_Size[1][3]);
		}
		if (isTrue[4] == true) {				
			g2d.drawOval(Level_Point[0][4],Level_Point[1][4],Level_Point_Size[0][4],Level_Point_Size[1][4]);
			g2d.drawOval(Level_Point[0][4]-600,Level_Point[1][4],Level_Point_Size[0][4],Level_Point_Size[1][4]);
		} 
		if (isTrue[5] == true) {				
			g2d.drawOval(Level_Point[0][5],Level_Point[1][5],Level_Point_Size[0][5],Level_Point_Size[1][5]);
			g2d.drawOval(Level_Point[0][5]-600,Level_Point[1][5],Level_Point_Size[0][5],Level_Point_Size[1][5]);
		} 
		if (isTrue[6] == true) {				
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
}//class


/*
	public void setUp(){		    

		this.setLayout(null);
		this.setSize(1200,850);
		this.add(progressBar);
		progressBar.setSize(600,30);
		progressBar.setLocation(30,780);
		this.add(timeLabel);
		timeLabel.setSize(100,50);
		timeLabel.setLocation(1000,780);
		this.add(scoreLabel);
		scoreLabel.setSize(100,50);
		scoreLabel.setLocation(650,780);
		this.add(countLabel);
		countLabel.setSize(1200,100);
		countLabel.setLocation(0,0);

		progressBarTimer = new Timer(1000,new TimeBar());

		for(int i=0;i<7;i++){
			btn[i] = new JButton(); 
			btn[i].setSize(50,50);
			//		pair_btn[i] = new JButton();
		//	pair_btn[i].setSize(40,40);
		}
		btn[0].setLocation(Level_Point[0][0]+590,Level_Point[1][0]);
		btn[1].setLocation(Level_Point[0][1]+590,Level_Point[1][1]);
		btn[2].setLocation(Level_Point[0][2]+590,Level_Point[1][2]);
		btn[3].setLocation(Level_Point[0][3]+590,Level_Point[1][3]);
		btn[4].setLocation(Level_Point[0][4]+590,Level_Point[1][4]);
		btn[5].setLocation(Level_Point[0][5]+590,Level_Point[1][5]);
		btn[6].setLocation(Level_Point[0][6]+590,Level_Point[1][6]);

		pair_btn[0].setLocation(Level_Point[0][0],Level_Point[1][0]);
		pair_btn[1].setLocation(Level_Point[0][1],Level_Point[1][1]);
		pair_btn[2].setLocation(Level_Point[0][2],Level_Point[1][2]);
		pair_btn[3].setLocation(Level_Point[0][3],Level_Point[1][3]);
		pair_btn[4].setLocation(Level_Point[0][4],Level_Point[1][4]);
		pair_btn[5].setLocation(Level_Point[0][5],Level_Point[1][5]);
		pair_btn[6].setLocation(Level_Point[0][6],Level_Point[1][6]);


		for(int i=0;i<7;i++){
		//	this.btn[i].setOpaque(false);
		//	this.btn[i].setBorderPainted(false);
		//	this.btn[i].setContentAreaFilled(false);
		//	this.pair_btn[i].setOpaque(false);
		//	this.pair_btn[i].setBorderPainted(false);
			btn[i].addActionListener(this);
			this.add(btn[i]);
		//	this.add(pair_btn[i]);
		}
		frame.setSize(1200,800);
	}	
	protected void paintComponent(Graphics g) {	img.draw(g);}

	class TimeBar extends JPanel implements ActionListener{
		public void actionPerformed(ActionEvent e) {

			timeLabel.setText("남은시간 : "+ progressBar.getTime()+"초");
			try {progressBar.repaint();}// 프로그레스바 repaint()
			catch (Exception e1) {};

		}
	}
	public int getCount(){
		return this.count;
	}
	public void setCount(int count){
		this.count = count;
	}

	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getSource()==btn[0]&&findState[0]==false);

		if(e.getSource()==btn[0]&&findState[0]==false){
			System.out.println(0);
			findState[0] = true;  
			btn[0].setIcon(new ImageIcon("맞춤.jpg"));
			btn[0].setOpaque(false);
			btn[0].setContentAreaFilled(true);

		//btn[0].setEnabled(false);  
			count++;
			totalScore++;
		}
		else if(e.getSource()==btn[1]&&findState[1]==false){
			System.out.println(1);
			findState[1] = true;  
		//	btn[1].setEnabled(false); 
			btn[1].setIcon(new ImageIcon("맞춤.jpg"));
			btn[1].setOpaque(false);
			btn[1].setContentAreaFilled(true);
			count++;
			totalScore++;
		}
		else if(e.getSource()==btn[2]&&findState[2]==false){
			System.out.println(2);
			findState[2] = true;  
		//	btn[2].setEnabled(false);  
			btn[2].setIcon(new ImageIcon("맞춤.jpg"));
			btn[2].setOpaque(false);
			btn[2].setContentAreaFilled(true);
			count++;
			totalScore++;
		}
		else if(e.getSource()==btn[3]&&findState[3]==false){
			System.out.println(3);
			findState[3] = true;  
		//	btn[3].setEnabled(false);  
			btn[3].setIcon(new ImageIcon("맞춤.jpg"));
			btn[3].setOpaque(false);
			btn[3].setContentAreaFilled(true);
			count++;
			totalScore++;
		}
		else if(e.getSource()==btn[4]&&findState[4]==false){
			System.out.println(4);
			findState[4] = true;  
		//	btn[4].setEnabled(false);  
			btn[4].setIcon(new ImageIcon("맞춤.jpg"));
			btn[4].setOpaque(false);
			btn[4].setContentAreaFilled(true);
			count++;
			totalScore++;
		}
		else if(e.getSource()==btn[5]&&findState[5]==false){
			System.out.println(5);
			findState[5] = true;  
		//	btn[5].setEnabled(false);  
			btn[5].setIcon(new ImageIcon("맞춤.jpg"));
			btn[5].setOpaque(false);
			btn[5].setContentAreaFilled(true);
			count++;
			totalScore++;
		}
		else if(e.getSource()==btn[6]&&findState[6]==false){
			System.out.println(6);
			findState[6] = true;  
		//	btn[6].setEnabled(false);  
			btn[6].setIcon(new ImageIcon("맞춤.jpg"));
			btn[6].setOpaque(false);
			btn[6].setContentAreaFilled(true);
			count++;
			totalScore++;
		}
		else{}

		scoreLabel.setText("총점수 : "+totalScore*10+"점");


		if(count == 1){countLabel.setIcon(new ImageIcon("6개남음.jpg"));}
		else if(count == 2){countLabel.setIcon(new ImageIcon("5개남음.jpg"));}
		else if(count == 3){countLabel.setIcon(new ImageIcon("4개남음.jpg"));}
		else if(count == 4){countLabel.setIcon(new ImageIcon("3개남음.jpg"));}
		else if(count == 5){countLabel.setIcon(new ImageIcon("2개남음.jpg"));}
		else if(count == 6){countLabel.setIcon(new ImageIcon("1개남음.jpg"));}

	}
}
 */
