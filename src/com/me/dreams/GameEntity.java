package com.me.dreams;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class GameEntity {
	Vector3 position;
	float	radius;
	
	ModelInstance model;
	
	
	GameEntity(Vector3 pos) {
		position = new Vector3();
		position = pos;
		
	}
	
	GameEntity() {
		position = new Vector3();
		position.set(0.0f,0.0f,0.0f);
	}
	
	
	void update(float msec){
	}
	
	Boolean SphereCollision(GameEntity other) {
		Boolean hasCollided = false;
		
		
		//Both radii combined
		//If the distance between the two objects is less than this then we have a collision.
	    float minDistCollide = radius + other.radius;
	    
	    
	    Vector3 distance = new Vector3();
	    
	    distance.set(position.x - other.position.x, position.y - other.position.y, position.z - other.position.z );
	    
	    if (distance.len() < minDistCollide){
	    	hasCollided = true;
	    }
		return hasCollided;
	}
	
	void reset() {
		
	}
	
	
	
}
