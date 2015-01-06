import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import org.jsfml.window.event.Event;


public class HUD extends GameObject {

	Font hudFont = new Font();
	Text hudText = new Text();
	String hudTextString = new String();
	
	public HUD(ArrayList<String> gobLineData) {
		super("images", gobLineData.get(5), gobLineData.get(6));
		try {
			hudFont.loadFromFile(FileSystems.getDefault().getPath("fonts", "visitor1.ttf"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		drawPriority = 0;
		isCollidible = false;
		rectangleHeight = 16; // TODO: read all this from permanent.txt file
		rectangleWidth = 16;
		hudText.setFont(hudFont);
		hudText.setPosition(Float.parseFloat(gobLineData.get(2)), Float.parseFloat(gobLineData.get(3)));
		// TODO: Read this info from file or put it on concrete file HUDType (each type has different values)
		hudText.setString("LIVES: " + PlayerShip.getLives());
		hudText.setCharacterSize(32);
		hudText.setColor(Color.WHITE);
	}

	@Override
	public void update(Event currentEvent) {
		hudText.setString("LIVES: " + PlayerShip.getLives());
	}

	@Override
	public void handleCollision(GameObject collidedObject, FloatRect intersect) {

	}
	
	@Override 
	public void draw(RenderWindow mainWindow) {
		//mainWindow.draw(getSprite());
		mainWindow.draw(hudText);
	}

	@Override
	public void updateObjectState() {

	}

}
