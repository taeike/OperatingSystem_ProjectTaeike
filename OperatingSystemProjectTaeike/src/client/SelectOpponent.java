package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;


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
    PosImageIcon selectOpImage = new PosImageIcon("selectOp.jpg", 0, 0, 1200, 850);
    
    public SelectOpponent(JFrame frame){
    	this.frame = frame;
    	this.setUpGUI();
    }
    
 	private void setUpGUI(){
    // ��ȭ ��� ���. �ʱ⿡�� "��ü" - ChatMessage.ALL �� ����
    String[] list = {ChatMessage.ALL};
    counterParts = new JList(list);
    JScrollPane cScroller = new JScrollPane(counterParts);
    cScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    cScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    cScroller.setBounds(1000, 150, 150, 200);
    counterParts.setVisibleRowCount(5);
    counterParts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    counterParts.setFixedCellWidth(100);
    
    // �޽��� ������ ���� ��ư
    JButton sendButton = new JButton("Send");
    sendButton.setBounds(1050, 350, 100, 50);

    JPanel mainPanel = new JPanel(){
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
}
