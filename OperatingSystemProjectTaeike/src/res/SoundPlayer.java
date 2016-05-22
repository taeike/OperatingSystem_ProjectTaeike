package res;

import java.applet.AudioClip;
import javax.swing.JApplet;

public class SoundPlayer {
	
	AudioClip inputSound;
	
	public SoundPlayer(String SoundFileURL){
		try {inputSound = JApplet.newAudioClip(getClass().getResource(SoundFileURL));}
	    catch (Exception e) {System.out.println("파일을 못읽었습니다");}
	}
	
	public void startPlay(){
		try {inputSound.play();} 
		catch (Exception e) {System.out.println("음악 재생을 못했습니다");}
	}
	
	public void stopPlayer(){
		inputSound.stop();
	}
	public void removePlayer(){
		inputSound=null;
	}
}
