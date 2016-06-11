package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.AncestorEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import util.AnswerWindow;
import util.ChatMessage;
import util.PosImageIcon;



public class SelectOpponent {
	JFrame frame;
	String frameTitle = "ä�� Ŭ���̾�Ʈ";
	JTextArea incoming;			// ���ŵ� �޽����� ����ϴ� ��
    JTextArea outgoing;			// �۽��� �޽����� �ۼ��ϴ� ��
    JList counterParts;			// ���� �α����� ä�� ������� ��Ÿ���� ����Ʈ.
    ObjectInputStream reader;	// ���ſ� ��Ʈ��
    ObjectOutputStream writer;	// �۽ſ� ��Ʈ��
    Socket sock;				// ���� ����� ����
    String user;				// �� Ŭ���̾�Ʈ�� �α��� �� ������ �̸�
    JButton logButton;			// ����� �Ǵ� �α���/�α׾ƿ� ��ư
    PosImageIcon selectOpImage = new PosImageIcon("����.jpg", 0, 0, 1200, 850);
    JPanel mainPanel;
    String opponentName;
    
    public SelectOpponent(String user,JFrame frame,ObjectOutputStream writer){
    	this.user = user;
    	this.frame = frame;
    	this.writer = writer;
    	String[] list = {ChatMessage.ALL};
    	counterParts = new JList(list);
    }
    
 	public void setUpGUI(){
    // ��ȭ ��� ���. �ʱ⿡�� "��ü" - ChatMessage.ALL �� ����
    String[] list = {ChatMessage.ALL};

    JScrollPane cScroller = new JScrollPane(counterParts);
    cScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    cScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    cScroller.setBounds(1000, 150, 150, 200);
    counterParts.setVisibleRowCount(5);
    counterParts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    counterParts.setFixedCellWidth(100);
    counterParts.setOpaque(false);
    counterParts.addListSelectionListener(new ListSelectionListener() {
		
		@Override
		public void valueChanged(ListSelectionEvent e) {
			//List ���� ���õ� �÷��̾��� String�� ������
			opponentName = (String)  counterParts.getSelectedValue();
		}
	});
    // �޽��� ������ ���� ��ư
    JButton sendButton = new JButton();
    sendButton.setBounds(800, 650, 300, 100);
    sendButton.setOpaque(false);
    sendButton.setContentAreaFilled(false);
    sendButton.setBorderPainted(false);
    sendButton.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			//���� ����
			try {
				writer.writeObject(new ChatMessage(ChatMessage.MsgType.SELECTPLAYER, user, opponentName, ""));
				writer.flush();
			} catch(Exception ex) {
				ex.printStackTrace();
			}	
		}
	});
    
    JButton messageSendButton = new JButton();
    messageSendButton.setBounds(100, 650, 300, 100);
    messageSendButton.setOpaque(false);
    messageSendButton.setContentAreaFilled(false);
    messageSendButton.setBorderPainted(false);
    messageSendButton.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			AnswerWindow an = new AnswerWindow(user,writer,opponentName);
		}
	});
    
   mainPanel = new JPanel(){
    	@Override
    	protected void paintComponent(Graphics g) {
    		// TODO Auto-generated method stub
    		selectOpImage.draw(g);
    	}
    };
    mainPanel.setLayout(null);
   
    JPanel upperPanel = new JPanel();
    upperPanel.setLayout(null);
 
    JPanel lowerPanel = new JPanel();
    lowerPanel.setLayout(null);
  
    mainPanel.add(sendButton);
    mainPanel.add(messageSendButton);
 
    mainPanel.add( cScroller);
    mainPanel.setBounds(0, 0, 1200, 850);
   
    // Ŭ���̾�� ������ â ����
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(null);
    frame.getContentPane().add(mainPanel);
    frame.setSize(1200,850);

    // �������� ��� �����Ƿ� ���⼭ ������ ������� ��� ���� ��
    // �� ������ �����带 �����ϸ�, �� �����ӿ��� ���� ��������� ���ܸ� �߻��ϰԵǰ�
    // �̸� �̿��� ��� �����带 �����ϰ� ���� ��Ű���� ��
 	}
 	
 	public void setCounterParts(String[] users){
 		counterParts.setListData(users);
  		frame.repaint();
 	}
 	
 	
}
