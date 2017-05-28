package com.me.dreams;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class BossDishEntity extends GameEntity{

	Boolean active;
	
	float horizSpeed;
	
	float dieCounter;
	int hp;
	
	final float DIE_TIME = 0.25f;
	final float Z_POS	 = 50.0f;
	
	final float X_SPAWN = Common.SCREEN_WIDTH * 1.5f;
	final float X_DESPAWN = -Common.SCREEN_WIDTH * 0.5f;
	
	
	BossDishEntity() {
		active = false;
		horizSpeed = 0;
		radius = 32;
		position.z = Z_POS;
		dieCounter = 0.0f;
		radius = 128;
		
		model = new ModelInstance(Dreams_Of_Dishes.assetManager.get("data/bossdish.g3db",Model.class));

		model.transform.setToScaling(256f, 256f, 256f);
		//activate();
	}
	
	
	public void update(float msec){
		if(active){
			position.x -= msec  * Common.SCREEN_WIDTH * horizSpeed;

			if(position.x < X_DESPAWN){
				active = false;	
			}

			model.transform.rotate(0.0f, 1.0f, 0.0f, msec * 100);
		} 
		else
		{
			if(dieCounter>0.0f){
				position.z-= msec * 4000.0f;
				position.y+= msec * Common.SCREEN_HEIGHT;
				//float temp =  MathUtils.random(0,30);
				//temp-=15.0f;
				position.x+=  msec * Common.SCREEN_WIDTH;
				dieCounter-=msec;

				//model.transform.rotate(0.3f, 0.3f, 0.3f, msec * 800);
			}
			else
			{
				position.set(Common.OUT_OF_SIGHT);
			}
		}
		
		model.transform.setTranslation(position);
		
	}
	
	public void activate(){
		active = true;
		float temp = MathUtils.random(0,10);
		horizSpeed = 0.05f + (temp * 0.01f);
		
		position.x = X_SPAWN;
		
		hp = Common.BOSS_DISH_HP;

		position.y = MathUtils.random(0,Common.SCREEN_HEIGHT);
		position.z = Z_POS;
		
	}
	
	public void destroy() {
		float temp = 300 + MathUtils.random(0,120);
		CurrentGameVar.Timer += temp * 0.001;
		active = false;
		dieCounter = DIE_TIME;
	}
	
	public Boolean shouldDraw(){
		return (active||dieCounter>0.0f);
		
	}
	
	public void reset() {
		//activate();
		active = false;
		position = new Vector3(Common.OUT_OF_SIGHT);
		
	}
	
	
	
}
