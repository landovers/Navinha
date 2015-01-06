




public class EnemyTypeSine implements EnemyType {

	// Enemy object that owns this object
	Enemy e;
	float shootingTimer = 0.0f;
	float maxShotTime;

	
	public EnemyTypeSine(Enemy e) {
		e.rectangleHeight = 16; 
		e.rectangleWidth = 32;
		e.speed = 40.0f;
		e.maxDamagePoints = 0.5f;
		e.animSpeed = 40.0f;
		e.deadFrameCount = 10.0f;
		maxShotTime = 1.0f;
		this.e = e;
	}
	
	@Override
	public void update() {
		float x = e.getSprite().getPosition().x;
		// Move left doing a sine wave
		//sprite.move(-(SPEED*elapsedTime), 0.2f*(float)Math.sin(x*elapsedTime) );
		//sprite.move(-(SPEED*elapsedTime), 1.2f*(float)Math.sin((x-320)*elapsedTime) );
		e.getSprite().move(-(e.speed*Game.elapsedTime), 0.1f*(float)Math.sin((x-320)*Game.elapsedTime) );
		
		shootingTimer += Game.elapsedTime;
		if(shootingTimer >= maxShotTime) {
			shoot();
			shootingTimer = 0.0f;
		}
		
	}

	@Override
	public void shoot() {
		// Add enemyShot.speed to constructor?
		e.getChildObjects().add(new EnemyShot(this));
	}
	@Override
	public Enemy getEnemy() {
		return e;
	}

}
