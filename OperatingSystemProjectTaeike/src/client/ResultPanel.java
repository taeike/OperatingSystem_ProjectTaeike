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
			System.out.println("�̱�");
		}
		else{
			resultImage = new PosImageIcon("lose.jpg", 0, 0, 1200, 850);
			System.out.println("��");
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
		exitButton = new JButton("����");
		exitButton.setBounds(500, 500, 100, 50);
		exitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		this.add(exitButton);
		
		regameButton = new JButton("�����");
		regameButton.setBounds(500, 600, 100, 50);
		regameButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				SelectOpponent so = new SelectOpponent(user, frame, writer);
				so.setUpGUI();
				frame.getContentPane().removeAll(); // ��ϵ� ��� �����̳� ����
				frame.getContentPane().add(so.mainPanel); // �ٽ� ���
                frame.setContentPane(frame.getContentPane()); // �����ӿ� ���� (this : Frame )   	
			}
		});
		this.add(regameButton);
	}
	@Override
	protected void paintComponent(Graphics g) {
		resultImage.draw(g);
	}
}
