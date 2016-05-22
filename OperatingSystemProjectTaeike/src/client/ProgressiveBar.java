package client;

import java.awt.*;
import java.applet.*;

public class ProgressiveBar extends Canvas {		
	public int time=0, maxTime=0,scoreTime=0;								// maxTime은 내가 총얼마까지 갈것인가 하는 시간, time은 현재시간
	int down=0;
	public ProgressiveBar(int maxTime) {			// 구성자로 maxTime을 가져옴
		this.time = maxTime+3;
		this.maxTime = maxTime+3;
		setForeground(new Color(255,255,200));		// RGB값을 이용한 색깔 설정
		setBackground(Color.BLACK);		
		setSize(maxTime * 3, 30);			    	// progressbar의 사이즈 설정
	}
	int getTime() {									// 시간을 가져옴
		return (time);
	}
	void setTime(int time) {						// time만큼 시간을 설정
		this.time = time;
		repaint();
	}
	int getMaxTime() {								// 총 얼마까지 갈것인가 하는 시간을 가져옴
		return (maxTime);
	}
	void setMAxTime(int maxTime) {					// 총 얼마까지 걸리는 시간을 설정
		this.maxTime = maxTime;
		repaint();
	}
	public void repaint() {							// 리페인트
		update(getGraphics());
	}
	public void update(Graphics g) {
		paint(g);
	}
	public void paint(Graphics g) {					// 실질적으로 프로그래스바를 그려줌
	
		//System.out.println(time);
		down += 600/maxTime;
		g.fillRect(0, 0, down, 30);
		time--;
		scoreTime++;
	}
}
