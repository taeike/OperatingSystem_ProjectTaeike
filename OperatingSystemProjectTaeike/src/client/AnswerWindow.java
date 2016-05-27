package client;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class AnswerWindow extends JFrame{
	
	private JPanel panel;
	private JButton okButton;
	private JButton cancelButton;
	private String op;
	private ObjectOutputStream writer;
	private String user;
	PosImageIcon image = new PosImageIcon("S.jpg", 0, 0, 300, 200);
	PosImageIcon rejectImage = new PosImageIcon("", 0, 0, 300, 200);
	JFrame frame;
	public AnswerWindow(String op,String user,ObjectOutputStream writer){
		this.user = user;
		this.writer = writer;
		this.op = op;
		System.out.println(user +"@@@"+op);
		requestSetUpGUI();
	}
	public AnswerWindow(int n){
		this.user = user;
		this.writer = writer;
		this.op = op;
		rejectSetUPGUI();
	}
	public void rejectSetUPGUI(){
		frame = this;
		this.setBounds(0, 0, 300, 200);
		panel = new JPanel(){
			@Override
			protected void paintComponent(Graphics g) {
				// TODO Auto-generated method stub
				super.paintComponent(g);
				image.draw(g);
			}
		};
		panel.setBounds(0, 0, 300, 200);
		panel.setLayout(null);
		

		okButton = new JButton("확인");
		okButton.setBounds(100, 170, 50, 30);
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				frame = null;
			}
		});
		
		panel.add(okButton);
		
		this.setUndecorated(true);
		this.add(panel);
		this.setVisible(true);
	}
	public void requestSetUpGUI(){
		frame = this;
		this.setBounds(0, 0, 300, 200);
		panel = new JPanel(){
			@Override
			protected void paintComponent(Graphics g) {
				// TODO Auto-generated method stub
				super.paintComponent(g);
				image.draw(g);
			}
		};
		panel.setBounds(0, 0, 300, 200);
		panel.setLayout(null);
		
		
		okButton = new JButton("수락");
		okButton.setBounds(100, 170, 50, 30);
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					writer.writeObject(new ChatMessage(ChatMessage.MsgType.ACCEPT, user,op, ""));
					writer.flush();
					frame.setVisible(false);
					frame = null;
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(null, "로그인 중 서버접속에 문제가 발생하였습니다.");
					ex.printStackTrace();
				}
				
			}
		});
		cancelButton = new JButton("거절");
		cancelButton.setBounds(150,170,50,30);
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					writer.writeObject(new ChatMessage(ChatMessage.MsgType.REJECT,user,op, ""));
					writer.flush();
					frame.setVisible(false);
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(null, "로그인 중 서버접속에 문제가 발생하였습니다.");
					ex.printStackTrace();
				}
			}
		});
		panel.add(okButton);
		panel.add(cancelButton);
		
		this.setUndecorated(true);
		this.add(panel);
		this.setVisible(true);
	}	
}
