package client;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;


import java.awt.*;
import java.awt.event.*;

public class ChatClient {
	JFrame frame;
	String frameTitle = "ä�� Ŭ���̾�Ʈ";
	JTextArea incoming;			// ���ŵ� �޽����� ����ϴ� ��
	JTextArea outgoing;			// �۽��� �޽����� �ۼ��ϴ� ��
	JList counterParts;			// ���� �α����� ä�� ������� ��Ÿ���� ����Ʈ.
	ObjectInputStream reader;	// ���ſ� ��Ʈ��
	ObjectOutputStream writer;	// �۽ſ� ��Ʈ��
	Socket sock;				// ���� ����� ����
	String user;				// �� Ŭ���̾�Ʈ�� �α��� �� ������ �̸�
	JButton loginButton;			// ����� �Ǵ� �α���/�α׾ƿ� ��ư
	JPanel loginPanel;
	PosImageIcon LoginPanelImage = new PosImageIcon("LoginImage.jpg", 0, 0,1200 , 850);
	JTextField nameText = new JTextField();

	SelectOpponent so;
	GameStartButton_Panel gameStartPanel;

	public static void main(String[] args) {
		ChatClient client = new ChatClient();
		client.setUpGUI();

	}

	private void setUpGUI() {
		setUpNetworking();
		// build GUI
		frame = new JFrame(frameTitle + " : �α����ϼ���");

	

		frame.setLayout(null);
		loginPanel = new JPanel(){
			protected void paintComponent(Graphics arg0) {
				LoginPanelImage.draw(arg0);
			}
		};
		loginPanel.setLayout(null);
		
	
		
		nameText.setBounds(100, 100, 200, 50);
		loginPanel.add(nameText);

		loginButton = new JButton("Login");
		loginButton.setBounds(100,200,100,30);
		loginButton.addActionListener(new LogButtonListener());
		loginPanel.add(loginButton);


		frame.setBounds(100, 100, 1200, 850);
		loginPanel.setBounds(0, 0, 1200, 850);
		frame.add(loginPanel);	   
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		// ��Ʈ��ŷ�� �õ��ϰ�, �������� �޽����� ���� ������ ����
		
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();

	}
	private void setUpNetworking() {  
		try {
			// ck = new cket("220.69.203.11", 5000);		// �������� ��ǻ��
			sock = new Socket("127.0.0.1", 5000);			// ���� ����� ���� ��Ʈ�� 5000�� ���Ű�� ��
			reader = new ObjectInputStream(sock.getInputStream());
			writer = new ObjectOutputStream(sock.getOutputStream());
		} catch(Exception ex) {
			JOptionPane.showMessageDialog(null, "�������ӿ� �����Ͽ����ϴ�. ������ �����մϴ�.");
			ex.printStackTrace();
			frame.dispose();		// ��Ʈ��ũ�� �ʱ� ���� �ȵǸ� Ŭ���̾�Ʈ ���� ����
		}
	} // close setUpNetworking   

	// �α��ΰ� �ƿ��� ����ϴ� ��ư�� ��û��. ó������ Login �̾��ٰ� �ϴ� �α��� �ǰ��� Logout�� ó��
	private class LogButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			processLogin();
		}
		// �α��� ó��
		private void processLogin() {

			try {
				writer.writeObject(new ChatMessage(ChatMessage.MsgType.LOGIN, nameText.getText(), "", ""));
				writer.flush();
				frame.setTitle(frameTitle + " ( �α��� : " +nameText.getText() + ")" );
				user = nameText.getText();
		
			} catch(Exception ex) {
				JOptionPane.showMessageDialog(null, "�α��� �� �������ӿ� ������ �߻��Ͽ����ϴ�.");
				ex.printStackTrace();
			}

		}
		// �α׾ƿ� ó��
		private void processLogout() {
			int choice = JOptionPane.showConfirmDialog(null, "Logout�մϴ�");
			if (choice == JOptionPane.YES_OPTION) {
				try {
					writer.writeObject(new ChatMessage(ChatMessage.MsgType.LOGOUT, user, "", ""));
					writer.flush();
					// ����� ��� ��Ʈ���� ������ �ݰ� ���α׷��� ���� ��
					writer.close(); reader.close(); sock.close();
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(null, "�α׾ƿ� �� �������ӿ� ������ �߻��Ͽ����ϴ�. ���������մϴ�");
					ex.printStackTrace();
				} finally {
					System.exit(100);			// Ŭ���̾�Ʈ ���� ���� 
				}
			}
		}
	}  // close LoginButtonListener inner class

	public class SendButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			String to = (String) counterParts.getSelectedValue();
			if (to == null) {
				JOptionPane.showMessageDialog(null, "�۽��� ����� ������ �� �޽����� ��������");
				return;
			}
			try {
				incoming.append(user + " : " + outgoing.getText() + "\n"); // ���� �޽��� â�� ���̱�
				writer.writeObject(new ChatMessage(ChatMessage.MsgType.CLIENT_MSG, user, to, outgoing.getText()));
				writer.flush();
				outgoing.setText("");
				outgoing.requestFocus();
			} catch(Exception ex) {
				JOptionPane.showMessageDialog(null, "�޽��� ������ ������ �߻��Ͽ����ϴ�.");
				ex.printStackTrace();
			}
		}
	}  // close SendButtonListener inner class

	// �������� ������ �޽����� �޴� ������ �۾��� �����ϴ� Ŭ����
	public class IncomingReader implements Runnable {
		public void run() {
			ChatMessage message;             
			ChatMessage.MsgType type;
			try {
				while (true) {
					message = (ChatMessage) reader.readObject();     	 // �����α� ������ �޽��� ���                   
					type = message.getType();
					if (type == ChatMessage.MsgType.LOGIN_FAILURE) {	 // �α����� ������ �����
						JOptionPane.showMessageDialog(null, "Login�� �����Ͽ����ϴ�. �ٽ� �α����ϼ���");
						frame.setTitle(frameTitle + " : �α��� �ϼ���");
						loginButton.setText("Login");
					} else if (type == ChatMessage.MsgType.SERVER_MSG) { // �޽����� �޾Ҵٸ� ������
						if (message.getSender().equals(user)) continue;  // ���� ���� ������ ���� �ʿ� ����
						incoming.append(message.getSender() + " : " + message.getContents() + "\n");
					} 
					if (type == ChatMessage.MsgType.LOGIN_LIST) {
						// ���� ����Ʈ�� ���� �ؼ� counterParts ����Ʈ�� �־� ��.
						// ����  ���� (""�� ����� ���� �� ����Ʈ �� �տ� ���� ��)
						String[] users = message.getContents().split("/");
						for (int i=0; i<users.length; i++) {
							if (user.equals(users[i])) users[i] = "";
						}
						users = sortUsers(users);		// ���� ����� ���� �� �� �ֵ��� �����ؼ� ����
						users[0] =  ChatMessage.ALL;	// ����Ʈ �� �տ� "��ü"�� ������ ��
						gameStartPanel.so.setCounterParts( users );
					}
					else if (type == ChatMessage.MsgType.NO_ACT){
						// �ƹ� �׼��� �ʿ���� �޽���. �׳� ��ŵ		 
					}
					else if(type == ChatMessage.MsgType.PASSLOGIN){
						System.out.println("�α����� �ϰ� ���� ȭ������ �Ѿ");            		
						so = new SelectOpponent(user,frame,writer);
						gameStartPanel = new GameStartButton_Panel(frame,so);
						frame.getContentPane().removeAll(); // ��ϵ� ��� �����̳� ����
						frame.getContentPane().add(gameStartPanel.panel); // �ٽ� ���
						frame.setContentPane(frame.getContentPane()); // �����ӿ� ���� (this : Frame )
					}
					else if(type == ChatMessage.MsgType.SELECTEDPLAYER){
						AnswerWindow selectFrame = new AnswerWindow(message.getSender(),message.getReceiver(),writer);
					}
					else if(type == ChatMessage.MsgType.REJECTED){
						AnswerWindow selectFrame = new AnswerWindow(1);
					}
					else {
						// ��ü�� Ȯ�ε��� �ʴ� �̻��� �޽���
						throw new Exception("�������� �� �� ���� �޽��� ��������");
					}
				} // close while
			} catch(Exception ex) {
				ex.printStackTrace();
				System.out.println("Ŭ���̾�Ʈ ������ ����");		// �������� ����� ��� �̸� ���� ������ ����
			}
		} // close run

		// �־��� String �迭�� ������ ���ο� �迭 ����
		private String [] sortUsers(String [] users) {
			String [] outList = new String[users.length];
			ArrayList<String> list = new ArrayList<String>();
			for (String s : users) {
				list.add(s);
			}
			Collections.sort(list);				// Collections.sort�� ����� �ѹ濡 ����
			for (int i=0; i<users.length; i++) {
				outList[i] = list.get(i);
			}
			return outList;
		}
	} // close inner class     
}
