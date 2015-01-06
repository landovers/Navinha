import java.util.ArrayList;

import org.jsfml.graphics.FloatRect;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.Event;

public class Enemy extends GameObject {
	
	float deadFrameCount;
	public float maxDamagePoints;
	
	EnemyType enemyType;
	// TODO: enemy type should determine enemy HP
	private float currentDamagePoints = 0.0f;
	
	public Enemy(ArrayList<String> gobLineData) {
		super("images", gobLineData.get(5), gobLineData.get(6));
		enemyType = makeEnemyType(Integer.parseInt(gobLineData.get(4)));
		sprite.setPosition(Float.parseFloat(gobLineData.get(2)), Float.parseFloat(gobLineData.get(3)));
		drawPriority = 2;
		gameObjectCollisionGroups.add(CollisionGroup.PLAYER);
		ownCollisionGroup = CollisionGroup.ENEMY;
	}

	private EnemyType makeEnemyType(int enemyTypeIndex) {
		switch (enemyTypeIndex) {
		case 1:
			return (new EnemyTypeSine(this));
		case 2:
			return (new EnemyTypeFwd(this));
		case 3:
			return (new EnemyTypeJellyFish(this));
		default:
			break;
		}
		return null;
	}

	@Override
	public void update(Event currentEvent) {
		// Update using type specific behavior if object can move
		if(isMovable)
			enemyType.update();
		// Update state
		updateObjectState();
		// TODO: Use Object Pattern to set enemy type and behavior (as in Monster<>--Breed)
		
		//This can be removed later
		if(Keyboard.isKeyPressed(Keyboard.Key.S))
			sprite.move(0.0f, speed * Game.elapsedTime);
		if(Keyboard.isKeyPressed(Keyboard.Key.W))
			sprite.move(0.0f, -(speed * Game.elapsedTime));
		if(Keyboard.isKeyPressed(Keyboard.Key.D))
			sprite.move(speed * Game.elapsedTime, 0.0f);
		if(Keyboard.isKeyPressed(Keyboard.Key.A))
			sprite.move(-(speed * Game.elapsedTime), 0.0f);
	}

	@Override
	public void handleCollision(GameObject collidedObject, FloatRect intersect) {
		// TODO: organize better HP, read from file and set with setter method
		currentDamagePoints += Game.elapsedTime;
		// Move this to updateObject State
		
	}

	@Override
	public void updateObjectState() {
		// State transitions
		if (currentDamagePoints > maxDamagePoints) // If taken enough damage to kill
			setObjectState(GameObjectState.DEAD);
		
		// State Data
		switch(objectState) {
		case NORMAL:
			currentAnimIndex = 0;
			stateTimer = 0.0f;
			isCollidible = true;
			isMovable = true;
			break;
		case DEAD:
			currentAnimIndex = 1;
			isCollidible = false;
			isMovable = false;
			if(stateTimer > deadFrameCount) { // State is over
				setMarkedForDeletion(true); // Dead enemies are marked for deletion
			}
			break;
		default:
			break;
		}
	}
	

}
