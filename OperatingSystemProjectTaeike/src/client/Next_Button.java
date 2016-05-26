package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import res.SoundPlayer;

public class Next_Button extends JButton {
	JPanel panel = new JPanel();
	SoundPlayer next_BGM = new SoundPlayer("다음단계.wav");
	public Next_Button(){
		super(new ImageIcon("다음단계.jpg"));
	}
	public void setUp(){
		panel.setLayout(null);
		panel.setSize(1200,850);
		this.setSize(1200,850);
		this.setLocation(0,0);
		panel.add(this);
	}
}