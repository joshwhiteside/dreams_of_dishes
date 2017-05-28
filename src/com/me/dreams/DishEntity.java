package com.me.dreams;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.MathUtils;

public class DishEntity extends GameEntity{

	Boolean active;
	
	float horizSpeed;
	
	float dieCounter;
	
	
	final float DIE_TIME = 0.25f;
	final float Z_POS	 = 50.0f;
	
	final float X_SPAWN = Common.SCREEN_WIDTH * 1.5f;
	final float X_DESPAWN = -Common.SCREEN_WIDTH * 0.5f;
	
	DishType dishType = DishType.BOWL;
	
	DishEntity() {
		active = false;
		horizSpeed = 0;
		radius = 32;
		position.z = Z_POS;
		dieCounter = 0.0f;
		
		int newDishType = MathUtils.random(0,DishType.NUM_TYPES);
		
		switch(newDishType)
		{
			case 0: 
			{
				dishType = DishType.BOWL;
				model = new ModelInstance(Dreams_Of_Dishes.assetManager.get("data/bowl.g3db",Model.class));
				break;
			}
			case 1:
			{
				dishType = DishType.PLATE;
				model = new ModelInstance(Dreams_Of_Dishes.assetManager.get("data/plate.g3db",Model.class));

				break;
			}
			case 2: 
			{
				dishType = DishType.SPOON;
				model = new ModelInstance(Dreams_Of_Dishes.assetManager.get("data/spoon.g3db",Model.class));
				break;
			}
			default :
			{
				dishType = DishType.SPOON;
				model = new ModelInstance(Dreams_Of_Dishes.assetManager.get("data/spoon.g3db",Model.class));
				break;
			}
		}
		
		model.transform.setToScaling(32f, 32f, 32f);
	}
	
	
	public void update(float msec){
		if(active){
			position.x -= msec  * Common.SCREEN_WIDTH * horizSpeed;

			if(position.x < X_DESPAWN){
				active = false;	
			}

			model.transform.rotate(0.3f, 0.3f, 0.3f, msec * 100);
		} 
		else
		{
			if(dieCounter>0.0f){
				position.z-= msec * 4000.0f;
				position.y+= msec * Common.SCREEN_HEIGHT;
				position.x+=  msec * Common.SCREEN_WIDTH;
				dieCounter-=msec;

				model.transform.rotate(0.3f, 0.3f, 0.3f, msec * 800);
			} else {
			
				activate();
			}
		}
		
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
		if(CurrentGameVar.mashMode)
		{
			CurrentGameVar.Timer += Common.DISH_TIME_ADD_MASH;
		}
		else
		{
			CurrentGameVar.Timer += Common.DISH_TIME_ADD_PRECISE;
		}
		active = false;
		dieCounter = DIE_TIME;
	}
	
	public Boolean shouldDraw(){
		return (active||dieCounter>0.0f);
		
	}
	
	public void reset() {
		active = false;
		position.x += X_SPAWN * 0.5f;
	}
	
	
	
}
