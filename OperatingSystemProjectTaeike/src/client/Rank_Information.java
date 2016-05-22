package client;

public class Rank_Information implements Comparable<Rank_Information>{
	
	String playerName;
	int totalScore=0;
	int totalTime =0;
	int totalGame =0;
	public Rank_Information(String playerName,int totalScore,int totaltime){
		this.playerName = playerName;
		this.totalScore = totalScore;
		this.totalTime = totaltime;
	}
	public int compareTo(Rank_Information s) {
		 if(this.totalScore != s.totalGame) return this.totalScore - s.totalScore;
		 else return s.totalTime-this.totalTime;
	}	
	public String toString(){
		return this.playerName+" "+this.totalScore+" "+this.totalTime;
	}
}
