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
	String frameTitle = "채팅 클라이언트";
	JTextArea incoming;			// 수신된 메시지를 출력하는 곳
    JTextArea outgoing;			// 송신할 메시지를 작성하는 곳
    JList counterParts;			// 현재 로그인한 채팅 상대목록을 나타내는 리스트.
    ObjectInputStream reader;	// 수신용 스트림
    ObjectOutputStream writer;	// 송신용 스트림
    Socket sock;				// 서버 연결용 소켓
    String user;				// 이 클라이언트로 로그인 한 유저의 이름
    JButton logButton;			// 토글이 되는 로그인/로그아웃 버튼
    PosImageIcon selectOpImage = new PosImageIcon("selectOp.jpg", 0, 0, 1200, 850);
    
    public SelectOpponent(JFrame frame){
    	this.frame = frame;
    	this.setUpGUI();
    }
    
 	private void setUpGUI(){
    // 대화 상대 목록. 초기에는 "전체" - ChatMessage.ALL 만 있음
    String[] list = {ChatMessage.ALL};
    counterParts = new JList(list);
    JScrollPane cScroller = new JScrollPane(counterParts);
    cScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    cScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    cScroller.setBounds(1000, 150, 150, 200);
    counterParts.setVisibleRowCount(5);
    counterParts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    counterParts.setFixedCellWidth(100);
    
    // 메시지 전송을 위한 버튼
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
   
    // 클라이언드 프레임 창 조정
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(null);
    frame.getContentPane().add(mainPanel);
    frame.setSize(1200,850);

    // 프레임이 살아 있으므로 여기서 만들은 스레드는 계속 진행 됨
    // 이 프레임 스레드를 종료하면, 이 프레임에서 만든 스레드들은 예외를 발생하게되고
    // 이를 이용해 모든 스레드를 안전하게 종료 시키도록 함
 	}
}
