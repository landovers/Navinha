
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.Image;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.window.event.Event;


public abstract class GameObject implements Comparable<GameObject>{
	// TODO: Mark as private where possible
	protected Sprite sprite;
	protected Texture texture;
	protected Texture hitBoxTexture;
	protected Image image;
	protected Image hbImage;
	protected int currentAnimFrame;
	protected int currentAnimIndex;
	protected int drawPriority;
	protected float frameTimer;
	protected float stateTimer;
	protected Boolean isCollidible = true;
	
	protected Boolean isMovable = true;
	protected Boolean markedForDeletion = false;
	protected List<GameObject> childObjects = new ArrayList<GameObject>();
	
	protected float speed; 
	protected float animSpeed;
	protected int rectangleHeight; 
	protected int rectangleWidth;
	protected static final float FRAME_TIME = 10.0f; // Frame Length: Same value for all classes 
	
	// Are states similar for all GObs? Should this enum be constants from each class? 
	public enum GameObjectState { NORMAL, COLLIDED, DEAD, RESPAWNING }
	protected GameObjectState objectState;
	// Collision groups
	public enum CollisionGroup { ENEMY, PLAYER, HUD, FG01, FG02, BG, ENEMYSHOT }
	protected ArrayList<CollisionGroup> gameObjectCollisionGroups = new ArrayList<>();
	protected CollisionGroup ownCollisionGroup;

	public GameObject(String folderName, String fileName, String hitBoxFileName) {
		setObjectState(GameObjectState.NORMAL);
		// Initialize image
				image = new Image();
				hbImage = new Image();
				try {
					// TODO: Read file paths from file for Player & Enemies?
					Path path = FileSystems.getDefault().getPath(folderName, fileName);
					image.loadFromFile(path);
					path = FileSystems.getDefault().getPath(folderName, hitBoxFileName);
					hbImage.loadFromFile(path);
					// Set this color as transparent (white)
					image.createMaskFromColor(Color.WHITE, 0);
					hbImage.createMaskFromColor(Color.WHITE, 0);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				// Initialize texture
				// |TODO: search one of the already loaded textures (levelLoader) stored in a Map
				texture = new Texture();
				hitBoxTexture = new Texture();
				
				try {
					texture.loadFromImage(image);
					hitBoxTexture.loadFromImage(hbImage);
				} catch (TextureCreationException e) {
					e.printStackTrace();
				}
				
				// Initialize sprite
				sprite = new Sprite();
				sprite.setTexture(texture);
	}

	public Boolean getIsCollidible() {
		return isCollidible;
	}
	public boolean getIsCollidible(GameObject gob2) {
		if(this.gameObjectCollisionGroups.contains(gob2.ownCollisionGroup)) {
			return true;
		}
		return false;
	}
	public float getStateTimer() {
		return stateTimer;
	}
	public void setStateTimer(float stateTimer) {
		this.stateTimer = stateTimer;
	}
	public Sprite getSprite() {
		return sprite;
	}
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
	public List<GameObject> getChildObjects() {
		return childObjects;
	}
	public void addChildObject(GameObject childObject) {
		this.childObjects.add(childObject);
	}
	public Boolean getMarkedForDeletion() {
		return markedForDeletion;
	}
	public void setMarkedForDeletion(Boolean markedForDeletion) {
		this.markedForDeletion = markedForDeletion;
	}
	public GameObjectState getObjectState() {
		return objectState;
	}
	public void setObjectState(GameObjectState objectState) {
		this.objectState = objectState;
	}
	public Texture getTexture() {
		return texture;
	}
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	public Texture getHitBoxTexture() {
		return hitBoxTexture;
	}
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	
	// Draw game Object on screen
	public void draw(RenderWindow mainWindow) {
		mainWindow.draw(getSprite());
	}
	
	// Run animation based on Object animation parameters
	public void animate() {
		Boolean frameChange = animationWork();
		if(frameChange) { // If frame changes
			stateTimer++; // State counter updates
		}
	}
	
	@Override 
	public int compareTo(GameObject gob) {
		return (gob.drawPriority - this.drawPriority);
	}
	
	// Perform animation calculations. If animation frame changes, returns true
	public Boolean animationWork() {
		int spriteAnimFramesNumber = (sprite.getTexture().getSize().x / rectangleWidth);
		
		// CurrentAnimFrame controls x (current frame); CurrentAnimIndex controls Y (state)
		// IntRect parameters are top left X, top left Y, width, height
		sprite.setTextureRect(new IntRect(currentAnimFrame*rectangleWidth, currentAnimIndex*rectangleHeight, 
											  rectangleWidth, rectangleHeight));
		// Update frame counter
		frameTimer = frameTimer + animSpeed*Game.elapsedTime;
		// If frameTimer if big enough, change frame
		if(frameTimer > FRAME_TIME) {
			frameTimer = 0;
			currentAnimFrame++;
			// If we are at the last frame, bring it back to zero
			if(currentAnimFrame >= spriteAnimFramesNumber)
				currentAnimFrame = 0;
			return true; // Frame has changed
		}
		return false; // Frame has not changed
	}
	public boolean outOfBounds() {
		if((sprite.getPosition().x < 0) || (sprite.getPosition().x > Game.SCREEN_WIDTH)
				|| (sprite.getPosition().y < 0) || (sprite.getPosition().y > Game.SCREEN_HEIGHT)) {
			return true;
		}
			return false;
	}
	public boolean outOfBoundsTotally() {
		if((sprite.getPosition().x < 0 - rectangleWidth) || (sprite.getPosition().x > Game.SCREEN_WIDTH + rectangleWidth)
				|| (sprite.getPosition().y < 0 - rectangleHeight) || (sprite.getPosition().y > Game.SCREEN_HEIGHT + rectangleHeight)) {
			return true;
		}
			return false;
	}
	//TODO: Should be implemented interface instead of abstract method? 
	// Abstract methods
	public abstract void update(Event currentEvent);
	public abstract void handleCollision(GameObject collidedObject, FloatRect intersect);
	public abstract void updateObjectState();

	
}
