package com.me.dreams;

import java.util.Calendar;

import com.badlogic.gdx.math.Matrix4;

public class Renderer {

	
	
	void render() {
		
		
		
	}
	

	void setViewOrtho() {
		MeshHelper.viewMatrix.idt();
		
		Matrix4 tempView = new Matrix4();
		tempView.setToOrtho(0, Common.SCREEN_WIDTH, 0, Common.SCREEN_HEIGHT, -100, 500);
		
		MeshHelper.projMatrix = tempView;
		
	}
	
	void updateGameTime(){
		MeshHelper.bgHour 	  = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		float bgMinute		  = Calendar.getInstance().get(Calendar.MINUTE);
		bgMinute = bgMinute / 60.0f;
		MeshHelper.bgHour += bgMinute;
		MeshHelper.hueSeconds = (float)((Calendar.getInstance().get(Calendar.SECOND))/60.0f)*3.142f;
		
	}
	
	
	Boolean loadResources() {
		Boolean hasLoaded = true;
		
		
		return hasLoaded;
	}
	
	
	
}
