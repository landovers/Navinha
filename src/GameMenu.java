import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Map;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;


public class GameMenu {
	
	Font font = new Font();
	Map<Text, Vector2f> texts = new HashMap<Text, Vector2f>();
	
	GameMenu() {
		try {
			font.loadFromFile(FileSystems.getDefault().getPath("fonts", "visitor1.ttf"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Text t1 = new Text();
		Text t2 = new Text();
		t1.setString("New Game");
		t2.setString("Options");
		texts.put(t1, new Vector2f(120.0f, 300.0f));
		texts.put(t2, new Vector2f(120.0f, 350.0f));
		for (Text t : texts.keySet()) {
			t.setFont(font);
			t.setCharacterSize(32);
			t.setPosition(texts.get(t));;
			t.setColor(Color.WHITE);
		}
		
	}

	public void showMenu(RenderWindow mainWindow) {
		mainWindow.clear(Color.BLACK);
		for (Text t : texts.keySet()) {
			mainWindow.draw(t);
		}
		
	}

	public boolean newGame() {
		if(Keyboard.isKeyPressed(Keyboard.Key.K))
			return true;
		return false;
	}
}
