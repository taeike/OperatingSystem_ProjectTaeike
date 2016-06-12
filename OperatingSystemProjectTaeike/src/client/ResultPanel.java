package client;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import util.ChatMessage;
import util.PosImageIcon;

public class ResultPanel extends JPanel{
	
	private PosImageIcon resultImage;
	private JButton exitButton;
	private JButton regameButton;
	private JFrame frame;
	private String user;
	private ObjectOutputStream writer;
	private SelectOpponent so;
	private int opScore;
	private int myScore;
	public ResultPanel(String result,JFrame frame,String user,ObjectOutputStream writer,SelectOpponent so,int opScore,int myScore){
		if(result.equals("win")){
			resultImage = new PosImageIcon("승리.jpg", 0, 0, 1200, 850);
			System.out.println("이김");
		}
		else{
			resultImage = new PosImageIcon("패배.jpg", 0, 0, 1200, 850);
			System.out.println("짐");
		}
		this.so = so;
		this.frame = frame;
		this.user = user;
		this.writer = writer;
		this.opScore = opScore;
		this.myScore = myScore;
		setUpGUI();
	}
	private void setUpGUI(){
		this.repaint();
		this.setLayout(null);
		exitButton = new JButton();
		exitButton.setBounds(150, 350, 200, 100);
		exitButton.setOpaque(false);
		exitButton.setContentAreaFilled(false);
		exitButton.setBorderPainted(false);
		exitButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		this.add(exitButton);
		
		regameButton = new JButton();
		regameButton.setBounds(850, 350, 300, 100);
		regameButton.setOpaque(false);
		regameButton.setContentAreaFilled(false);
		regameButton.setBorderPainted(false);
		regameButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//SelectOpponent so = new SelectOpponent(user, frame, writer);
				//so.setUpGUI();
				so.setUpGUI();
				frame.getContentPane().removeAll(); // 등록된 모든 컨테이너 삭제
				frame.getContentPane().add(so.mainPanel); // 다시 등록
                frame.setContentPane(frame.getContentPane()); // 프레임에 설정 (this : Frame )   	
                
                try {
    				writer.writeObject(new ChatMessage(ChatMessage.MsgType.UPDATELIST,user,"",""));
    				writer.flush();	
    			} catch(Exception ex) {
    				JOptionPane.showMessageDialog(null, "로그인 중 서버접속에 문제가 발생하였습니다.");
    				ex.printStackTrace();
    			}
			}
		});
		this.add(regameButton);
		
		JLabel label = new JLabel();
		label.setBounds(550, 100, 300, 50);
		label.setText("내점수: "+myScore + " 상대점수: "+opScore);
		label.setOpaque(false);
		this.add(label);
	}
	@Override
	protected void paintComponent(Graphics g) {
		resultImage.draw(g);
	}
}
