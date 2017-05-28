package com.me.dreams;

import com.badlogic.gdx.math.Vector3;

public class ScoreEntity extends GameEntity{

	static final float SCORE_SHOW_TIMER = 0.25f;
	
	String text;
	Boolean active;
	float timer;
	
	ScoreEntity() {
		active = false;
		timer = 0.0f;
		text = "";
	}
	
	
	void activate(String t,Vector3 pos){
		text = t;
		active = true;
		position.set(pos);
		timer = SCORE_SHOW_TIMER;
	}
	
	void update(float msec){
		if(active){
			position.y+= Common.SCREEN_HEIGHT * 2.0f * msec;
			timer -=msec;
			if(timer < 0.0f) active = false;
		}
		
		
	}
	
	void reset() {
		
		active = false;
	}
	
	
}
