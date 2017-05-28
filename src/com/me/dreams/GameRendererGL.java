package com.me.dreams;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class GameRendererGL extends Renderer{
	private MeshHelper hudMesh;
	
	private MeshHelper bgMesh;
	
	private TextMesh   textMesh;
	private TextMesh   scoreMesh;
	
	final float PERFECT_PITCH 	= 0.0f;
	final float START_PITCH 	= 180.0f;
	
	float newGameTimer = 0.0f;
	
	PlayingLogic playingLogic;
	
	float yaw, pitch;
	Vector3 cameraPos;
	
	
	GameRendererGL() {
		cameraPos = new Vector3();
		cameraPos.set(-Common.SCREEN_WIDTH * 0.5f,- Common.SCREEN_HEIGHT * 0.5f, -750.0f);
		
		loadResources();
		
		yaw = 0.0f;
		pitch = START_PITCH;
		
	}
	
	void renderBG(float msec) {

		if(MeshHelper.drawMadBG > 0.0f)
		{
			MeshHelper.drawMadBG -= (msec * 4);
		}
		else if(MeshHelper.drawMadBG < 0.0f)
		{
			MeshHelper.drawMadBG = 0f;
		}
		
		setViewOrtho();
		
		//Set to CBP if CBP
		//Set to super if super
		//Set to fanBG
		
		switch(CurrentGameVar.bgType)
		{
		case CBP_BG:
		{
			bgMesh.setTex("data/cbpBG.png");
			break;
		}
		case FAN_BG:
		{
			bgMesh.setTex("data/fanBG.png");
			break;
		}
		case FOLLOW_BG:
		{
			bgMesh.setTex("data/follow.png");
			break;
		}
		case SB_BG:
		{
			bgMesh.setTex("data/fractal.png");
			break;
		}
		default:
			break;
		}

		bgMesh.tex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		bgMesh.drawMesh();
	}
	
	void renderScores(float msec)
	{
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		for(int i = 0; i < playingLogic.NO_SCORES; i++){
			if(playingLogic.scores[i].active){
				float scaleVal = 128.0f;
				scaleVal *= playingLogic.scores[i].timer * 2.0f;
				scaleVal += 50.0f;
				scoreMesh.position.set(playingLogic.scores[i].position);
				scoreMesh.scale.set(scaleVal,scaleVal,1.0f);
				scoreMesh.text = playingLogic.scores[i].text;
				scoreMesh.drawMesh();
			}
		}
		
		Gdx.gl20.glDisable(GL20.GL_BLEND);
	}
	
	void renderHUD(float msec){

		setViewOrtho();
		
		//Draw timer.
		hudMesh.position.set(Common.SCREEN_WIDTH * 0.5f, 5, 0);
		
		hudMesh.position.z = 2.0f;		
		hudMesh.colour.set(0.0f,0.0f,0.0f);
		hudMesh.scale.set(Common.SCREEN_WIDTH, 10, 1);
		hudMesh.drawMesh();
		
		hudMesh.position.z = 1.0f;
		hudMesh.colour.set(1.0f,1.0f,1.0f);
		hudMesh.scale.set(Common.SCREEN_WIDTH * 0.2f * CurrentGameVar.Timer, 10, 1);
		hudMesh.drawMesh();
		
		//Draw hp
		if(playingLogic.bossDish.active)
		{
			hudMesh.position.set(Common.SCREEN_WIDTH * 0.5f, Common.SCREEN_HEIGHT * 0.7f, 0);
		
			hudMesh.position.z = 2.0f;		
			hudMesh.colour.set(0.0f,0.0f,0.0f);
			hudMesh.scale.set(Common.SCREEN_WIDTH * 0.5f, 10, 1);
			hudMesh.drawMesh();
		
			hudMesh.position.z = 1.0f;
			hudMesh.colour.set(1.0f,1.0f,1.0f);
			hudMesh.scale.set(playingLogic.bossDish.hp * (Common.SCREEN_WIDTH / Common.BOSS_DISH_HP) * 0.5f, 10, 1);
			hudMesh.drawMesh();
		}
		
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		float scoreSize = 50.0f;
		scoreSize += scoreSize * playingLogic.upscoreTimer * 2.0f;
		
		textMesh.text = String.valueOf(CurrentGameVar.GameScore);
		textMesh.position.set(Common.SCREEN_WIDTH * 0.5f, Common.SCREEN_HEIGHT * 0.8f, 1.0f);
		textMesh.scale.set(scoreSize,scoreSize,1.0f);
		textMesh.drawMesh();

		textMesh.text = "Time:";
		textMesh.position.set(Common.SCREEN_WIDTH * 0.5f, Common.SCREEN_HEIGHT * 0.05f, 1.0f);
		textMesh.scale.set(24.0f,24.0f,1.0f);
		textMesh.drawMesh();
		
		if(newGameTimer < 0.5f){
			float scaleTemp = 32.0f + (Common.SCREEN_WIDTH  * newGameTimer);
			textMesh.text = "GO!";
			textMesh.position.set(Common.SCREEN_WIDTH * 0.5f, Common.SCREEN_HEIGHT * 0.5f, 1.0f);
			textMesh.scale.set(scaleTemp,scaleTemp,1.0f);
			textMesh.drawMesh();
			
		}
		
		Gdx.gl20.glDisable(GL20.GL_BLEND);
	}
	
	void render(float msec) {
		pitch+=playingLogic.bump;
		MeshHelper.tic += msec;
		if(newGameTimer < 0.5f) newGameTimer += msec;
		
		if(playingLogic.doDrawMadBG()){
			MeshHelper.drawMadBG = 1;
		}
		
		playingLogic.update(msec);
		
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		Gdx.gl.glClearDepthf(1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT|GL20.GL_DEPTH_BUFFER_BIT);
		
		renderBG(msec);
		playingLogic.GameRender(msec);
		renderScores(msec);
		renderHUD(msec);
	}
	
	void setViewPersp(){
		Matrix4 tempView = new Matrix4();
		
		tempView.rotate(new Vector3(1.0f,0.0f,0.0f),-pitch);
		tempView.rotate(new Vector3(0.0f,1.0f,0.0f),-yaw);
		tempView.translate(cameraPos);
		MeshHelper.viewMatrix.idt();
		MeshHelper.viewMatrix = tempView;	
	
		Matrix4 tempProj = new Matrix4();
		tempProj.setToProjection(0, 1000, 45, Common.SCREEN_WIDTH / Common.SCREEN_HEIGHT);
		MeshHelper.projMatrix = tempProj;
	}
	
	Boolean loadResources() {
		Boolean hasLoaded = true;
		
		Gdx.gl20.glEnable(GL20.GL_CULL_FACE);	
	
		MeshHelper.cameraPos.set(cameraPos);
		
		setViewOrtho();
		
		
		//Load and set up meshes :D
		hudMesh = new MeshHelper();
		hudMesh.generateQuad();
		hudMesh.position.set(Common.SCREEN_WIDTH * 0.5f, 5, 0);
		hudMesh.GameHUDShader();
		
		bgMesh = new MeshHelper();
		bgMesh.generateQuad();
		bgMesh.setTex("data/cbpBG.png");
		bgMesh.tex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		bgMesh.position.set(Common.SCREEN_WIDTH * 0.5f, Common.SCREEN_HEIGHT * 0.5f, 90.0f);
		bgMesh.scale.set(Common.SCREEN_WIDTH, Common.SCREEN_HEIGHT, 1.0f);
		bgMesh.bgShader();
		
		textMesh = new TextMesh(false);
		textMesh.setTex("data/font.png");
		textMesh.position.set(Common.SCREEN_WIDTH * 0.5f, Common.SCREEN_HEIGHT * 0.9f, 1.0f);
		textMesh.scale.set(50.0f,50.0f,1.0f);
		textMesh.align = TextAlign.CENTRE;
		textMesh.text = "hello";

		scoreMesh = new TextMesh(true);
		scoreMesh.setTex("data/font.png");
		scoreMesh.position.set(Common.SCREEN_WIDTH * 0.5f, Common.SCREEN_HEIGHT * 0.9f, 1.0f);
		scoreMesh.scale.set(50.0f,50.0f,1.0f);
		scoreMesh.align = TextAlign.CENTRE;
		scoreMesh.text = "hello";
		
		
		//Initialises game logic
		playingLogic = new PlayingLogic();

		return hasLoaded;
		
	}

	public void dispose() {
		
	}

	public void reset() {
		playingLogic.reset();
		updateGameTime();
		pitch = START_PITCH;
		newGameTimer = 0.0f;
	}

		
}
