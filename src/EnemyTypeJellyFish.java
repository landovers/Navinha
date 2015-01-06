



public class EnemyTypeJellyFish implements EnemyType {

	Enemy e;
	float shootingTimer = 0.0f;
	float maxShotTime;

	
	public EnemyTypeJellyFish(Enemy e) {
		e.rectangleHeight = 64; 
		e.rectangleWidth = 64;
		e.speed = 100.0f;
		e.maxDamagePoints = 1.5f;
		e.animSpeed = 40.0f;
		e.deadFrameCount = 5.0f;
		maxShotTime = 50.0f;
		this.e = e;
	}
	
	@Override
	public void update() {
		float x = e.sprite.getPosition().x;
		e.sprite.move(-(e.speed * Game.elapsedTime), 0.1f*(float)Math.sin((x-320)*Game.elapsedTime) );
		
		shootingTimer += Game.elapsedTime;
		if(shootingTimer >= maxShotTime) {
			shoot();
			shootingTimer = 0.0f;
		}
	}

	@Override
	public void shoot() {
		
		
	}
	@Override
	public Enemy getEnemy() {
		return e;
	}

}
