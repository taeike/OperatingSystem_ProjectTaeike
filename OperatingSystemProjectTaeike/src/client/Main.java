package client;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import client.FirstButton_Panel;


public class Main implements  ActionListener{//implements ActionListener{

	private Cursor cursor;//마우스에 이미지를 주기위한것
	private Image img;
	private Point point = new Point(0,0);

	private JLayeredPane layerPane = new JLayeredPane();//순차적으로 화면을 바꾸기위한 패널준비
	private FirstButton_Panel firstButton_Panel = new FirstButton_Panel();//바로 처음 떠주는 화면
	JFrame frame = new JFrame();
	private GameStartButton_Panel gameStartButton_Panel = new GameStartButton_Panel(frame);

	//******************게임 클리어시 나오는 버튼 패널***********************************//
	private Next_Button next_Button1 = new Next_Button();//1단계 클리어
	private Next_Button next_Button2 = new Next_Button();//2단계 클리어
	private Next_Button next_Button3 = new Next_Button();//3단계 클리어
	private False_Panel false_Panel;//실패했을때 ...

	private Timer gameStartTimer;//    3/2/1/게임화면을 해주기위한 타이머
	private Timer repaintTimer;//      계속해서 조건문을 검사해서 확인j해주는 타이머
	//************************일단계 틀린위치와 틀린그림의 크기**************************
	private final int[][] Level1_Point = {{744,1004,1110,1065,615,697,905},
										  {480,430,397,600,634,308,592}};	

	private final int[][] Level1_Point_Size ={{40,40,40,70,70,60,50},
			                                  {40,40,50,50,50,70,50}};

	//************************이단계 틀린위치와 틀린그림의 크기**************************
	private final int[][] Level2_Point = {{938,1044,995,968,957,1094,1117},
			                              {205,503,358,499,713,540,690}};	

	private final int[][] Level2_Point_Size ={{40,50,40,40,40,60,50},
			                                  {50,50,40,40,40,40,50}};

	//************************삼단계 틀린위치와 틀린그림의 크기**************************
	private final int[][] Level3_Point = {{785,1100,770,775,1124,998,919},
			                              {224,247,720,542,137,523,642}};	

	private final int[][] Level3_Point_Size ={{50,40,70,40,30,40,40},
			                                  {50,150,40,40,40,40,50}};
	//************************사단계 틀린위치와 틀린그림의 크기**************************
	private final int[][] Level4_Point = {{695,914,650,1005,810,1086,995},
			                              {270,272,682,420,243,222,693}};	

	private final int[][] Level4_Point_Size ={{50,40,50,40,50,50,100},
			                                  {50,50,40,50,50,80,60}};


	private String playerName; //이름을 받기위해
	//**************************1/2/3/4 단계 틀린그림게임화면을 찍어내주는 것************************************
	private Make_GamePanel Level1 = new Make_GamePanel("일단계 게임화면.jpg",70,"3단계 배경음악.wav",Level1_Point,Level1_Point_Size);
	private Make_GamePanel Level2 = new Make_GamePanel("이단계 게임화면.jpg",70,"2단계 배경음악.wav",Level2_Point,Level2_Point_Size);
	private Make_GamePanel Level3 = new Make_GamePanel("삼단계 게임화면.jpg",70,"1단계 배경음악.wav",Level3_Point,Level3_Point_Size);	
	private Make_GamePanel Level4 = new Make_GamePanel("사단계 게임화면.jpg",70,"4단계 배경음악.wav",Level4_Point,Level4_Point_Size);
	//*************************모두 클리어했을때**************************************************************
	private Final_Panel final_Panel = new Final_Panel(frame,playerName , Level2.totalScore , 60-Level1.progressBar.scoreTime+60+Level2.progressBar.scoreTime+Level3.progressBar.scoreTime);
	//
	private int Levelclear_Count=0;//조건문에 걸리고 중복되서 안걸리게 하기위해
	private int count = 4;         //3/2/1/화면
	private int Level = 0;		   //몇단계에서 끝났는지 알기위해

	public static void main(String[] args){
		Main project = new Main();
		project.setUp();

	}
	public void setUp(){
		//*********************마우스 이미지를 위해*******************************
		Toolkit tk = Toolkit.getDefaultToolkit();
		img = tk.getImage("붓이미지.jpg");
		cursor = tk.createCustomCursor(img,point,"붓이미지.jpg");
		frame.setCursor(cursor);
		//**********************이름을 받아주고**********************************
		playerName=JOptionPane.showInputDialog("이름을 입력해주세요 :");
		//**********************프레임 설정*************************************
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1200,850);
		frame.setLocation(300,70);
		frame.setResizable(false);//못움직이게
		frame.setTitle("                                                                                                              현대풍속화 와 현대국악이 함께 하는 틀린 그림 찾기");
		//**********************타이머 설정*************************************
		gameStartTimer = new Timer(1000,new CountTimerAction());
		repaintTimer = new Timer(1,new RepaintTimerAction());
		//***********************처음으로 띄울 패널******************************
		firstButton_Panel.setup_GUI();
		firstButton_Panel.FirstButton.addActionListener(this);
		layerPane.add(firstButton_Panel, new Integer(12));
		firstButton_Panel.setSize(1200,850);
		//***********************두번째로 띄울 게임시작 버튼***********************
		gameStartButton_Panel.setup_GUI();
		gameStartButton_Panel.gameStartButton.addActionListener(this);
		gameStartButton_Panel.explanation_Button.addActionListener(this);
		layerPane.add(gameStartButton_Panel.panel, new Integer(11));
		gameStartButton_Panel.panel.setSize(1200,850);	
		//***********************3초남음***************************************
		layerPane.add(gameStartButton_Panel.sec_3,new Integer(10));
		gameStartButton_Panel.sec_3.setSize(1200,850);
		//***********************2초남음***************************************
		layerPane.add(gameStartButton_Panel.sec_2,new Integer(9));
		gameStartButton_Panel.sec_2.setSize(1200,850);
		//***********************1초남음***************************************
		layerPane.add(gameStartButton_Panel.sec_1,new Integer(8));
		gameStartButton_Panel.sec_1.setSize(1200,850);
		//***********************1단계패널*************************************
		Level1.setUp();
		layerPane.add(Level1,new Integer(7));
		Level1.setSize(1200,850);
		//***********************1단계클리어************************************
		layerPane.add(next_Button1.panel,new Integer(6));
		next_Button1.panel.setSize(1200,850);
		next_Button1.addActionListener(this);
		//***********************2단계패널*************************************
		layerPane.add(Level2,new Integer(5));
		Level2.setSize(1200,850);
		//***********************2단계클리어***********************************
		layerPane.add(next_Button2.panel,new Integer(4));
		next_Button2.panel.setSize(1200,850);
		next_Button2.addActionListener(this);
		//***********************3단계패널*************************************
		layerPane.add(Level3,new Integer(3));
		Level2.setSize(1200,850);
		//***********************3단계클리어************************************
		layerPane.add(next_Button3.panel,new Integer(2));
		next_Button3.panel.setSize(1200,850);
		next_Button3.addActionListener(this);
		//***********************4단계패널**************************************
		layerPane.add(Level4,new Integer(1));
		Level4.setSize(1200,850);
		//***********************4단계 클리어 패널********************************
		layerPane.add(final_Panel,new Integer(0));
		final_Panel.setSize(1200,850);

		repaintTimer.start();
		frame.add(layerPane);
		frame.setVisible(true);
	}
	public void actionPerformed(ActionEvent e) {
		//************틀린그림찾기라는것을 알려준뒤 시작하기 전 버튼패널**************************************************
		if(e.getSource()==firstButton_Panel.FirstButton){
			layerPane.setLayer(gameStartButton_Panel.panel,13);
		}
		//************누르면 시작이되는 버튼패널*********************************************************************
		else if(e.getSource()==gameStartButton_Panel.gameStartButton){
			firstButton_Panel.FirstButton.setEnabled(false);
			gameStartTimer.start();
		}
		//************클리어하고 준비 할 수 있도록 만든 버튼패널을 눌렸을 경우 2단계 게임이 시작된다***************************
		else if(e.getSource()==next_Button1){ 
			next_Button1.setEnabled(false);
			Level++;
			Level2.setUp();
			Level2.Level_BGM.startPlay();
			Level2.progressBarTimer.start();
			next_Button1.next_BGM.stopPlayer();
			layerPane.setLayer(Level2,19);
		}
		//************클리어하고 준비 할 수 있도록 만든 버튼패널을 눌렸을 경우 3단계 게임이 시작된다***************************
		else if(e.getSource()==next_Button2){ 
			next_Button2.setEnabled(false);
			next_Button2.next_BGM.stopPlayer();
			Level++;
			Level3.setUp();
			Level3.Level_BGM.startPlay();
			Level3.progressBarTimer.start();
			next_Button1.next_BGM.stopPlayer();
			layerPane.setLayer(Level3,21);
		}
		//************클리어하고 준비 할 수 있도록 만든 버튼패널을 눌렸을 경우 4단계 게임이 시작된다***************************
		else if(e.getSource()==next_Button3){ 
			next_Button3.setEnabled(false);
			next_Button3.next_BGM.stopPlayer();
			Level++;
			Level4.setUp();
			Level4.Level_BGM.startPlay();
			Level4.progressBarTimer.start();
			next_Button1.next_BGM.stopPlayer();
			layerPane.setLayer(Level4,23);
		}
	}
	class CountTimerAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			//*********************3초*******************************************
			if(count==4){
				layerPane.setLayer(gameStartButton_Panel.sec_3,14);
				gameStartButton_Panel.gameStartButton.setEnabled(false);
			}
			//*********************2초*******************************************
			else if(count==3){layerPane.setLayer(gameStartButton_Panel.sec_2,15);}
			//*********************1초*******************************************
			else if(count==2){layerPane.setLayer(gameStartButton_Panel.sec_1,16);}
			//*********************게임시작****************************************
			else{
				firstButton_Panel.start_BGM.stopPlayer();
				Level1.Level_BGM.startPlay();
				layerPane.setLayer(Level1,17);
				Level1.progressBarTimer.start();
				Level++;
				gameStartTimer.stop();
			}
			count--;
		}
	}

	class RepaintTimerAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			//*********************1단계 클리어*************************************
			if(Level1.clearLevel()&&Levelclear_Count==0){
				Levelclear_Count++;
				Level1.Level_BGM.stopPlayer();
				Level1.progressBarTimer.stop();
				next_Button1.setUp();
				next_Button1.next_BGM.startPlay();
				layerPane.setLayer(next_Button1.panel,18);		
			}
			//*********************1단계 실패***************************************
			if(Level1.progressBar.time==0&&Levelclear_Count==0){
				frame.setVisible(false);
				Level1.Level_BGM.stopPlayer();
				false_Panel = new False_Panel(Level,playerName , Level1.totalScore , 60-Level1.progressBar.scoreTime); 
				false_Panel.setUp();
				repaintTimer.stop();
			}
			//*********************2단계 클리어*************************************
			if(Level2.clearLevel()&&Levelclear_Count==1){
				Level2.progressBarTimer.stop();
				Level2.Level_BGM.stopPlayer();
				Levelclear_Count++;
				next_Button2.setUp();
				next_Button2.next_BGM.startPlay();
				layerPane.setLayer(next_Button2.panel,20);	
			}
			//*************2단계 실패************************************************	
			if(Level3.progressBar.time==0&&Levelclear_Count==1){
				frame.setVisible(false);
				false_Panel = new False_Panel(Level,playerName,Level2.totalScore,(60-Level1.progressBar.scoreTime)-(60-Level2.progressBar.scoreTime)); 
				false_Panel.setUp();
				repaintTimer.stop();
				Level2.Level_BGM.stopPlayer();
				Level2.progressBarTimer.stop();
				Level2.progressBar.time--;
			}
			//*********************3단계 클리어*************************************
			if(Level3.clearLevel()&&Levelclear_Count==2){
				Level3.progressBarTimer.stop();
				Level3.Level_BGM.stopPlayer();
				Levelclear_Count++;
				next_Button3.setUp();
				next_Button3.next_BGM.startPlay();
				layerPane.setLayer(next_Button3.panel,22);	
			}
			//*************3단계 실패************************************************	
			if(Level3.progressBar.time==0&&Levelclear_Count==2){
				frame.setVisible(false);
				false_Panel = new False_Panel(Level,playerName,Level2.totalScore,(60-Level1.progressBar.scoreTime)-(60-Level2.progressBar.scoreTime)-(60-Level3.progressBar.scoreTime)); 
				false_Panel.setUp();
				repaintTimer.stop();
				Level3.Level_BGM.stopPlayer();
				Level3.progressBarTimer.stop();
				Level3.progressBar.time--;
			}
			//*********************4단계 클리어*************************************
			if(Level4.clearLevel()&&Levelclear_Count==3){
				final_Panel.set_newPlayerName(playerName);
				final_Panel.set_newTotalScore(Level4.totalScore);
				final_Panel.set_newTotalTime((60-Level1.progressBar.scoreTime)-(60-Level2.progressBar.scoreTime)-(60-Level3.progressBar.scoreTime)-(60-Level4.progressBar.scoreTime));
				Level4.progressBarTimer.stop();
				repaintTimer.stop();
				Level4.Level_BGM.stopPlayer();
				final_Panel.Final_BGM.startPlay();
				layerPane.remove(Level4);
				layerPane.setLayer(final_Panel,24);	
				Levelclear_Count++;
			}
			//*************4단계 실패************************************************	
			if(Level4.progressBar.time==0&&Levelclear_Count==3){
				frame.setVisible(false);
				false_Panel = new False_Panel(Level,playerName,Level4.totalScore,(60-Level1.progressBar.scoreTime)-(60-Level2.progressBar.scoreTime)-(60-Level3.progressBar.scoreTime)-(60-Level4.progressBar.scoreTime)); 
				false_Panel.setUp();
				repaintTimer.stop();
				Level4.Level_BGM.stopPlayer();
				Level4.progressBarTimer.stop();
				Level4.progressBar.time--;

			}
		}	
	}//repaint

}//class
