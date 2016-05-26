package client;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import res.SoundPlayer;


public class FirstButton_Panel extends JPanel{
	JButton FirstButton = new JButton(new ImageIcon("시작화면.jpg"));
	SoundPlayer start_BGM = new SoundPlayer("시작음악.wav");

	public void setup_GUI(){
		this.setLayout(null);
		this.setSize(1200,850);
		FirstButton.setSize(1200,850);
		FirstButton.setLocation(0,0);
		this.add(FirstButton);
		start_BGM.startPlay();
	}
}
