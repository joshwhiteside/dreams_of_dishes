package com.me.dreams;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Model;

public class LoadRenderer extends Renderer{
	private SpriteBatch batch;
	private Texture texture;
	private MeshHelper bgMesh;
	private MeshHelper titleMesh;
	private	TextMesh   textMesh;
	private TextMesh   musicMesh;
	
	
	LoadRenderer() {
		loadResources();
	}
	
	void render(float msec) {
		MeshHelper.tic +=msec;
		
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		setViewOrtho();
		bgMesh.drawMesh();
		
		titleMesh.position.set(Common.SCREEN_WIDTH * 0.5f, Common.SCREEN_HEIGHT * 0.5f, 90.0f);
		
		titleMesh.drawMesh();
		

		textMesh.position.set(Common.SCREEN_WIDTH * 0.5f, Common.SCREEN_HEIGHT * 0.1f, 90.0f);
		textMesh.text = "loading...";
		textMesh.drawMesh();
		
		musicMesh.position.set(Common.SCREEN_WIDTH * 0.5f, Common.SCREEN_HEIGHT * 0.9f, 90.0f);
		musicMesh.text = "Music by Jamie Roberts";
		musicMesh.drawMesh();
		
		Gdx.gl20.glDisable(GL20.GL_BLEND);	
	}
	

	Boolean loadResources() {
		//Models
		Dreams_Of_Dishes.assetManager.load("data/bottle.g3db",Model.class);
		Dreams_Of_Dishes.assetManager.load("data/bubble.g3db",Model.class);
		Dreams_Of_Dishes.assetManager.load("data/bowl.g3db",Model.class);
		Dreams_Of_Dishes.assetManager.load("data/clouds.g3db",Model.class);
		Dreams_Of_Dishes.assetManager.load("data/plate.g3db",Model.class);
		Dreams_Of_Dishes.assetManager.load("data/spoon.g3db",Model.class);
		Dreams_Of_Dishes.assetManager.load("data/mountain.g3db",Model.class);
		Dreams_Of_Dishes.assetManager.load("data/cbp.g3db",Model.class);
		Dreams_Of_Dishes.assetManager.load("data/bossdish.g3db",Model.class);
		
		//Sounds
		Dreams_Of_Dishes.assetManager.load("data/newhighscore.wav",Sound.class);
		Dreams_Of_Dishes.assetManager.load("data/shoot.wav",Sound.class);
		Dreams_Of_Dishes.assetManager.load("data/supershoot.wav",Sound.class);
		Dreams_Of_Dishes.assetManager.load("data/jump.wav",Sound.class);
		Dreams_Of_Dishes.assetManager.load("data/gameover.wav",Sound.class);
		Dreams_Of_Dishes.assetManager.load("data/explosion.wav",Sound.class);
		Dreams_Of_Dishes.assetManager.load("data/blip.wav",Sound.class);

		//Textures
		Dreams_Of_Dishes.assetManager.load("data/fractal.png",Texture.class);
		Dreams_Of_Dishes.assetManager.load("data/cbpBG.png",Texture.class);
		Dreams_Of_Dishes.assetManager.load("data/fanBG.png",Texture.class);
		Dreams_Of_Dishes.assetManager.load("data/fractal.png",Texture.class);
		Dreams_Of_Dishes.assetManager.load("data/follow.png",Texture.class);
		Dreams_Of_Dishes.assetManager.load("data/play.png",Texture.class);
		Dreams_Of_Dishes.assetManager.load("data/sound.png",Texture.class);
		Dreams_Of_Dishes.assetManager.load("data/back.png",Texture.class);
		Dreams_Of_Dishes.assetManager.load("data/font.png",Texture.class);
		Dreams_Of_Dishes.assetManager.load("data/menu.png",Texture.class);
		Dreams_Of_Dishes.assetManager.load("data/logo.png",Texture.class);
		Dreams_Of_Dishes.assetManager.load("data/title.png",Texture.class);
		
		Dreams_Of_Dishes.assetManager.finishLoading();
		
		updateGameTime();
		
		Boolean hasLoaded = true;
		
		bgMesh = new MeshHelper();
		
		bgMesh.generateQuad();
		bgMesh.position.set(Common.SCREEN_WIDTH * 0.5f, Common.SCREEN_HEIGHT * 0.5f, 90.0f);
		bgMesh.scale.set(Common.SCREEN_WIDTH, Common.SCREEN_HEIGHT, 1.0f);
		bgMesh.bgShader();
		
		titleMesh = new MeshHelper();
		
		titleMesh.generateQuad();
		titleMesh.scale.set(Common.SCREEN_HEIGHT * 0.75f, Common.SCREEN_HEIGHT * 0.75f, 1.0f);
		titleMesh.setTex("data/logo.png");
		titleMesh.TexturedShader();
		

		textMesh = new TextMesh(false);
		textMesh.setTex("data/font.png");
		textMesh.position.set(Common.SCREEN_WIDTH * 0.5f, Common.SCREEN_HEIGHT * 0.9f, 90.0f);
		textMesh.scale.set(32.0f,32.0f,1.0f);
		textMesh.text = "loading...";
		
		musicMesh = new TextMesh(false);
		musicMesh.setTex("data/font.png");
		musicMesh.position.set(Common.SCREEN_WIDTH * 0.5f, Common.SCREEN_HEIGHT * 0.9f, 90.0f);
		musicMesh.scale.set(32.0f,32.0f,1.0f);
		musicMesh.text = "Music by Jamie Roberts";
		
		return hasLoaded;	
	}
	
	public void dispose() {
		batch.dispose();
		texture.dispose();
	}

		
}
