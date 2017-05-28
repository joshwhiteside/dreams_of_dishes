package com.me.dreams;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class CurrentGameVar {

	final static float TIME_LIMIT = 7.0f;
	
	static int GameScore = 0;
	static int HighScore = 0;
	static Boolean Alive;
	static GameState state;
	static float Timer;
	static String failReason;
	static int gameOverStage = 0;
	static MadBGType bgType = MadBGType.SB_BG;
	static boolean mashMode = true;
	
	
	static void StartNewGame() {
		state 				= GameState.PLAYING;
		GameScore 			= 0;
		Alive 				= true;
		Timer				= TIME_LIMIT;
	}
	
	static void GameOver() {
		Gdx.input.vibrate(200);
		state				= GameState.SCORES;	
		Alive				= false;
		MeshHelper.drawMadBG = 0;
		gameOverStage		= 0;
		if(GameScore > HighScore){
			SoundSystem.playHighScore();
		} else{
			SoundSystem.playGameOver();
		}		
	}
	
	static void BackToMenu() {
		if(GameScore > HighScore){	
			HighScore = GameScore;
			writeHighScore();
		}
		state				= GameState.STARTMENU;
		failReason			= "";
		GameScore 			= 0;
		Alive				= false;	
	
	}
	
	static void Pause() {
		if(state==GameState.PLAYING){
			state = GameState.PAUSED;
		}
		
	}
	
	static void Resume() {
		state = GameState.PLAYING;
		
	}
	
	static public void writeHighScore(){
		Preferences prefs = Gdx.app.getPreferences( "profile" );
		prefs.putInteger( "highScore", HighScore );
		prefs.flush();
	}
	
	static public void getHighScore(){
		Preferences prefs = Gdx.app.getPreferences( "profile" );
		HighScore = prefs.getInteger("highScore");
		
	}
	
	
}
