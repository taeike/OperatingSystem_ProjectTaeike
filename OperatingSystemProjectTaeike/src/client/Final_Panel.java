package client;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.text.ChangedCharSetException;

import res.SoundPlayer;


public class Final_Panel extends JPanel implements ActionListener {

	private JFrame frame;
	public SoundPlayer Final_BGM = new SoundPlayer("마지막애국가.wav");
	private ArrayList<PosImageIcon> imgList = new ArrayList<PosImageIcon>();
	private Timer changePanelTimer;
	private JButton rankView_Button = new JButton(new ImageIcon("마지막순위.jpg"));
	private JButton endpicture = new JButton(new ImageIcon("마치며.jpg"));
	private False_Panel final_rank;
	private String newPlayerName;
	private int newTotalScore;
	private int newTotalTime;
	private int imgCount=0;
	private boolean rankCont = false;
	private boolean	viewCont = false;
	
	public Final_Panel(JFrame frame,String playerName,int totalScore,int totalTime){

		this.setLayout(null);

		this.frame=frame;

		rankView_Button.setSize(600,850);
		rankView_Button.setLocation(0,0);
		final_rank = new False_Panel(2, playerName, totalScore, totalTime);
		rankView_Button.addActionListener(new RankView_Panel());
		this.add(rankView_Button);
		
		endpicture.setSize(600,850);
		endpicture.setLocation(600,0);
		endpicture.addActionListener(this);
		this.add(endpicture);
		
		this.newTotalScore = totalScore;
		this.newTotalTime = totalTime;
		this.newPlayerName = playerName;

		changePanelTimer = new Timer(6000,this);
		imgList.add(new PosImageIcon("마지막화면1.jpg",0,0,1200,850));
		imgList.add(new PosImageIcon("마지막화면2.jpg",0,0,1200,850));
		imgList.add(new PosImageIcon("마지막화면3.jpg",0,0,1200,850));
		imgList.add(new PosImageIcon("마지막화면4.jpg",0,0,1200,850));
		imgList.add(new PosImageIcon("마지막화면5.jpg",0,0,1200,850));
		imgList.add(new PosImageIcon("만든이.jpg",0,0,1200,850));
	}
	protected void paintComponent(Graphics g) {
		if(viewCont==true){
			if(imgCount<6){
			changePanelTimer.start();
			imgList.get(imgCount).draw(g);
			imgCount++;
			}
			else changePanelTimer.stop();
		}
	}

	public void actionPerformed(ActionEvent e) {
		changePanelTimer.start();
		this.remove(rankView_Button);
		this.remove(endpicture);
		viewCont = true;
		repaint();
	}

	class RankView_Panel extends JPanel implements ActionListener{
		PosImageIcon img = new PosImageIcon("순위화면.jpg",0,0,1200,850);
		public void actionPerformed(ActionEvent e) {
			frame.setVisible(false);
			JFrame newFrame = new JFrame();
			ArrayList<Rank_Information> rank_list = new ArrayList<Rank_Information>();
			ArrayList<JLabel> label_list = new ArrayList<JLabel>();
			newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			newFrame.setSize(1200,850);
			newFrame.setLocation(300,70);
			newFrame.setResizable(false);
			this.setLayout(null);	
			this.setSize(1200,850);

			File file = new File("틀린그림찾기순위.txt");	
			String lineString;
			String playerName;

			int totalScore=0;
			int totalTime =0;
			int totalGame =0;
			int rankPrintCount=0;
			int x=0,y=200;

			//***********************파일을 일단 읽음**************************************************
			try{
				Scanner scan = new Scanner(file);
				while(scan.hasNext()){
					lineString = scan.nextLine();
					Scanner scanFile = new Scanner(lineString);
					playerName = scanFile.next();
					totalScore = Integer.parseInt(scanFile.next());
					totalTime  = Integer.parseInt(scanFile.next());
					rank_list.add(new Rank_Information(playerName, totalScore, totalTime));
				}
				scan.close();
			}catch(Exception ex){System.out.println("파일 못읽음");}
			//***********************새로운 리스트를 넣어줌***********************************************
			System.out.println(newPlayerName+" "+newTotalScore+"   "+newTotalTime);
			rank_list.add(new Rank_Information(newPlayerName,newTotalScore,newTotalTime));
			//***********************정렬을 하기 위해****************************************************
			Collections.sort(rank_list,Collections.reverseOrder());

			for(Rank_Information rank:rank_list){
				rankPrintCount++;
				label_list.add(new JLabel(rankPrintCount+"등 이름 :"+rank.playerName+" 점수 :"+rank.totalScore));
				if(rankPrintCount==5) break;
			}
			for(JLabel label:label_list){
				label.setForeground(Color.BLACK);
				label.setFont(new Font("1훈화양연화 R",Font.CENTER_BASELINE,50));
				label.setSize(1200,100);
				label.setLocation(x+20,y+=100);
				this.add(label);
			}
			newFrame.add(this);
			newFrame.setVisible(true);
			//***********************다시 넣어 준다*******************************************************
			try {
				FileWriter  fileWriter = new FileWriter(file,true); 
				fileWriter.write(newPlayerName+" "+Integer.toString(newTotalScore)+" "+Integer.toString(newTotalTime)+"\n");
				fileWriter.flush();		
				fileWriter.close();
			} catch (Exception e2) {e2.printStackTrace();}
		}//action
		protected void paintComponent(Graphics g) {
			img.draw(g);
		}
	}//rank
	public void set_newPlayerName(String newPlayerName){
		this.newPlayerName = newPlayerName;
	}
	public void set_newTotalScore(int newTotalScore){
		this.newTotalScore = newTotalScore;
	}
	public void set_newTotalTime(int newTotalTime){
		this.newTotalTime = newTotalTime;
	}
}
