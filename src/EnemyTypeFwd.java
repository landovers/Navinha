



public class EnemyTypeFwd implements EnemyType {
	
	Enemy e;
	float shootingTimer = 0.0f;
	float maxShotTime;
	
	public EnemyTypeFwd(Enemy e) {
		e.rectangleHeight = 32; 
		e.rectangleWidth = 64;
		e.speed = 100.0f;
		e.maxDamagePoints = 1.0f;
		e.animSpeed = 20.0f;
		e.deadFrameCount = 10.0f;
		maxShotTime = 1.0f;
		this.e = e;
	}
	
	@Override
	public void update() {
		e.sprite.move(-(e.speed/2 * Game.elapsedTime), 0.0f);
		
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
