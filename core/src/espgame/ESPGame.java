package espgame;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import espgame.level.Level;
import espgame.mechanics.Highscore;
import espgame.resources.AssetLoader;
import espgame.resources.Einstellungen;
import espgame.resources.FileManager;
import espgame.screens.HighscoreScreen;
import espgame.screens.OptionsScreen;

public class ESPGame extends Game {
	private static Level level;

	public SpriteBatch batch;
	public static ESPGame game;
	private boolean hasLevel;
	private boolean firstTimePlaying;
	private int schwierigkeit;
	private String bufferedPlayerName;
	private Einstellungen bufferedEinstellungen;

	private FileManager fileManager;

	Texture img;

	public ESPGame() {
		super();
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		game = this;

		AssetLoader.get().collect();
		AssetLoader.get().load();

		bindTextures();

		fileManager = new FileManager(this);
		try {
			fileManager.initFiles();
		} catch (IOException e) {
			e.printStackTrace();
			// TODO handle
		}
		loadSettings();

		// TODO start level with difficulty
		newGame();

		// setScreen(new HighscoreScreen());
		 setScreen(new HighscoreScreen(new Highscore(6, 200, "Test", 1, new
		 Date().getTime())));
//		setScreen(new OptionsScreen());
	}

	@Override
	public void render() {
		super.render();
		// Gdx.gl.glClearColor(1, 0, 0, 1);
		// Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// batch.begin();
		// batch.draw(img, 0, 0);
		// batch.end();
	}

	public void update() {
		if (level != null)
			level.update();
		// if (activeMenu != null)
		// activeMenu.update(delta);
		if (!hasLevel && level != null)
			level = null;
		// setTabbedOut(container.hasFocus());
		// if (level != null && !isTabbedOut && !level.isPaused()) {
		// level.togglePause();
		// }
		//
		// if (requestedMenu != activeMenu)
		// setActiveMenu(requestedMenu);
	}

	private void bindTextures() {
		for (Texture t : AssetLoader.get().getTextureContainer()) {
			t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		}
	}

	public Level newGame() {
		if (screen != null)
			screen.dispose();
		levelBeenden();
		setLevel(new Level(getSchwierigkeit(), this));
		// level.init();
		// if (DEBUG)
		// debuginit();
		setScreen(level);
		return level;
	}

	public Level levelBeenden() {
		if (level != null)
			level.onRemove();
		hasLevel = false;
		return level;
	}

	public FileManager getFileManager() {
		return fileManager;
	}

	public static void setLevel(Level level) {
		ESPGame.level = level;
		game.hasLevel = true;
	}

	private void loadSettings() {
		Einstellungen e = null;
		try {
			e = AssetLoader.get().loadEinstellungen();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			e = Einstellungen.getDefaultEinstellungen();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			e = Einstellungen.getDefaultEinstellungen();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			e = Einstellungen.getDefaultEinstellungen();
		}
		setBufferedEinstellungen(e);
	}

	public void toMenu() {
		levelBeenden();
		screen.dispose();
		setScreen(new MainMenu());
	}

	public boolean hasLevel() {
		return hasLevel;
	}

	public static Level getLevel() {
		return level;
	}

	public static float getGamescale() {
		return getLevel().camera.zoom;
	}

	public static int getRenderWidth() {
		return Gdx.graphics.getWidth() / 2;
	}

	public static int getRenderHeight() {
		return Gdx.graphics.getHeight() / 2;
	}

	public static float getRandomOmega() {
		// TODO remove cast?
		return (float) (new Random().nextFloat() * 2f * Math.PI);
	}

	public boolean isFirstTimePlaying() {
		return firstTimePlaying;
	}

	public void setFirstTimePlaying(boolean firstTimePlaying) {
		this.firstTimePlaying = firstTimePlaying;
	}

	public int getSchwierigkeit() {
		return schwierigkeit;
	}

	public void setSchwierigkeit(int schwierigkeit) {
		this.schwierigkeit = schwierigkeit;
		System.out.println("Schwierigkeit geändert: " + schwierigkeit);
		// TODO eddy highlight?
	}

	public String getBufferedPlayerName() {
		return bufferedPlayerName;
	}

	public void setBufferedPlayerName(String bufferedPlayerName) {
		this.bufferedPlayerName = bufferedPlayerName;
	}

	public Einstellungen getBufferedEinstellungen() {
		return bufferedEinstellungen;
	}

	private void setBufferedEinstellungen(Einstellungen bufferedEinstellungen) {
		this.bufferedEinstellungen = bufferedEinstellungen;
	}

}
