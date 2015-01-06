import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.event.Event;

public class GameObjectManager {
	
	ArrayList<GameObject> gameObjects; // Store all GameObjects
	BitMaskCollisionController collisionController;

	public GameObjectManager() {
		// Instantiate important members
		gameObjects = new ArrayList<GameObject>();
		collisionController = new BitMaskCollisionController();
	}
	
	public ArrayList<GameObject> getGameObjects() {
		return gameObjects;
	}

	// Update all Objects, usually State and Position
	public void updateAll(Event currentEvent) {
		// Store newly created objects, if any
		ArrayList<GameObject> toAddObjects = new ArrayList<GameObject>();
		
		for (GameObject g : gameObjects) {
			// Update each GameObject in ArrayList
			g.update(currentEvent); 
			// TODO: Use visitor pattern for this?
			toAddObjects = getChildrenAll();
		}
		// Add all children to gameObjects
		gameObjects.addAll(toAddObjects);
	}
	
	// Get all Children Objects and add them to gameObjects list 
	public ArrayList<GameObject> getChildrenAll() {
		ArrayList<GameObject> toAddObjects = new ArrayList<GameObject>();
		for(GameObject g: gameObjects) {
			// If childObject exists and not yet added to gameObjects list 
			if(!g.getChildObjects().isEmpty()) {
				for(GameObject child: g.getChildObjects()) {
					if(!gameObjects.contains(child))
					toAddObjects.add(child);
				}
			}
		}
		return toAddObjects;
	}

	// Draw all visible objects on screen
	public void drawAll(RenderWindow mainWindow) {
		for (GameObject g : gameObjects) {
			//mainWindow.draw(g.getSprite()); // Draw every GameObject on screen
			// TODO: Consider draw priorities (order matters!)
			// TODO: Just sort gameObjects from time to time based on drawPriority
			g.draw(mainWindow);
		}
	}

	// Add Enemies reads from file to gameObjects list. Handled by EnemyFactory
	public void addObjects(GameObjectFactory gameObjectFactory) {
		ArrayList<GameObject> newEnemies = new ArrayList<GameObject>();
		newEnemies = gameObjectFactory.makeGameObject(); // Makes enemies from file read
		if(!newEnemies.isEmpty()) {
			/*for(GameObject enemy : newEnemies) {
				collisionController.Bitmasks.GetMask(enemy.getTexture());
			}*/
			gameObjects.addAll(newEnemies); // Set the TextureRect correctly and update frames
		}
	}

	// Run animations on all gameObjects
	public void animateAll() {
		for (GameObject g : gameObjects) {
			g.animate(); // Set the TextureRect correctly and update frames
		}
	}

	// Check collisions between all collidible Objects and let them handle collisions
	public void checkCollisionAll() {
		// TODO: Load Masks
		//collisionController.Bitmasks.GetMask(g.getTexture());
		for (GameObject gob1 : gameObjects) { // Check all objects against all others
			for (GameObject gob2 : gameObjects) {
				if(!gob1.equals(gob2)) { // Object can't collide with itself
					// TODO: use getters for isCollidible
					// TODO: get if g1 can collide at all with g2 specifically
					if(gob1.getIsCollidible() && gob2.getIsCollidible()) {
						// TODO: Use Visitor pattern for this?
						// g1.accept(collisionHandler, gameObjects)
						//if(collisionController.CircleTest(g1, g2)) {
						//if(collisionController.BoundingBoxTest(g1, g2)) {
						if(collisionController.pixelPerfectTest(gob1, gob2)) {
							FloatRect intersect = collisionController.getLatestIntersect();
							//System.out.println("g1:"+gob1+" g2:"+gob2);
							if(gob1.getIsCollidible(gob2))
								gob1.handleCollision(gob2, intersect);
							if(gob2.getIsCollidible(gob1))
							gob2.handleCollision(gob1, intersect);
						}
					}
				}
			}
		}
	}

	// Obsolete GameObjects are marked for deletion and are removed from list
	public void deleteMarked() { 
		Iterator<GameObject> iter = gameObjects.iterator();
		while(iter.hasNext()){
			// if marked for deletion or completely out of bounds, remove from game
			GameObject gob = iter.next(); 
		    if(gob.getMarkedForDeletion() || gob.outOfBoundsTotally())
		    	iter.remove();
		}
		
	}

	public void sortObjects() {
		Collections.sort(gameObjects);
	}
	
}
