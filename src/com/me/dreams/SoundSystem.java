package com.me.dreams;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundSystem {

	static Sound jump;
	static Sound shoot;
	static Sound explosion;
	static Sound superShoot;
	static Sound blip;
	static Sound newHighScore;
	static Sound gameOver;
	static Music music;
	
	static Boolean mute;
	
	static void loadResources(){

		jump 			= Gdx.audio.newSound(Gdx.files.internal("data/jump.wav"));
		shoot 			= Gdx.audio.newSound(Gdx.files.internal("data/shoot.wav"));
		superShoot 		= Gdx.audio.newSound(Gdx.files.internal("data/supershoot.wav"));
		explosion 		= Gdx.audio.newSound(Gdx.files.internal("data/explosion.wav"));
		blip			= Gdx.audio.newSound(Gdx.files.internal("data/blip.wav"));
		newHighScore	= Gdx.audio.newSound(Gdx.files.internal("data/newhighscore.wav"));
		gameOver		= Gdx.audio.newSound(Gdx.files.internal("data/gameover.wav"));
		
		music			= Gdx.audio.newMusic(Gdx.files.internal("data/thunder.mp3"));
		music.setLooping(true);
		
		mute    = false;
	}
	
	static void playJump() {
		if(!mute)
		jump.play();
		
	}
	
	static void playShoot() {
		if(!mute)
		shoot.play();
		
	}
	
	static void playGameOver() {
		if(!mute)
		gameOver.play();
		
	}

	static void playSuperShoot() {
		if(!mute)
		superShoot.play();
		
	}
	static void playExplosion() {
		if(!mute)
		explosion.play();
		
	}

	static void playBlip() {
		if(!mute)
		blip.play();
		
	}

	static void playHighScore() {
		if(!mute)
		newHighScore.play();
		
	}
	
	static public void toggleMute(){
		mute = !mute;
		if(mute)
		{
			music.stop();
		}
		else
		{
			music.play();
		}
		writeMute();
	}
	
	static public void playMusic()
	{
		music.play();
	}
	
	static public void writeMute(){
		Preferences prefs = Gdx.app.getPreferences( "profile" );
		prefs.putBoolean( "mute", mute );
		prefs.flush();
	}
	
	static public void getMute(){
		Preferences prefs = Gdx.app.getPreferences( "profile" );
		mute = prefs.getBoolean("mute");
		
	}
	
}
