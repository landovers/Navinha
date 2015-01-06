import java.util.HashMap;
import java.util.Map;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Clock;
import org.jsfml.window.Keyboard;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;


public class Game {

	// Screen dimension constants
	// TODO: Read from file!!!
	static final int SCREEN_WIDTH = 640;
	static final int SCREEN_HEIGHT = 480;
	static final int BITS_PER_PIXEL = 32;
	
	// Stores game state
	public enum GameState { UNINITIALIZED, SHOWINGSPLASH, PAUSED, STARTING,
		SHOWINGMENU, PLAYING, EXITING, LOADING, SHOWINGGAMEOVER }
	
	GameState gameState;
	RenderWindow mainWindow;
	GameObjectManager gameObjectManager;
	AssetsLoader assetsLoader;
	//PlayerShip playerShip;
	Clock clock;
	//EnemyFactory enemyFactory;
	//ForegroundFactory foregroundFactory;
	GameObjectFactory permanentFactory;
	GameObjectFactory enemyFactory;
	GameObjectFactory foregroundFactory;
	public static float elapsedTime;
	Map<String, Sprite> loadedAssets = new HashMap<String, Sprite>();
	GameMenu gameMenu;
	
	// Game Constructor
	Game() {
		
	}
	
	// Game Start: organize and call state machine
	public void start() {
		
		clearGame(GameState.UNINITIALIZED);
		
		if(!gameState.equals(GameState.UNINITIALIZED)) // Initialize only once
			return;
	
		gameState = GameState.STARTING;
		
		while(!gameState.equals(GameState.EXITING)) {
			gameStateMachine();
		}
		mainWindow.close();
	}

	private void clearGame(GameState state) {
		gameState = state; // Set initial state upon construction
		// Instantiate important members
		//clock = new Clock();
		gameObjectManager = new GameObjectManager();
		if(mainWindow == null)
			mainWindow = new RenderWindow();
		permanentFactory = new GameObjectFactory("permanent.txt");
		enemyFactory = new GameObjectFactory("enemy.txt");
		foregroundFactory = new GameObjectFactory("foreground.txt");
		assetsLoader = new AssetsLoader();
		
	}

	private void gameStateMachine() {
		Event currentEvent = mainWindow.pollEvent();
		// State machine to control Game States and what they do
		switch(gameState)
		{
			// STARTING: Create Window and initial objects
			case STARTING:
				mainWindow.create(new VideoMode(SCREEN_WIDTH, SCREEN_HEIGHT, BITS_PER_PIXEL), 
						"Jogo");
				
				// TODO: this should not be here, probably in game loop
				// Initialize GOM with stuff always on screen
				// Add Player, PlayerShot and Background to game (order matters)
				// TODO: replace this with file read and get into GameObjectFactory
				// playerandBGFactory.add
				// TODO: fix this!
				// loadedAssets = levelLoader.loadAssets(gameObjectManager.collisionController);
				gameState = GameState.SHOWINGMENU;
				break;
				
			case SHOWINGMENU:
				showMenu();
				if(gameMenu.newGame()) {
					gameState = GameState.PLAYING;
				}
				break;
			// PLAYING: call main gameplay loop		
			case PLAYING:
				gamePlayLoop(currentEvent); // In play game loop
				// TODO: merge with checkstateevent()
				if(PlayerShip.getLives() <= 0) {
					// Restart game
				}
				break;
			default:
				break;
		}
	
	}

	private void showMenu() {
		mainWindow.clear(Color.BLACK);
		if(gameMenu == null)
			gameMenu = new GameMenu();
		gameMenu.showMenu(mainWindow);
		mainWindow.display();
		
	}

	private void gamePlayLoop(Event currentEvent) {
		if(clock == null)
			clock = new Clock();
		
		elapsedTime = clock.restart().asSeconds(); // Clock yelds time in ms
		mainWindow.clear(Color.BLACK); // Clear window to black
		
		// Actions to do with all GameObjects 
		gameObjectManager.addObjects(permanentFactory); // Add new permanent objects to list of GameObjects
		gameObjectManager.addObjects(enemyFactory); // Add new enemies to list of GameObjects
		gameObjectManager.addObjects(foregroundFactory); // Add new FGs to list of GameObjects
		gameObjectManager.checkCollisionAll(); // Check all objects for collisions
		gameObjectManager.updateAll(currentEvent); // Update object positions and status
		gameObjectManager.animateAll(); // Animate all objects
		gameObjectManager.drawAll(mainWindow); // Draw all drawable objects
		// TODO: Mark enemies and FGs long passed to Delete, even if not dead!
		gameObjectManager.deleteMarked(); // Clear objects marked for Deletion
		gameObjectManager.sortObjects(); // Sort objects to draw them in correct priority order
		//TODO: add background
		
		mainWindow.display(); // Display window with everything drawn onto it
		checkStateEvent(currentEvent); // Check to see if state changes
	}
	
	// Check to see if an event to exit the game arrives
	private void checkStateEvent(Event currentEvent) {
		if(currentEvent != null) { // Check if event to close occurred	
			if(currentEvent.type.equals(Event.Type.CLOSED)) {
				gameState = GameState.EXITING;
			}
			if(currentEvent.type.equals(Event.Type.KEY_PRESSED)) {
				if(currentEvent.asKeyEvent().key.equals(Keyboard.Key.ESCAPE) || 
					currentEvent.asKeyEvent().key.equals(Keyboard.Key.RETURN)) {
					gameState = GameState.EXITING;
				}
			}
		}
		
	}
	
}
