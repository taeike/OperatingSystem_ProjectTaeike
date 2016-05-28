package client;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import util.PosImageIcon;

public class ResultPanel extends JPanel{
	
	private PosImageIcon resultImage;
	private JButton exitButton;
	private JButton regameButton;
	private JFrame frame;
	private String user;
	private ObjectOutputStream writer;
	
	public ResultPanel(String result,JFrame frame,String user,ObjectOutputStream writer){
		if(result.equals("win")){
			resultImage = new PosImageIcon("win.jpg", 0, 0, 1200, 850);
			System.out.println("이김");
		}
		else{
			resultImage = new PosImageIcon("lose.jpg", 0, 0, 1200, 850);
			System.out.println("짐");
		}
		this.frame = frame;
		this.user = user;
		this.writer = writer;
		setUpGUI();
	}
	private void setUpGUI(){
		this.repaint();
		this.setLayout(null);
		System.out.println("setup");
		exitButton = new JButton("종료");
		exitButton.setBounds(500, 500, 100, 50);
		exitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		this.add(exitButton);
		
		regameButton = new JButton("재시작");
		regameButton.setBounds(500, 600, 100, 50);
		regameButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SelectOpponent so = new SelectOpponent(user, frame, writer);
				so.setUpGUI();
				frame.getContentPane().removeAll(); // 등록된 모든 컨테이너 삭제
				frame.getContentPane().add(so.mainPanel); // 다시 등록
                frame.setContentPane(frame.getContentPane()); // 프레임에 설정 (this : Frame )   	
			}
		});
		this.add(regameButton);
	}
	@Override
	protected void paintComponent(Graphics g) {
		resultImage.draw(g);
	}
}
