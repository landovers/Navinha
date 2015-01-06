import org.jsfml.graphics.FloatRect;
import org.jsfml.system.Vector2f;
import org.jsfml.window.event.Event;




public class EnemyShot extends GameObject {

	Enemy enemy;
	Vector2f playerPosition;
	Vector2f shotInitialPosition;
	float lineSlope;
	float euclidianDistance;
	
	public EnemyShot(EnemyType enemyType) {
		super("images", "tiro01.png", "hb-tiro01.png");
		enemy = enemyType.getEnemy();
		sprite.setPosition(enemy.sprite.getPosition().x, enemy.sprite.getPosition().y);
		rectangleHeight = 16;
		rectangleWidth = 16;
		speed = 200.0f;
		animSpeed = 80.0f;
		playerPosition = PlayerShip.getPosition();
		shotInitialPosition = sprite.getPosition();
		lineSlope = (playerPosition.x - sprite.getPosition().x) / (playerPosition.y - sprite.getPosition().y);
		euclidianDistance = Math.abs(playerPosition.x - sprite.getPosition().x) + Math.abs(playerPosition.y - sprite.getPosition().y);
		gameObjectCollisionGroups.add(CollisionGroup.PLAYER);
		ownCollisionGroup = CollisionGroup.ENEMYSHOT;
	}

	@Override
	public void update(Event currentEvent) {
		// Something wrong with this
		//updateObjectState();
		/* This is some golden hilarious stuff to use!
		 * Vector2f playerPosition = PlayerShip.getPosition();
		float lineSlope = (playerPosition.x - sprite.getPosition().x) / ((playerPosition.y - sprite.getPosition().y));
		if(playerPosition.x > sprite.getPosition().x) {
			sprite.move(0.0f, speed * lineSlope * Game.elapsedTime);
		} else {
			sprite.move(0.0f, -(speed * lineSlope * Game.elapsedTime));
		}
		if(playerPosition.y > sprite.getPosition().y) {
			sprite.move(speed * lineSlope * Game.elapsedTime, 0.0f);
		} else {
			sprite.move(-(speed * lineSlope * Game.elapsedTime), 0.0f);
		}*/
		/* This is good too!!! faster with more distance
		sprite.move((playerPosition.x - shotInitialPosition.x) * Game.elapsedTime,
				(playerPosition.y - shotInitialPosition.y) * Game.elapsedTime);
				*/
		sprite.move(((playerPosition.x - shotInitialPosition.x)/euclidianDistance) * Game.elapsedTime * speed,
				((playerPosition.y - shotInitialPosition.y)/euclidianDistance) * Game.elapsedTime * speed);
	}

	@Override
	public void handleCollision(GameObject collidedObject, FloatRect intersect) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateObjectState() {
		if(outOfBounds()) {
			this.markedForDeletion = true;
		}
	}
}
