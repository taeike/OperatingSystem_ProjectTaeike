package res;

import java.applet.AudioClip;
import javax.swing.JApplet;

public class SoundPlayer {
	
	AudioClip inputSound;
	
	public SoundPlayer(String SoundFileURL){
		try {inputSound = JApplet.newAudioClip(getClass().getResource(SoundFileURL));}
	    catch (Exception e) {System.out.println("������ ���о����ϴ�");}
	}
	
	public void startPlay(){
		try {inputSound.play();} 
		catch (Exception e) {System.out.println("���� ����� ���߽��ϴ�");}
	}
	
	public void stopPlayer(){
		inputSound.stop();
	}
	public void removePlayer(){
		inputSound=null;
	}
}
