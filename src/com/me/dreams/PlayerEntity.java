package com.me.dreams;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class PlayerEntity extends GameEntity{

	float verticalVelocity;
	
	Boolean doJump;
	Boolean lastJump;
	int jumpSinceLastShot = 0;
	
	Boolean shootThisFrame;
	Boolean megaShootThisFrame;
	
	Boolean doShoot;
	Boolean lastShoot;
	float megaShotTimer;
	float megaShotLastShot;
	
	Boolean canSuper;
	Boolean lastSuper;
	
	Boolean superBubble;
	
	Vector3 touchPos;
	
	final float JUMP_VAL = 512.0f;
	final float Z_POS	 = 50.0f;
	
	
	PlayerEntity() {
		touchPos 			= new Vector3();
		radius = 16;
		
		model = new ModelInstance(Dreams_Of_Dishes.assetManager.get("data/bottle.g3db",Model.class));
		model.transform.setToScaling(32, 32, 32);
		
		reset();
	}
	
	
	void update(float msec) {
		doShoot = false;
		doJump  = false;
		canSuper = false;
		lastSuper = false;
		shootThisFrame = false;
		//megaShootThisFrame = false;
		
		
		for(int i = 0; i < 5; i++)
		{
			if(Gdx.input.justTouched() && Gdx.input.isTouched(i)){
				touchPos.set(Gdx.input.getX(i), Gdx.input.getY(i), 0);
			
				if(touchPos.x <= (Common.TOUCH_WIDTH * 0.5f)) doJump = true;
				else doShoot = true;
			}
		}
		
		if(Gdx.input.isKeyPressed(Keys.Z)) doJump  = true;
		if(Gdx.input.isKeyPressed(Keys.X)) doShoot = true;
		
		if(megaShotTimer > 0.0f)
		{
			megaShotTimer -= msec;
			megaShotLastShot += msec;
			if(megaShotLastShot > 0.1f)
			{
				megaShotLastShot = 0.0f;
				megaShoot(msec);
			}
		}
		else if(doShoot && !lastShoot)
		{
			shoot(msec);
		}
		
		if(doJump && !lastJump)
		{
			jump(msec);
		}
		
		lastShoot 	= doShoot;
		lastJump 	= doJump;
		
		verticalVelocity -= JUMP_VAL * msec;
		 
		position.y += verticalVelocity * msec; 
		
		if(position.y > Common.SCREEN_HEIGHT -16)
		{
			verticalVelocity = 0f;
			position.y = Common.SCREEN_HEIGHT - 16;
		}
		if(position.y < -64){
			CurrentGameVar.failReason= "You fell!";
			CurrentGameVar.GameOver();
		}
		
		if((verticalVelocity * verticalVelocity) < JUMP_VAL * 0.25 ){
			canSuper = true;
			if(canSuper&&!lastSuper){
				SoundSystem.playBlip();
			}
			lastSuper = canSuper;
		}
		 
		model.transform.setTranslation(position);
	}
	
	void jump(float msec) {
		verticalVelocity = JUMP_VAL;
		SoundSystem.playJump();
		jumpSinceLastShot++;
	}
	
	void shoot(float msec){
		if((verticalVelocity * verticalVelocity) < JUMP_VAL ){
			//FIRE SUPER BULLET AT POSITION
			superBubble = true;
			
			SoundSystem.playSuperShoot();
		} else {
			//FIRE NORMAL BULLET AT LOCATION
			superBubble = false;
			SoundSystem.playShoot();
		}
		shootThisFrame = true;
	}
	
	void megaShoot(float msec){
		if((verticalVelocity * verticalVelocity) < JUMP_VAL ){
			//FIRE SUPER BULLET AT POSITION
			superBubble = true;
			SoundSystem.playSuperShoot();
		} else {
			//FIRE NORMAL BULLET AT LOCATION
			superBubble = false;
			SoundSystem.playShoot();
		}
		
		megaShootThisFrame = true;
	}
	
	void reset() {
		doShoot 			= false;
		doJump  			= false;
		lastJump			= true;
		lastShoot 			= true;
		megaShotTimer		= -1.0f;
		megaShootThisFrame  = false;
		jumpSinceLastShot   = 0;
		
		verticalVelocity	=JUMP_VAL;

		position.set(16.f,250.f,Z_POS);		
	}
	
}
