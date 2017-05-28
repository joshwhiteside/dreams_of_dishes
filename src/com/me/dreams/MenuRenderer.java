package com.me.dreams;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class MenuRenderer extends Renderer{
	private SpriteBatch batch;
	private Texture texture;
	private MeshHelper bgMesh;
	private MeshHelper titleMesh;
	private MeshHelper startMesh;
	private MeshHelper muteMesh;
	private	TextMesh   textMesh;
	
	
	MenuRenderer() {
		loadResources();
		if(!SoundSystem.mute)
		{
			SoundSystem.playMusic();
		}
	}
	
	void render(float msec) {
		checkInput(msec);
		
		MeshHelper.tic +=msec;
		if(MeshHelper.tic > (2.0f * 3.142f)) MeshHelper.tic = 0.0f;
		
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		setViewOrtho();;
		bgMesh.drawMesh();
		
		titleMesh.position.set(Common.SCREEN_WIDTH * 0.5f, Common.SCREEN_HEIGHT * 0.65f + MathUtils.sin(MeshHelper.tic)* 50, 90.0f);

		titleMesh.drawMesh();
		

		float scaleSize = 0.125f +( MathUtils.sin(MeshHelper.tic) * 0.03125f);
		

		startMesh.scale.set(Common.SCREEN_WIDTH * scaleSize,Common.SCREEN_WIDTH * scaleSize, 1.0f);
		startMesh.drawMesh();
		
		if(SoundSystem.mute) muteMesh.colour.set(1.0f,1.0f,1.0f);
		else				 muteMesh.colour.set(1.0f,1.0f,0.0f);

		
		muteMesh.scale.set(Common.SCREEN_WIDTH * scaleSize,Common.SCREEN_WIDTH * scaleSize, 1.0f);
		muteMesh.drawMesh();
		
		textMesh.position.set(Common.SCREEN_WIDTH * 0.5f, Common.SCREEN_HEIGHT * 0.25f, 90.0f);
		//CHANGE FOR ANDROID
		//textMesh.text = "Press Z to jump!";
		textMesh.text = "Tap left to jump!";
		textMesh.drawMesh();

		textMesh.position.set(Common.SCREEN_WIDTH * 0.5f, Common.SCREEN_HEIGHT * 0.15f, 90.0f);
		//CHANGE FOR ANDROID

		textMesh.text = "Tap right to shoot!";
		//textMesh.text = "Press X to shoot!";
		textMesh.drawMesh();
		
		textMesh.position.set(Common.SCREEN_WIDTH * 0.5f, Common.SCREEN_HEIGHT * 0.05f, 90.0f);
		textMesh.text = "HIGH SCORE: " + CurrentGameVar.HighScore;
		textMesh.drawMesh();
		
		
		Gdx.gl20.glDisable(GL20.GL_BLEND);
		
	}
	

	void checkInput(float mesc){
		
		
	}
	
	Boolean loadResources() {
		updateGameTime();
		
		Boolean hasLoaded = true;
		
		bgMesh = new MeshHelper();
		
		bgMesh.generateQuad();
		bgMesh.position.set(Common.SCREEN_WIDTH * 0.5f, Common.SCREEN_HEIGHT * 0.5f, 90.0f);
		bgMesh.scale.set(Common.SCREEN_WIDTH, Common.SCREEN_HEIGHT, 1.0f);
		bgMesh.bgShader();
		
		titleMesh = new MeshHelper();
		
		titleMesh.generateQuad();
		titleMesh.position.set(Common.SCREEN_WIDTH * 0.5f, Common.SCREEN_HEIGHT * 0.25f, 90.0f);
		titleMesh.scale.set(Common.SCREEN_WIDTH * 0.5f, Common.SCREEN_HEIGHT * 0.5f, 1.0f);
		titleMesh.setTex("data/title.png");
		titleMesh.TexturedShader();
		

		startMesh = new MeshHelper();
		
		startMesh.generateQuad();
		startMesh.position.set(Common.SCREEN_WIDTH * 0.75f, Common.SCREEN_HEIGHT * 0.5f, 90.0f);
		startMesh.scale.set(Common.SCREEN_WIDTH * 0.125f, Common.SCREEN_WIDTH * 0.125f, 1.0f);
		startMesh.colour.set(0.0f,1.0f,0.0f);
		startMesh.setTex("data/play.png");
		startMesh.TexturedShader();
		

		muteMesh = new MeshHelper();
		
		muteMesh.generateQuad();
		muteMesh.position.set(Common.SCREEN_WIDTH * 0.25f, Common.SCREEN_HEIGHT * 0.5f, 90.0f);
		muteMesh.scale.set(Common.SCREEN_WIDTH * 0.125f,Common.SCREEN_WIDTH * 0.125f, 1.0f);
		muteMesh.colour.set(1.0f,1.0f,1.0f);
		muteMesh.setTex("data/sound.png");
		muteMesh.TexturedShader();
		
		

		textMesh = new TextMesh(false);
		textMesh.setTex("data/font.png");
		textMesh.position.set(Common.SCREEN_WIDTH * 0.5f, Common.SCREEN_HEIGHT * 0.9f, 90.0f);
		textMesh.scale.set(32.0f,32.0f,1.0f);
		textMesh.text = "hello";
		
		return hasLoaded;
		
		
	}
	
	

	public void dispose() {
		batch.dispose();
		texture.dispose();
	}

		
}
