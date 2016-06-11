package util;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AnswerWindow extends JFrame{

	private JPanel panel;
	private JButton okButton;
	private JButton cancelButton;
	private String op;
	private ObjectOutputStream writer;
	private String user;
	PosImageIcon image ;
	PosImageIcon rejectImage = new PosImageIcon("", 0, 0, 300, 200);
	JFrame frame;
	public AnswerWindow(String op,String user,ObjectOutputStream writer){
		image = new PosImageIcon("싸움요청.jpg", 0, 0, 300, 200);
		this.user = user;
		this.writer = writer;
		this.op = op;
		System.out.println(user +"@@@"+op);
		requestSetUpGUI();
	}
	public AnswerWindow(String user,ObjectOutputStream writer,String op){
		image = new PosImageIcon("보내기.jpg", 0, 0, 300, 200);
		textSetUp();
		this.op = op;
		this.writer = writer;
		this.user = user;
	}
	public AnswerWindow(int n){
		this.user = user;
		this.writer = writer;
		this.op = op;
		rejectSetUPGUI();
	}
	public AnswerWindow(ChatMessage data,ObjectOutputStream writer){
		image = new PosImageIcon("메시지받음.jpg", 0, 0, 300, 200);
		this.op = data.getSender();
		System.out.println(data.getContents());
		InMessageSetUpGUI(data.getContents(),data.getSender(),data.getReceiver(),writer);
	}
	public void textSetUp(){
		frame =this;
		this.setBounds(600,400 , 300, 200);
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

		JTextField text = new JTextField();
		text.setBounds(20,20,260, 120);
		panel.add(text);

		okButton = new JButton();
		okButton.setBounds(150, 150, 100, 50);
		okButton.setOpaque(false);
		okButton.setContentAreaFilled(false);
		okButton.setBorderPainted(false);
		okButton.setBounds(80, 160, 70, 40);
		okButton.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				frame = null;
				if (op == null) {
					JOptionPane.showMessageDialog(null, "송신할 대상을 선택한 후 메시지를 보내세요");
					return;
				}
				try {
					writer.writeObject(new ChatMessage(ChatMessage.MsgType.CLIENT_MSG, user, op, text.getText()));
					writer.flush();
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(null, "메시지 전송중 문제가 발생하였습니다.");
					ex.printStackTrace();
				}
			}
		});

		panel.add(okButton);

		this.setUndecorated(true);
		this.add(panel);
		this.setVisible(true);
	}
	public void InMessageSetUpGUI(String n,String sender, String receiver,ObjectOutputStream writer){
		frame = this;
		this.setBounds(000,000 , 300, 250);
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

		JLabel label = new JLabel(n);
		label.setOpaque(false);
		label.setBounds(100, 100, 100, 100);
		panel.add(label);

		okButton = new JButton();
		okButton.setBounds(200, 0, 100, 50);
		okButton.setOpaque(false);
		okButton.setContentAreaFilled(false);
		okButton.setBorderPainted(false);
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				frame = null;
				AnswerWindow ans = new AnswerWindow(receiver,writer,sender);
			}
		}); 
		panel.add(okButton);
		this.setTitle(sender+"님이 메시지를 보냈습니다.");
		this.add(panel);
		this.setVisible(true);
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


		okButton = new JButton();
		okButton.setOpaque(false);
		okButton.setContentAreaFilled(false);
		okButton.setBorderPainted(false);
		okButton.setBounds(80, 160, 70, 40);
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
		cancelButton = new JButton();
		cancelButton.setBounds(180,160,70,40);
		cancelButton.setOpaque(false);
		cancelButton.setContentAreaFilled(false);
		cancelButton.setBorderPainted(false);
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
