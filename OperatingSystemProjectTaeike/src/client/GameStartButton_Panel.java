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


public class GameStartButton_Panel{

	Sec_1 sec_1 = new Sec_1();
	Sec_2 sec_2 = new Sec_2();
	Sec_3 sec_3 = new Sec_3();
	
	JButton gameStartButton = new JButton(new ImageIcon("놀이시작하기.jpg"));
	JButton explanation_Button = new JButton(new ImageIcon("놀이설명.jpg"));
	ArrayList<PosImageIcon> imgList = new ArrayList<PosImageIcon>();
	JPanel panel = new JPanel();
	JFrame frame;
	int count=2;
	
	public GameStartButton_Panel(JFrame frame){
		this.frame = frame;
	}
	public void setup_GUI(){
		imgList.add(new PosImageIcon("1초후게임시작.jpg", 0,0,1200,850));
		imgList.add(new PosImageIcon("2초후게임시작.jpg", 0,0,1200,850));
		imgList.add(new PosImageIcon("3초후게임시작.jpg", 0,0,1200,850));	
		
		gameStartButton.setSize(600,850);
		gameStartButton.setLocation(0,0);
		
		explanation_Button.setSize(600,850);
		explanation_Button.setLocation(600,0);
		
		panel.setLayout(null);
		panel.setSize(1200,850);
		panel.setLocation(0,0);
		panel.add(gameStartButton);
		panel.add(explanation_Button);
	}

	class Sec_1 extends JPanel {
		protected void paintComponent(Graphics g) {
			imgList.get(0).draw(g);
		}
	}
	class Sec_2 extends JPanel {
		protected void paintComponent(Graphics g) {
			imgList.get(1).draw(g);
		}
	}
	class Sec_3 extends JPanel {
		protected void paintComponent(Graphics g) {
			imgList.get(2).draw(g);
		}
	}
}
