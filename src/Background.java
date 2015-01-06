import java.util.ArrayList;

import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.event.Event;

public class Background extends GameObject {
	
	public Background(ArrayList<String> gobLineData) {
		super("images", gobLineData.get(4), gobLineData.get(4));
		isCollidible = false;
		speed = 200.0f; // LeverLoader/LevelManager decides this (allow for vertical and horizontal movement)
		rectangleHeight = Game.SCREEN_HEIGHT; 
		rectangleWidth = Game.SCREEN_WIDTH;
		sprite.setPosition(Float.parseFloat(gobLineData.get(2)), Float.parseFloat(gobLineData.get(3)));
		drawPriority = 5;
		ownCollisionGroup = CollisionGroup.BG;
	}

	@Override
	public void update(Event currentEvent) {
		sprite.move(-(speed * Game.elapsedTime), 0.0f);
		if(getSprite().getPosition().x <= (-rectangleWidth)) {
			getSprite().move(rectangleWidth, 0.0f);
		}
	}

	@Override
	public void handleCollision(GameObject collidedObject, FloatRect intersect) {
		
	}

	@Override
	public void updateObjectState() {
		
	}
	
	
	// Draw game Object on screen
	@Override
	public void draw(RenderWindow mainWindow) {
		// Draw background twice to allow scrolling
		mainWindow.draw(sprite);
		sprite.move(rectangleWidth, 0.0f);
		mainWindow.draw(sprite);
		sprite.move(-rectangleWidth, 0.0f);
	}
	

}
