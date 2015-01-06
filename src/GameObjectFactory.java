import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;

public class GameObjectFactory {
	
	BufferedReader fileReader;
	private float gameObjectTimer;
	private int currentFileLine;
	private final int SPAWN_TIME = 0;
	private final int GOB_TYPE = 1;
	
	private final Path path;
	
	public GameObjectFactory(String dataFileName) {
		path = FileSystems.getDefault().getPath("data", dataFileName);
	}
	
	public ArrayList<GameObject> makeGameObject() {
		// ForegroundFactory reads file, instantiates foreground, loads correct file.png.
		
		ArrayList<GameObject> gameObjects = new ArrayList<GameObject>();
		ArrayList<String> gobLineData = new ArrayList<String>();
		String fileLine = null;
		
		gameObjectTimer += Game.elapsedTime;
		
		try {
			fileReader = new BufferedReader(new FileReader(path.toString()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			// Read specific line
			for(int i = 0; i <= currentFileLine; ++i) 
				fileLine = fileReader.readLine();
			// If line not null, read and parse into tokens
//			if (((fileLine = fileReader.readLine()) != null)) {
			if ((fileLine != null) && !(fileLine.isEmpty())) {
				String[] fileLineTokens = fileLine.split(" "); // Divide line into tokens
				for(String s : fileLineTokens) // Convert simple array to ArrayList
				    gobLineData.add(s);
				// If time is right, spawn enemy
				if(gameObjectTimer >= Float.parseFloat(gobLineData.get(SPAWN_TIME))) { 
					// TODO: Pass this processing to a file
					GameObject gob = null;
					switch (gobLineData.get(GOB_TYPE)) {
					case "ENEMY":
						gob = new Enemy(gobLineData);
						break;
					case "FG":
						gob = new Foreground(gobLineData);
						break;
					case "BG":
						// TODO: put this in as read from file
						gob = new Background(gobLineData);
						break;
					case "PLAYER":
						// TODO: put this in as read from file
						gob = new PlayerShip(gobLineData);
						break;
					case "HUD":
						// TODO: put this in as read from file
						gob = new HUD(gobLineData);
						break;
					default:
						break;
					}
					if(gob != null)
						gameObjects.add(gob);
					currentFileLine++; // Next time go to next line
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return gameObjects;
	}
}
