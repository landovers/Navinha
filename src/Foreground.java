import java.util.ArrayList;

import org.jsfml.graphics.FloatRect;
import org.jsfml.window.event.Event;

public class Foreground extends GameObject {
	
	// Depth of foreground element in screen
	// Depth zero means 
	private int depth; 
	
	public Foreground(ArrayList<String> gobLineData) {
		super("images", gobLineData.get(5), gobLineData.get(6));
		// File reader decides speed, allowing for vertical screen movement
		speed = 50.0f; // Find speed based on depth (parameter read from file)
		animSpeed = 60.0f;
		rectangleHeight = 32; 
		rectangleWidth = 96;
		// TODO: Use a ForegroundType class for stats like EnemyType
		// foregroundType = new FGType(Float.parseFloat(gobLineData.get(4)))
		sprite.setPosition(Float.parseFloat(gobLineData.get(2)), Integer.parseInt(gobLineData.get(3)));
		drawPriority = 3;
		gameObjectCollisionGroups.add(CollisionGroup.PLAYER);
		ownCollisionGroup = CollisionGroup.FG01;
	}
	
	
	public Foreground(String folderName, String fileName, String hbFileName) {
		super(folderName, fileName, hbFileName);
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	@Override
	public void update(Event currentEvent) {
		sprite.move(-(speed * Game.elapsedTime), 0.0f);
	}

	@Override
	public void handleCollision(GameObject collidedObject, FloatRect intersect) {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateObjectState() {
		// TODO Auto-generated method stub
		
	}
}
