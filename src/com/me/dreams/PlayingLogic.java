package com.me.dreams;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class PlayingLogic extends GameLogic {

	final int NO_DISHES = 10;
	final int NO_BUBBLES= 30;
	final int NO_CLOUDS = 5;
	final int NO_SCORES = 10;
	
	public PlayerEntity 	player;
	public DishEntity[]   	dishes;
	public BubbleEntity[]   bubbles;
	public CloudEntity[]	clouds;
	public ScoreEntity[]	scores;
	public CBPEntity 		cbp;
	public BossDishEntity	bossDish;
	
	public Array<ModelInstance> modelInstances;
	
	public Environment environment;
	public ModelBatch modelBatch;
	public PerspectiveCamera camera;
	public ModelInstance mountain;
	
	public float bump;
	
	float dishSpawnTimer;
	int dishSpawnCount = 0;
	
	float bossDishSpawn = 0.0f;
	
	public float upscoreTimer;
	
	final static float UPSCORE_TIME = 0.25f;
	
	public float mountainStretch = 0f;
	
	PlayingLogic() {
		loadResources();
	}
	
	
	
	public void update(float msec) {
		camera.lookAt(new Vector3(0f,bump * 10,0f).add(Common.STANDARD_LOOK_AT));
		
		mountainStretch += msec;
		
		mountain.transform.setToScaling(1f, 1f + (0.25f * MathUtils.sin(mountainStretch)), 1f);
		
		upscoreTimer -=msec;
		
		if(!bossDish.active)
		{
			bossDishSpawn += msec;
		}
		
		if(bossDishSpawn > Common.TIME_BETWEEN_BOSSES)
		{
			bossDishSpawn = 0.f;
			bossDish.activate();
		}
		
		if(upscoreTimer < 0.0f)
			upscoreTimer = 0.0f;
		
		bump *= 0.95f;
		if(bump < 0.1)
		{
			bump = 0.0f;
		}
		
		if(CurrentGameVar.Timer > 5 ) CurrentGameVar.Timer = 5;
		CurrentGameVar.Timer -= msec;
		
		if(CurrentGameVar.Timer<0){
			CurrentGameVar.GameOver();
			CurrentGameVar.failReason= "Time up!";
		}
		
		player.update(msec);
		if(player.shootThisFrame){
			boolean notFanShot = true;
			if(CurrentGameVar.mashMode)
			{
				notFanShot = (player.jumpSinceLastShot < Common.MIN_JUMP_FOR_FAN_SHOT_MASH);
			}
			else
			{
				notFanShot = player.jumpSinceLastShot < Common.MIN_JUMP_FOR_FAN_SHOT_PRECISE;
			}
			
			if(notFanShot)
			{
				for(int i = 0 ; i < NO_BUBBLES; i++){
					if(!bubbles[i].active){
						bubbles[i].activate(player.position);
						bubbles[i].superBubble = player.superBubble;
						bubbles[i].verticalRecoil = player.verticalVelocity * 2;
						boolean isFollowShot = true;
						if(CurrentGameVar.mashMode)
						{
							isFollowShot = player.jumpSinceLastShot >= Common.MIN_JUMP_FOR_FOLLOW_SHOT_MASH;
						}
						else
						{
							isFollowShot = player.jumpSinceLastShot >= Common.MIN_JUMP_FOR_FOLLOW_SHOT_PRECISE;
						}
						
						if(isFollowShot)
						{
							bubbles[i].follow = true;
						}
						
						player.jumpSinceLastShot = 0;
						break;
					}
				}
			}
			else 
			{
				float verticalVelocity = player.verticalVelocity - (Common.FAN_AMOUNT * 2.0f);
				for(int n = 0; n < 5; n++)
				{
					for(int i = 0 ; i < NO_BUBBLES; i++){
						if(!bubbles[i].active){
							bubbles[i].activate(player.position);
							bubbles[i].superBubble = player.superBubble;
							bubbles[i].verticalRecoil = verticalVelocity * 2;
							player.jumpSinceLastShot = 0;
							break;
						}
					}
					verticalVelocity += Common.FAN_AMOUNT;
				}
			}
		}
		if(player.megaShootThisFrame){
			player.megaShootThisFrame = false;
			int found = 0;
			float verticalRecoil = player.verticalVelocity * 2 - player.JUMP_VAL;
			for(int i = 0 ; i < NO_BUBBLES; i++){
				if(!bubbles[i].active){
					bubbles[i].activate(player.position);
					bubbles[i].superBubble = player.superBubble;
					bubbles[i].verticalRecoil = verticalRecoil;
					verticalRecoil += player.JUMP_VAL * 2.0f;
					
					found++;
					player.jumpSinceLastShot = 0;
					if(found==2)
					{
						break;
					}
				}
			}
		}
		/*
		dishSpawnTimer+= msec;
		
		if(dishSpawnTimer > 0.2f)
		{
			if(dishSpawnCount<NO_DISHES)
			{
				dishSpawnTimer = 0.0f;
				dishes[dishSpawnCount].activate();
				dishSpawnCount++;
			}
		}
		*/
		for(int i = 0; i < NO_DISHES; i++){
			dishes[i].update(msec);
		}
		

		for(int i = 0; i < NO_SCORES; i++){
			scores[i].update(msec);
		}

		for(int i = 0; i < NO_BUBBLES; i++){
			bubbles[i].update(msec);
			if(bubbles[i].follow)
			{
				bubbles[i].position.y = player.position.y;
			}
		}

		for(int i = 0; i < NO_CLOUDS; i++){
			clouds[i].update(msec);
		}
		
		cbp.update(msec);
		bossDish.update(msec);
		
		checkCollisions(msec);
	}
	
	public Boolean loadResources() {
		Boolean hasLoaded = true;
		
		modelBatch = new ModelBatch();
		modelInstances = new Array<ModelInstance>();
		
		mountain = new ModelInstance(Dreams_Of_Dishes.assetManager.get("data/mountain.g3db",Model.class));
		
		modelInstances.add(mountain);
		bump = 0.0f;
		
		player = new PlayerEntity();
		modelInstances.add(player.model);
		dishes = new DishEntity[NO_DISHES];
		for(int i = 0; i < NO_DISHES; i++){
			dishes[i] = new DishEntity();
			modelInstances.add(dishes[i].model);
		}
		
		bubbles = new BubbleEntity[NO_BUBBLES];
		for(int i = 0; i < NO_BUBBLES; i++){
			bubbles[i] = new BubbleEntity();
			modelInstances.add(bubbles[i].model);
		}

		clouds = new CloudEntity[NO_CLOUDS];
		for(int i = 0; i < NO_CLOUDS; i++){
			clouds[i] = new CloudEntity();
			modelInstances.add(clouds[i].model);
		}
		
		scores = new ScoreEntity[NO_SCORES];
		for(int i = 0; i < NO_SCORES; i++){
			scores[i] = new ScoreEntity();
		}
		
		cbp = new CBPEntity();
		modelInstances.add(cbp.model);

		bossDish = new BossDishEntity();
		modelInstances.add(bossDish.model);
		
		camera = new PerspectiveCamera(45, Common.SCREEN_WIDTH,Common.SCREEN_HEIGHT);
		camera.near = 1;
		camera.far = 1500;
		//camera.position.set(-Common.SCREEN_WIDTH * 0.5f,- Common.SCREEN_HEIGHT * 0.5f, -0.0f);
		camera.position.set(Common.SCREEN_WIDTH * 0.5f,Common.SCREEN_HEIGHT * 0.5f,750);
		camera.lookAt(Common.STANDARD_LOOK_AT);
		camera.update();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1f));
		environment.add(new DirectionalLight().set(0.4f,0.4f,0.4f, -0.3f, -0.3f, -0.8f));
		
		return hasLoaded;
	}
	
	public Boolean isSuperBubble() {
		Boolean retValue = false;
		
		for(int i = 0; i < NO_BUBBLES; i++){
			if(bubbles[i].superBubble && bubbles[i].active){
				retValue = true;
				break;
			}
		}
		
		return retValue;
	}
	
	public Boolean doDrawMadBG()
	{
		Boolean retValue = false;
		
		Boolean followShotOn;
		Boolean fanShotOn;
		
		if(CurrentGameVar.mashMode)
		{
			followShotOn = player.jumpSinceLastShot>=Common.MIN_JUMP_FOR_FOLLOW_SHOT_MASH;
			fanShotOn	 = player.jumpSinceLastShot>=Common.MIN_JUMP_FOR_FAN_SHOT_MASH;
		}
		else
		{
			followShotOn = player.jumpSinceLastShot>=Common.MIN_JUMP_FOR_FOLLOW_SHOT_PRECISE;
			fanShotOn	 = player.jumpSinceLastShot>=Common.MIN_JUMP_FOR_FAN_SHOT_PRECISE;
		}
		
		if(followShotOn)
		{
			retValue = true;
			CurrentGameVar.bgType = MadBGType.FOLLOW_BG;
		}
		
		if(fanShotOn)
		{
			retValue = true;
			CurrentGameVar.bgType = MadBGType.FAN_BG;
		}
		
		if(player.megaShotTimer>0f)
		{
			retValue = true;
			CurrentGameVar.bgType = MadBGType.CBP_BG;
		}
		
		if(isSuperBubble())
		{
			retValue = true;
			CurrentGameVar.bgType = MadBGType.SB_BG;
		}
		return retValue;
	}
	
	public void reset() {
		upscoreTimer = 0.0f;
		dishSpawnCount = 0;

		
		player.reset();
		for(int i = 0; i < NO_DISHES; i++){
			dishes[i].reset();
		}

		for(int i = 0; i < NO_BUBBLES; i++){
			bubbles[i].reset();
		}
		

		for(int i = 0; i < NO_SCORES; i++){
			scores[i].reset();
		}
		
		bossDish.reset();
		cbp.reset();
		
		CurrentGameVar.GameScore = 0;
	}
	
	public void addScore(int s, Vector3 pos){
		upscoreTimer = UPSCORE_TIME;
		
		for(int i = 0; i < NO_SCORES; i++){
			if(!scores[i].active){
				scores[i].activate(Integer.toString(s), pos);
				break;
			}
		}
	}
	
	public void checkCollisions(float msec) {
		for(int i = 0; i < NO_DISHES; i++){
			if(dishes[i].active){
				if(player.SphereCollision(dishes[i])){
					CurrentGameVar.failReason= "You got got!";
					CurrentGameVar.GameOver();
				}
			}
		}
		
		
		for(int a = 0; a < NO_DISHES; a++){
			if(dishes[a].active){
				for(int b = 0 ; b < NO_BUBBLES; b++){
					if(bubbles[b].active){
						if(bubbles[b].SphereCollision(dishes[a])){
							SoundSystem.playExplosion();
							if(!bubbles[b].superBubble){
								bubbles[b].destroy();
							} else {
								bubbles[b].score++;
							}

							CurrentGameVar.GameScore+= bubbles[b].score;
							addScore(bubbles[b].score, dishes[a].position);
							dishes[a].destroy();
							bump+= MathUtils.random(-2.5f, 2.5f);
							if(bump<0) bump-=1.0f;
							else bump+=1.0f;
							

							Gdx.input.vibrate(100);
						}
					}
				}
			}
		}
		
		for(int i = 0; i < NO_BUBBLES; i++)
		{
			if(bubbles[i].SphereCollision(cbp))
			{
				//bubbles[i].reset();
				cbp.reset();
			}
			if(bossDish.active)
			{
			if(bubbles[i].SphereCollision(bossDish))
			{
				bump+= MathUtils.random(-2.5f, 2.5f);
				if(bump<0) bump-=1.0f;
				else bump+=1.0f;
					if(bubbles[i].superBubble)
					{	
						bossDish.hp = 0;
					}
					else
					{
						bossDish.hp--;
						bubbles[i].reset();
					}
					if(bossDish.hp<1)
					{
						Vector3 bossDishPos = new Vector3(bossDish.position);
						bubbles[i].score+= 30;
						CurrentGameVar.GameScore += bubbles[i].score;
						addScore(bubbles[i].score,bossDishPos);
						bossDish.destroy();
					}
				}
			}
		}
		
		if(player.SphereCollision(cbp))
		{
			if(CurrentGameVar.mashMode)
			{
				player.megaShotTimer = Common.MEGA_SHOT_TIMER_MASH;
			}
			else
			{
				player.megaShotTimer = Common.MEGA_SHOT_TIMER_PRECISE;
			}
			player.megaShoot(msec);
			cbp.reset();
		}
		
		if(player.SphereCollision(bossDish))
		{
			if(bossDish.active){
				if(player.SphereCollision(bossDish)){
					CurrentGameVar.failReason= "Filth destroyed you!";
					CurrentGameVar.GameOver();
				}
			}
		}
	}
	
	public void GameRender(float dt)
	{
		camera.update();
		
		modelBatch.begin(camera);
		modelBatch.render(modelInstances,environment);
		modelBatch.end();	
	}
	
}
