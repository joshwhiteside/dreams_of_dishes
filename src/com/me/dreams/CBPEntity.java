package com.me.dreams;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.MathUtils;

public class CBPEntity extends GameEntity{
	Boolean active;
	float horizSpeed;
	float dieCounter;
	
	final float DIE_TIME = 0.25f;
	final float Z_POS	 = 50.0f;
	
	final float X_SPAWN = Common.SCREEN_WIDTH * 1.5f;
	final float X_DESPAWN = -Common.SCREEN_WIDTH * 0.5f;

	CBPEntity() {
		active = false;
		horizSpeed = 0;
		radius = 64;
		position.z = Z_POS;
		dieCounter = 0.0f;
		
		model = new ModelInstance(Dreams_Of_Dishes.assetManager.get("data/cbp.g3db",Model.class));
	
		model.transform.setToScaling(64f, 64f, 64f);
		activate();
	}
	
	
	public void update(float msec){
		if(active){			
			position.x -= msec  * Common.SCREEN_WIDTH * horizSpeed;

			if(position.x < X_DESPAWN){
				active = false;
			}
		} else{
			if(dieCounter>0.0f){
				position.z-= msec * 4000.0f;
				position.y+= msec * Common.SCREEN_HEIGHT;
				position.x+=  msec * Common.SCREEN_WIDTH;
				dieCounter-=msec;
				
			} else {
			
				activate();
			}
		}
		model.transform.rotate(0.0f, 1f, 0.0f, msec * 100);
		
		model.transform.setTranslation(position);
		
	}
	
	public void activate(){
		active = true;
		float temp = MathUtils.random(0,30);
		horizSpeed = 0.75f + (temp * 0.01f);
		
		position.x = X_SPAWN;

		model.transform.rotate(0.3f, 0.3f, 0.3f, MathUtils.random(0,360));
		
		position.y = MathUtils.random(0,Common.SCREEN_HEIGHT);
		position.z = Z_POS;
		
	}
	
	public void destroy() {
		float temp = 120 + MathUtils.random(0,40);
		CurrentGameVar.Timer += temp * 0.001;
		active = false;
		dieCounter = DIE_TIME;
	}
	
	public Boolean shouldDraw(){
		return (active||dieCounter>0.0f);
	}
	
	public void reset() {
		activate();
		position.x += X_SPAWN * 0.5f;
	}
	
	
	
}
