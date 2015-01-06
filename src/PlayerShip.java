import java.util.ArrayList;
import java.util.Iterator;

import org.jsfml.graphics.FloatRect;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;
import org.jsfml.window.event.Event;


public class PlayerShip extends GameObject {
	
	private final float deadFrameCount = 10.0f;
	private final float respawningFrameCount = 20.0f;
	private static Vector2f playerPosition;
	private static int playerLives;
	// Shot Types
	public enum ShotType { REGULAR, BACK, SPREAD, HOMING }
	
	private Boolean isShooting = false;
	
	public PlayerShip(ArrayList<String> gobLineData) {
		super("images", gobLineData.get(4), gobLineData.get(5));
		// TODO: Read this info from file!!!
		// TODO: If so needs a file reader class to encapsulate the work
		speed = 230.0f; 
		animSpeed = 100.0f;
		rectangleHeight = 16; // TODO: read all this from permanent.txt file
		rectangleWidth = 32;
		drawPriority = 1;
		sprite.setPosition(Float.parseFloat(gobLineData.get(2)), Float.parseFloat(gobLineData.get(3)));
		playerPosition = new Vector2f(sprite.getPosition().x, sprite.getPosition().y);
		playerLives = 3;
		gameObjectCollisionGroups.add(CollisionGroup.ENEMY);
		gameObjectCollisionGroups.add(CollisionGroup.ENEMYSHOT);
		gameObjectCollisionGroups.add(CollisionGroup.FG01);
		ownCollisionGroup = CollisionGroup.PLAYER;
	}
	public PlayerShip() {
		super("images", "", "");
	}

	@Override
	public void update(Event currentEvent) {
		
		playerPosition = new Vector2f(sprite.getPosition().x, sprite.getPosition().y);
		updateObjectState();
		
		if(isMovable){
			if(Keyboard.isKeyPressed(Keyboard.Key.DOWN))
				sprite.move(0.0f, speed * Game.elapsedTime);
			if(Keyboard.isKeyPressed(Keyboard.Key.UP))
				sprite.move(0.0f, -(speed * Game.elapsedTime));
			if(Keyboard.isKeyPressed(Keyboard.Key.RIGHT))
				sprite.move(speed * Game.elapsedTime, 0.0f);
			if(Keyboard.isKeyPressed(Keyboard.Key.LEFT))
				sprite.move(-(speed * Game.elapsedTime), 0.0f);
			
			// Move this heavy-work to state machine?
			if(Keyboard.isKeyPressed(Keyboard.Key.Z) && !isShooting && 
					(!getObjectState().equals(GameObjectState.DEAD))) {
				// TODO: change to add instance of PlayerShipShot to gameObjects list?
				// If started to press Shot key, Set isShooting and Spawn new shot
				isShooting = true; 
				childObjects.add(new PlayerShipShot(this));
			} 
			if(currentEvent != null) {
				if(((currentEvent.type.equals(Event.Type.KEY_RELEASED) && 
						(currentEvent.asKeyEvent().key.equals(Keyboard.Key.Z))) ||
						!Keyboard.isKeyPressed(Keyboard.Key.Z)))/* && !(childObjects.isEmpty()) && isShooting*/
						/*!getObjectState().equals(GameObjectState.NORMAL) && !getObjectState().equals(GameObjectState.RESPAWNING)*/
								/*getObjectState().equals(GameObjectState.DEAD) ||*/ {
					// If stopped pressing Shot key, mark existing shot for deletion
					isShooting = false;
					Iterator<GameObject> iter = childObjects.iterator();
					while(iter.hasNext()) {
					    iter.next().setMarkedForDeletion(true);
					    iter.remove();
					}
				}
			}
		}
	}

	@Override
	public void handleCollision(GameObject collidedObject, FloatRect intersect) {
		// TODO: Change state based on Collision
		if(getObjectState().equals(GameObjectState.NORMAL)) {
				setObjectState(GameObjectState.DEAD);
				playerLives--;
		}
		// TODO: Call method to take damage
	}
	
	@Override
	public void updateObjectState() {
		switch(objectState) {
		// State transitions
		
		
		// State data and timing
			case NORMAL:
				currentAnimIndex = 0;
				stateTimer = 0.0f;
				break;
			case DEAD:
				currentAnimIndex = 1;
				isCollidible = true;
				isMovable = false;
				if(stateTimer > deadFrameCount) { // State is over
					setObjectState(GameObjectState.RESPAWNING);
					isMovable = true;
					stateTimer = 0;
					System.out.println("changed to RESPAWNING");
				}
				break;
			case RESPAWNING:
				currentAnimIndex = 2;
				isCollidible = false;
				if(stateTimer > respawningFrameCount) { // State is over
					setObjectState(GameObjectState.NORMAL);
					stateTimer = 0;
					isCollidible = true;
					System.out.println("changed to NORMAL");
				}
				break;
			default:
				break;
		}
	}
	public static Vector2f getPosition() {
		return playerPosition;
	}
	public static int getLives() {
		return playerLives;
	}
	public static void setLives(int i) {
		playerLives = i;
		
	}
		
	
}
