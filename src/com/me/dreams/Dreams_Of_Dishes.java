package com.me.dreams;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;


public class Dreams_Of_Dishes implements ApplicationListener {
	
	static GameState state;
	
	static AssetManager assetManager;
	
	public PlayingLogic playing;
	public GameRendererGL playingRenderer;
	public PauseRenderer pauseRenderer;
	public MenuRenderer menuRenderer;
	public LoadRenderer loadRenderer;
	public GameOverRenderer gameoverRenderer;
	
	Vector3 touchPos;
	
	int frame = 0;
	
	@Override
	public void create() {
		assetManager = new AssetManager();
		
		CurrentGameVar.getHighScore();
		CurrentGameVar.state = GameState.STARTMENU;
		Common.setMessages();
		SoundSystem.getMute();
		
		

		//Sets up view matrices
		MeshHelper.tic		  = 0.0f;
		MeshHelper.drawMadBG = 0;
		MeshHelper.viewMatrix = new Matrix4();
		MeshHelper.projMatrix = new Matrix4();
		
		MeshHelper.lightColour = new Vector3(1.0f,1.0f,0.8f);
		MeshHelper.lightRadius = 5000.0f;
		MeshHelper.lightPos	   = new Vector3();
		MeshHelper.lightPos.set(Common.SCREEN_WIDTH,Common.SCREEN_HEIGHT * 0.5f,500.0f);
		MeshHelper.cameraPos   = new Vector3();
		
		
		MeshHelper.viewMatrix.idt();
		
		
		loadRenderer = new LoadRenderer();
		loadRenderer.render(0.0f);
	}

	@Override
	public void dispose() {
		playingRenderer.dispose();
	}

	@Override
	public void render() {		
		float msec = Gdx.graphics.getDeltaTime();
		
		if(frame<5){
			loadRenderer.render(msec);
			switch(frame) 
			{		
				case 1:
				{
					gameoverRenderer = new GameOverRenderer();
					break;
				}
				case 2:
				{
					SoundSystem.loadResources();
					break;
				}				
				case 3:
				{
					menuRenderer = new MenuRenderer();
					break;
				}
				case 4:
				{
					playingRenderer = new GameRendererGL();
					break;
				}
			}
			
			frame++;
			
		}
		
		else
		{
		if(CurrentGameVar.state==GameState.STARTMENU){
			if(Gdx.input.justTouched()){
				Vector3 mutePos = new Vector3(Gdx.input.getX() - Common.TOUCH_WIDTH * 0.25f, Gdx.input.getY() - Common.TOUCH_HEIGHT * 0.5f, 0.0f);
				Vector3 startPos = new Vector3(Gdx.input.getX() - Common.TOUCH_WIDTH * 0.75f, Gdx.input.getY() - Common.TOUCH_HEIGHT * 0.5f, 0.0f);
				if(mutePos.len() < Common.TOUCH_WIDTH * 0.1f) SoundSystem.toggleMute();
				if(startPos.len() < Common.TOUCH_WIDTH * 0.1f){
					playingRenderer.reset();
					CurrentGameVar.StartNewGame();
				}
			}
		}
		
		if(CurrentGameVar.state==GameState.SCORES && gameoverRenderer.timer > 2.0f){
			if(Gdx.input.justTouched()){
				
				Vector3 toPos = new Vector3(Gdx.input.getX() - Common.TOUCH_WIDTH * 0.75f, Gdx.input.getY() - Common.TOUCH_HEIGHT * 0.75f, 0.0f);

				if(toPos.len() < Common.TOUCH_HEIGHT * 0.1f)CurrentGameVar.BackToMenu();
				

				toPos = new Vector3(Gdx.input.getX() - Common.TOUCH_WIDTH * 0.25f, Gdx.input.getY() - Common.TOUCH_HEIGHT * 0.75f, 0.0f);

				if(toPos.len() < Common.TOUCH_HEIGHT * 0.1f)
				{
					CurrentGameVar.BackToMenu();
					playingRenderer.reset();
					CurrentGameVar.StartNewGame();
				}
			}
		}
	
		if(Gdx.input.isKeyPressed(Keys.M)){
			SoundSystem.toggleMute();
		}
		
		switch(CurrentGameVar.state) {
			case STARTMENU:
			{
				menuRenderer.render(msec);
				break;
			}
			case PLAYING:
			{
				playingRenderer.render(msec);
				break;
			}
			case SCORES:
			{
				gameoverRenderer.render(msec);
				break;
			}
		case PAUSED:
		{
			System.out.println("Shouldn't be here...");
			break;
		}
		default:
			break;
		}
		
		}
	}

	@Override
	public void resize(int width, int height) {
		Common.TOUCH_HEIGHT = height;
		Common.TOUCH_WIDTH 	= width;
		
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		
	}
}
