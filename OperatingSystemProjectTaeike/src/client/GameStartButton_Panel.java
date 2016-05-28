package client;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import res.SoundPlayer;
import util.PosImageIcon;


public class GameStartButton_Panel{
	
	JButton gameStartButton = new JButton(new ImageIcon("놀이시작하기.jpg"));
	JButton explanation_Button = new JButton(new ImageIcon("놀이설명.jpg"));
	ArrayList<PosImageIcon> imgList = new ArrayList<PosImageIcon>();
	JPanel panel = new JPanel();
	JFrame frame;
	SelectOpponent so,tmp;
	
	int count=2;
	
	public GameStartButton_Panel(JFrame frame,SelectOpponent so){
		this.frame = frame;
		this.so = so;
		this.setup_GUI();
		
	}
	public void setup_GUI(){	
		gameStartButton.setSize(600,850);
		gameStartButton.setLocation(0,0);
		gameStartButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				so.setUpGUI();
				frame.getContentPane().removeAll(); // 등록된 모든 컨테이너 삭제
				frame.getContentPane().add(so.mainPanel); // 다시 등록
                frame.setContentPane(frame.getContentPane()); // 프레임에 설정 (this : Frame )   	
			}
		});
		
		explanation_Button.setSize(600,850);
		explanation_Button.setLocation(600,0);
		explanation_Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
		
				Paint_Thread pt = new Paint_Thread();
				pt.start();
			}
		});
		
		panel.setLayout(null);
		panel.setSize(1200,850);
		panel.setLocation(0,0);
		panel.add(gameStartButton);
		panel.add(explanation_Button);
	}
	
	class Paint_Thread extends Thread{
		Explan_Panel explan_Panel  = new Explan_Panel();
		PosImageIcon explanation_1 = new PosImageIcon("설명1.jpg",0,0,1200,850);
		PosImageIcon explanation_2 = new PosImageIcon("설명2.jpg",0,0,1200,850);
		PosImageIcon explanation_3 = new PosImageIcon("설명3.jpg",0,0,1200,850);
		PosImageIcon explanation_4 = new PosImageIcon("설명4.jpg",0,0,1200,850);
		PosImageIcon explanation_5 = new PosImageIcon("설명5.jpg",0,0,1200,850);
		int count = 1;
		JFrame explan_Frame = new JFrame();
		public Paint_Thread() {
			// TODO Auto-generated constructor stub
			setUp();
		}
		public void setUp(){
			explan_Frame.setSize(1200,880);
			explan_Frame.setLocation(160,30);
			explan_Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			explan_Frame.setLayout(null);
			explan_Panel.setBounds(0,0,1200,850);
			explan_Frame.add(explan_Panel);
			explan_Frame.setTitle("놀이 규칙 설명");
			explan_Frame.setResizable(false);
			explan_Frame.setVisible(true);
		}
		public void run(){
			while(count<7){
				try {
					if(count>=1){explan_Frame.repaint();}
					this.sleep(5000);//몇초단위로 이미지를 넘길건지
				} catch (Exception e2) {
					System.out.println("못재움");
				}
			}
		}
		class Explan_Panel extends JPanel{
			protected void paintComponent(Graphics g) {
				if(count==1)      explanation_1.draw(g);
				else if(count==2) explanation_2.draw(g);
				else if(count==3) explanation_3.draw(g);
				else if(count==4) explanation_4.draw(g);
				else if(count==5) explanation_5.draw(g);
				else if(count==6){
					explan_Frame.setVisible(false);
					explan_Frame=null;
				}
				count++;
			}
		}
	}
}
