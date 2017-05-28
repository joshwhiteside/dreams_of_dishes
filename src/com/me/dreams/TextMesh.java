package com.me.dreams;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;

public class TextMesh extends MeshHelper{
	
	TextMesh(Boolean rainbow){
		align = TextAlign.CENTRE;
		generateQuad();

		setupValues();
		if(rainbow){
			RainbowShader();
		} else {
			TexturedShader();
		}
	}
	
	String text;
	TextAlign align;
	
	public void drawMesh() {
		for(int i = 0; i < text.length(); i++){
			modelMatrix.idt();
			textureMatrix.idt();
			

			Character c = text.charAt(i);
			float x,y;
			
			x = ((int)c) % 16;
			y = ((int)c) / 16;
			
			textureMatrix.scale(1.0f/16.0f, 1.0f/16.0f, 1.0f);
			textureMatrix.translate(x,y,0.0f);

			switch(align)				
			{
			case LEFT :
			{
				modelMatrix.translate(position.x+ (scale.x * i),position.y,position.z);
				break;
			}
			case RIGHT:
			{
				float tempX = (position.x + scale.x * i)  - (scale.x * text.length());
				modelMatrix.translate(tempX,position.y,position.z);
				break;
			}
			case CENTRE:
			{
				float tempX = (position.x + scale.x * i)  - ((scale.x  * 0.5f )* (float)text.length());
				modelMatrix.translate(tempX,position.y,position.z);
				break;
			}
				
			}
		
			modelMatrix.scl(scale);
			modelMatrix.rotate(rotationAxis, degrees);
			
			
			drawCore();
			
			
		}
		
		
	}
	
	public void drawCore(){
		meshShader.begin();
		
		meshShader.setUniformMatrix(meshShader.getUniformLocation("textureMatrix"), textureMatrix);
		meshShader.setUniformMatrix(meshShader.getUniformLocation("modelMatrix"), modelMatrix);
		meshShader.setUniformMatrix(meshShader.getUniformLocation("viewMatrix"), viewMatrix);
		meshShader.setUniformMatrix(meshShader.getUniformLocation("projMatrix"), projMatrix);
		meshShader.setUniformMatrix(meshShader.getUniformLocation("normalMatrix"), normalMatrix);
		
		meshShader.setUniformf(meshShader.getUniformLocation("lightColour"), lightColour.x, lightColour.y, lightColour.z);
		
		meshShader.setUniformf(meshShader.getUniformLocation("cameraPos"), cameraPos.x, cameraPos.y, cameraPos.z);
		
		meshShader.setUniformf(meshShader.getUniformLocation("lightPos"), lightPos.x, lightPos.y, lightPos.z);
		meshShader.setUniformf(meshShader.getUniformLocation("lightRadius"), lightRadius);
		meshShader.setUniformf(meshShader.getUniformLocation("hueSeconds"), hueSeconds);
		
		float temp = bgHour;
		
		temp = temp / 24.0f;
		temp*= 3.142;
		temp = MathUtils.sin(temp);
		

		meshShader.setUniformf(meshShader.getUniformLocation("tic"), tic);

		meshShader.setUniformf(meshShader.getUniformLocation("isSuper"), drawMadBG);
		
		
		
		meshShader.setUniformf(meshShader.getUniformLocation("dayRatio"), temp);
		
		meshShader.setUniformf(meshShader.getUniformLocation("colour"), colour.x, colour.y, colour.z);
		
		if(tex!=null){
			Gdx.gl20.glActiveTexture(GL20.GL_TEXTURE0);
			Gdx.gl20.glBindTexture(GL20.GL_TEXTURE_2D, tex.getTextureObjectHandle());

			meshShader.setUniformi(meshShader.getUniformLocation("s_texture"),0);
			
		}
		mesh.render(meshShader, GL20.GL_TRIANGLES);
		meshShader.end();
		
		
	}
	
}
