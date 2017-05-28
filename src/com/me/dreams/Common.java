package com.me.dreams;

import com.badlogic.gdx.math.Vector3;

public class Common {
	static float SCREEN_WIDTH 		= 800.0f;
	static float SCREEN_HEIGHT 		= 480.0f;
	
	static final int NO_MESSAGES = 26;
	
	static final Vector3 STANDARD_LOOK_AT = new Vector3(Common.SCREEN_WIDTH * 0.5f,Common.SCREEN_HEIGHT * 0.5f, -200);
	
	static String messages[] = new String[NO_MESSAGES];
	
	static Vector3 OUT_OF_SIGHT = new Vector3(-5000,-5000,-5000);
	
	
	static final float MEGA_SHOT_TIMER_MASH = 1.5f;
	static final float MEGA_SHOT_TIMER_PRECISE = 1.5f;

	static final int MIN_JUMP_FOR_FAN_SHOT_MASH = 5;
	static final int MIN_JUMP_FOR_FOLLOW_SHOT_MASH = 2;
	static final int MIN_JUMP_FOR_FAN_SHOT_PRECISE = 5;
	static final int MIN_JUMP_FOR_FOLLOW_SHOT_PRECISE = 2;
	static final float FAN_AMOUNT = 60f;
	
	static final int BOSS_DISH_HP = 15;
	static final float TIME_BETWEEN_BOSSES = 15.0f;
	
	static final float DISH_TIME_ADD_MASH = 0.15f;	
	static final float DISH_TIME_ADD_PRECISE = 1.0f;

	
	static public void setMessages(){
		messages[0] = "EVERYTHING IS";
		messages[1] = "NOTHING ISN'T";
		messages[2] = "THINK LESS";
		messages[3] = "FLESH ENTROPY";
		messages[4] = "SILVER EYE GEL : RELY";
		messages[5] = "NOURISH SHRUBS";
		messages[6] = "TENTACLE PESTER";
		messages[7] = "SIMIAN REMORSE COURSE";
		messages[8] = "BANK PASTA";
		messages[9] = "FRANK BLASTER";
		messages[10]= "BARKER PEGS";
		messages[11]= "GLASS PEAR TRANCE";
		messages[12]= "THE SCOTTISH PLUG";
		messages[13]= "SWITCH BISCUITS";
		messages[14]= "PENDANT FLOURISH";
		messages[15]= "SEOUL CRUNCH";
		messages[16]= "BEST REST";
		messages[17]= "BENCH PISTON";
		messages[18]= "HONESTY CORK";
		messages[19]= "MANTIS HANDLE";
		messages[20]= "PREDATOR CANVAS";
		messages[21]= "MULCH PURSE";
		messages[22]= "DRY TATTERS";
		messages[23]= "BOLSHIE PLUMS";
		messages[24]= "THREAD BURST";
		messages[25]= "WONDER DENT";

		
		
	}
	
	static float TOUCH_WIDTH = SCREEN_WIDTH;
	static float TOUCH_HEIGHT = SCREEN_HEIGHT;

	
}
