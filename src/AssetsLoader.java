import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Image;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.TextureCreationException;


public class AssetsLoader {

	BufferedReader fileReader;
	private int currentLevel;
	private final Path path = FileSystems.getDefault().getPath("data", "levels.txt");
	
	public Map<String, Sprite> loadAssets(BitMaskCollisionController collisionController) {
		Map<String, Sprite> assetsLoaded = new HashMap<String, Sprite>();
		String fileLine = null;
		try {
			fileReader = new BufferedReader(new FileReader(path.toString()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			// Skip to correct line
			for(int i = 0; i <= currentLevel; ++i) {
				fileLine = fileReader.readLine();
			}
			// If line not null, read and parse into tokens
			if ((fileLine != null) && !(fileLine.isEmpty())) {
				String[] fileLineTokens = fileLine.split(" "); // Divide line into tokens
				for(String s : fileLineTokens) {
				    System.out.println(s);
				    // Initialize image
					Image image = new Image();
					try {
						// TODO: Read file paths from file for Player & Enemies?
						Path path = FileSystems.getDefault().getPath("images", s);
						image.loadFromFile(path);
						// Set this color as transparent (WHITE)
						// TODO: Change transparent color to (255,0,255)
						image.createMaskFromColor(Color.WHITE, 0);
					} catch (IOException e) {
						e.printStackTrace();
					}
					// Initialize texture
					// |TODO: search one of the already loaded textures (levelLoader) stored in a Map
					Texture texture = new Texture();
					try {
						texture.loadFromImage(image);
					} catch (TextureCreationException e) {
						e.printStackTrace();
					}
					// Initialize sprite
					Sprite sprite = new Sprite();
					sprite.setTexture(texture);
					assetsLoaded.put(s, sprite);
					// Generate BitMask
					collisionController.Bitmasks.GetMask(sprite.getTexture());
				}
				currentLevel++; // Next time load next level	
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return assetsLoaded;
	}
}
