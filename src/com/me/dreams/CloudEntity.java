package com.me.dreams;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.MathUtils;

public class CloudEntity extends GameEntity{

	float horizSpeed;
	
	final float Z_POS	 = -200.0f;
	
	final float X_SPAWN  = Common.SCREEN_WIDTH * 2.0f;
	final float X_DESPAWN= -Common.SCREEN_WIDTH;
	float scaleSize;
	
	CloudEntity() {
		scaleSize = 32;
		horizSpeed = 0;
		radius = 0;
		position.z = Z_POS;

		model = new ModelInstance(Dreams_Of_Dishes.assetManager.get("data/clouds.g3db",Model.class));
		
		activate();
		position.x = MathUtils.random(X_SPAWN);
	}
	
	
	public void update(float msec){
		position.x -= msec  * Common.SCREEN_WIDTH * horizSpeed;	
		
		if(position.x < X_DESPAWN) activate();
		model.transform.setTranslation(position);
	}
	
	public void activate(){
		float temp = MathUtils.random(0,30);
		horizSpeed = 0.1f + (temp * 0.01f);
		
		position.x = X_SPAWN;
		
		position.y = MathUtils.random(Common.SCREEN_HEIGHT * 0.5f,Common.SCREEN_HEIGHT);
		position.z = Z_POS - MathUtils.random(0,100);
		
		scaleSize = 32 + MathUtils.random(0,96);
		
		model.transform.setTranslation(position);
		model.transform.setToScaling(scaleSize, scaleSize, scaleSize);
	}
	
}
