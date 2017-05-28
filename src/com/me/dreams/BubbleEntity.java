package com.me.dreams;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class BubbleEntity extends GameEntity{

	Boolean active;
	Boolean superBubble;
	final float BUBBLE_SPEED = 1.0f;

	final float Z_POS	 = 50.0f;

	final float X_DESPAWN = Common.SCREEN_WIDTH * 1.5f;
	
	float verticalRecoil;
	int score=0;
	boolean follow = false;
	
	
	BubbleEntity() {
		active = false;
		superBubble = true;
		radius = 32;
		verticalRecoil = 0.0f;
		position.z = Z_POS;

		model = new ModelInstance(Dreams_Of_Dishes.assetManager.get("data/bubble.g3db",Model.class));
		model.transform.setToScaling(32, 32, 32);
				
	}
	
	
	public void update(float msec){
		if(active){
			
			float moveAmount =  msec  * Common.SCREEN_WIDTH * BUBBLE_SPEED;
			if(superBubble) moveAmount *=2;
			position.x += moveAmount;
			
			position.y += verticalRecoil * msec;
			if(position.y > (Common.SCREEN_HEIGHT * 1.1) | position.y < 0)
			{
				verticalRecoil *= -1;
			}
			
			if(position.x > X_DESPAWN){
				active = false;
				
			}
		}
		else
		{
			position.set(Common.OUT_OF_SIGHT);
		}
		model.transform.setTranslation(position);
		model.transform.rotate(1f, 1f, 1f, msec * 360);
	}
	
	public void activate(Vector3 pos){
		position.set(pos);
		score = 1;
		active = true;
		model.transform.rotate(1f, 1f, 1f, MathUtils.random(360));
		follow = false;
	}
	
	public void destroy() {
		active = false;
	}
	
	public void reset() {
		score = 1;
		active = false;
	}
	
	
	
}
