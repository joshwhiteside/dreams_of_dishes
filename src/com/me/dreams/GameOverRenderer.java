package com.me.dreams;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class GameOverRenderer extends Renderer{
	private SpriteBatch batch;
	private Texture texture;
	private MeshHelper bgMesh;
	private MeshHelper menuMesh;
	private MeshHelper replayMesh;
	private	TextMesh   textMesh;
	
	public float timer;
	
	public final static float SCREEN_SWITCH_TIME = 1.0f;
	
	GameOverRenderer() {
		loadResources();	
	}
	
	void render(float msec) {
		MeshHelper.tic +=msec;
		if(MeshHelper.tic > (2.0f * 3.142f)) MeshHelper.tic = 0.0f;
		
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		setViewOrtho();
		bgMesh.drawMesh();
		
		textMesh.scale.set(32.0f,32.0f,1.0f);
		
		
		switch(CurrentGameVar.gameOverStage){
			case 0: 
			{
				CurrentGameVar.gameOverStage = 1;
				timer = 0.0f;
				break;
			
			}
			case 1:
			{

				textMesh.position.set(Common.SCREEN_WIDTH * 0.5f, Common.SCREEN_HEIGHT * 0.5f, 90.0f);
				float scaleNo = 32.0f;
				
				timer += msec;
				
				if(timer < SCREEN_SWITCH_TIME * 0.5f){
					scaleNo += ((SCREEN_SWITCH_TIME * 0.5f) - timer) * Common.SCREEN_WIDTH * 2.0f;
				}
				
				textMesh.scale.set(scaleNo,scaleNo,1.0f);
				
				textMesh.text = CurrentGameVar.failReason;
				textMesh.drawMesh();
				
				timer += msec;
				if(timer > SCREEN_SWITCH_TIME){
					CurrentGameVar.gameOverStage = 2;
					timer = 0.0f;
				}
				break;
			}
			case 2:
			{
				float scaleSize = 0.125f +( MathUtils.sin(MeshHelper.tic) * 0.03125f);
				
				textMesh.position.set(Common.SCREEN_WIDTH * 0.5f, Common.SCREEN_HEIGHT * 0.7f, 90.0f);
				if(CurrentGameVar.GameScore > CurrentGameVar.HighScore){

					textMesh.scale.set(20.0f,20.0f,1.0f);
					textMesh.text = "NEW HIGH SCORE! Forget this old one :";
				}
				else{

					textMesh.scale.set(32.0f,32.0f,1.0f);
					textMesh.text = "High Score:";
				}
				textMesh.drawMesh();
				
				textMesh.position.set(Common.SCREEN_WIDTH * 0.5f, Common.SCREEN_HEIGHT * 0.6f, 90.0f);

				textMesh.scale.set(32.0f,32.0f,1.0f);
				textMesh.text = String.valueOf(CurrentGameVar.HighScore);
				textMesh.drawMesh();
				
				float scaleNo = 32.0f;
				
				timer += msec;
				
				if(timer < SCREEN_SWITCH_TIME * 0.5f){
					scaleNo += ((SCREEN_SWITCH_TIME * 0.5f) - timer) * Common.SCREEN_WIDTH * 2.0f;
				}
				
				textMesh.scale.set(scaleNo,scaleNo,1.0f);
				
				textMesh.position.set(Common.SCREEN_WIDTH * 0.5f, Common.SCREEN_HEIGHT * 0.5f, 90.0f);
				textMesh.text = "Score:" + String.valueOf(CurrentGameVar.GameScore);
				textMesh.drawMesh();
				
				scaleNo = 32.0f;

				if(timer < SCREEN_SWITCH_TIME){
					scaleNo += ((SCREEN_SWITCH_TIME) - timer) * Common.SCREEN_WIDTH * 2.0f;
				}
				

				textMesh.scale.set(scaleNo,scaleNo,1.0f);

				int messageNo = CurrentGameVar.GameScore / 50;
				if(messageNo > Common.NO_MESSAGES-1) messageNo = Common.NO_MESSAGES - 1; 
				if(messageNo < 0) messageNo = 0;
				textMesh.position.set(Common.SCREEN_WIDTH * 0.5f, Common.SCREEN_HEIGHT * 0.4f, 90.0f);
				textMesh.text = Common.messages[messageNo];
				textMesh.drawMesh();
				
				if(timer > 2.0f	){
					 scaleSize = 0.125f +( MathUtils.sin(MeshHelper.tic) * 0.03125f);
						

					 	menuMesh.scale.set(Common.SCREEN_WIDTH * scaleSize,Common.SCREEN_WIDTH * scaleSize, 1.0f);
					 	menuMesh.position.set(Common.SCREEN_WIDTH * 0.75f, Common.SCREEN_HEIGHT * 0.25f, 90.0f);
					 	menuMesh.drawMesh();
					 	
					 	textMesh.position.set(Common.SCREEN_WIDTH * 0.8f, Common.SCREEN_HEIGHT * 0.1f, 90.0f);
						textMesh.text = "Menu";
						textMesh.drawMesh();
					 	
						
						replayMesh.scale.set(Common.SCREEN_WIDTH * scaleSize,Common.SCREEN_WIDTH * scaleSize, 1.0f);
						
						replayMesh.position.set(Common.SCREEN_WIDTH * 0.25f, Common.SCREEN_HEIGHT * 0.25f, 90.0f);
						replayMesh.drawMesh();
						
						textMesh.position.set(Common.SCREEN_WIDTH * 0.2f, Common.SCREEN_HEIGHT * 0.1f, 90.0f);
						textMesh.text = "Replay";
						textMesh.drawMesh();
					 	
						scaleSize*= 0.25f;
				}			
	
				break;
			}
		
		}
		
		Gdx.gl20.glDisable(GL20.GL_BLEND);
		
	}
	
	Boolean loadResources() {
		updateGameTime();
		
		Boolean hasLoaded = true;
		
		bgMesh = new MeshHelper();
		
		bgMesh.generateQuad();
		bgMesh.position.set(Common.SCREEN_WIDTH * 0.5f, Common.SCREEN_HEIGHT * 0.5f, 90.0f);
		bgMesh.scale.set(Common.SCREEN_WIDTH, Common.SCREEN_HEIGHT, 1.0f);
		bgMesh.bgShader();
		
		menuMesh = new MeshHelper();
		
		menuMesh.generateQuad();
		menuMesh.position.set(Common.SCREEN_WIDTH * 0.5f, Common.SCREEN_HEIGHT * 0.25f, 90.0f);
		menuMesh.colour.set(0.0f,1.0f,1.0f);
		menuMesh.scale.set(Common.SCREEN_WIDTH * 0.5f, Common.SCREEN_WIDTH* 0.5f, 1.0f);
		menuMesh.setTex("data/menu.png");
		menuMesh.TexturedShader();
		
		replayMesh = new MeshHelper();
		
		replayMesh .generateQuad();
		replayMesh .position.set(Common.SCREEN_WIDTH * 0.5f, Common.SCREEN_HEIGHT * 0.25f, 90.0f);
		replayMesh .colour.set(0.0f,1.0f,1.0f);
		replayMesh .scale.set(Common.SCREEN_WIDTH * 0.5f, Common.SCREEN_WIDTH* 0.5f, 1.0f);
		replayMesh .setTex("data/back.png");
		replayMesh .TexturedShader();
		

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
