import java.util.HashMap;
import java.util.Map;

import org.jsfml.graphics.FloatRect;
import org.jsfml.window.event.Event;

public class PlayerShipShot extends GameObject implements PlayerShipShotType {
	
	// Set to store objects hit by the shot (Set avoids duplicates)
	Map<GameObject, FloatRect> collidedObjects = new HashMap<GameObject, FloatRect>();
	FloatRect closestIntersect;
	PlayerShip playerShip;
	static final int hitDelta = 10;
	

	public PlayerShipShot(PlayerShip parent) {
		super("images", "nave_tiro_02.png", "hb-nave_tiro_02.png");
		// TODO: Read this info from file?
		// TODO: If so needs a file reader class to encapsulate the work
		speed = 230.0f; 
		animSpeed = 500.0f;
		rectangleHeight = 16; 
		rectangleWidth = 16;
		isCollidible = true;
		playerShip = parent;
		drawPriority = 4;
		sprite.setPosition(parent.getSprite().getPosition().x + 
				parent.rectangleWidth, parent.getSprite().getPosition().y);
		gameObjectCollisionGroups.add(CollisionGroup.ENEMY);
		gameObjectCollisionGroups.add(CollisionGroup.FG01);
		ownCollisionGroup = CollisionGroup.PLAYER;
	}
	public PlayerShipShot() {
		super("images", "", "");
	}
	
	@Override
	public void update(Event currentEvent) {
		updateObjectState();
		
		// TODO: Organize this, crappy programming. Set ANIM_SPEED to 50 to see problem
		// Set playerShipShot position depending on parent position
		sprite.setPosition(playerShip.getSprite().getPosition().x + 
				playerShip.rectangleWidth, playerShip.getSprite().getPosition().y);
		
		// Use Set of collidedObjects every loop iteration to decide where to stop drawing shot
		// TODO: Organize this to collide properly. Should be Box Collision?
		// If collided with more than one, shot has to stop at first (left-most)
		GameObject gLeftMost = null;
		if(!collidedObjects.isEmpty()) {
			// Get left most object hit by shot (replace with template method: get hit enemy/enemies)
			for(GameObject g : collidedObjects.keySet()) {
				if(gLeftMost == null) 
					gLeftMost = g; 
				if(g.getSprite().getPosition().x < gLeftMost.getSprite().getPosition().x) {
					gLeftMost = g;
					closestIntersect = collidedObjects.get(g);
				}		
			}
			// Drawing shot stops @ position of hit enemy 
			 rectangleWidth = (int) (gLeftMost.getSprite().getPosition().x - this.getSprite().getPosition().x +
				gLeftMost.getSprite().getLocalBounds().width/2);
			//if(closestIntersect != null)
				//rectangleWidth = (int) (closestIntersect.left - this.getSprite().getPosition().x);
			if(rectangleWidth == 0) 
				rectangleWidth = 1; // Avoid division by zero
		} else {
			rectangleWidth = Game.SCREEN_WIDTH;
		}
		collidedObjects.clear(); // Empty Set for next iteration		
	}

	@Override
	public void handleCollision(GameObject gameObject, FloatRect intersect) {
		// Fill Set of collided objects; Sets will not add duplicates (add returns false)
		collidedObjects.put(gameObject, intersect);
		closestIntersect = FloatRect.EMPTY;
	}

	@Override
	public void updateObjectState() {
		
	}

}
