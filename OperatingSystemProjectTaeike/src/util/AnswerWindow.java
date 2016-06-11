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
	public AnswerWindow(String user,ObjectOutputStream writer,String op){
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
		
		okButton = new JButton("Ȯ��");
		okButton.setBounds(145, 170, 50, 30);
		okButton.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				frame = null;
				if (op == null) {
					JOptionPane.showMessageDialog(null, "�۽��� ����� ������ �� �޽����� ��������");
					return;
				}
				try {
					writer.writeObject(new ChatMessage(ChatMessage.MsgType.CLIENT_MSG, user, op, text.getText()));
					writer.flush();
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(null, "�޽��� ������ ������ �߻��Ͽ����ϴ�.");
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
		
		okButton = new JButton("����");
		okButton.setBounds(100, 170, 50, 30);
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				frame = null;
				AnswerWindow ans = new AnswerWindow(receiver,writer,sender);
			}
		}); 
		panel.add(okButton);
		this.setTitle(sender+"���� �޽����� ���½��ϴ�.");
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
		

		okButton = new JButton("Ȯ��");
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
		
		
		okButton = new JButton("����");
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
					JOptionPane.showMessageDialog(null, "�α��� �� �������ӿ� ������ �߻��Ͽ����ϴ�.");
					ex.printStackTrace();
				}
				
			}
		});
		cancelButton = new JButton("����");
		cancelButton.setBounds(150,170,50,30);
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					writer.writeObject(new ChatMessage(ChatMessage.MsgType.REJECT,user,op, ""));
					writer.flush();
					frame.setVisible(false);
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(null, "�α��� �� �������ӿ� ������ �߻��Ͽ����ϴ�.");
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
