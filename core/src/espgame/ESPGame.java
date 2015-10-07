package espgame;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import espgame.level.Level;
import espgame.mechanics.Highscore;
import espgame.resources.AssetContainer;
import espgame.resources.AssetLoader;
import espgame.resources.Einstellungen;
import espgame.resources.FileManager;
import espgame.screens.HighscoreScreen;
import espgame.screens.OptionsScreen;
import espgame.ui.menus.ESPMenu;
import espgame.ui.menus.MainMenu;

public class ESPGame extends Game {
	private static Level level;

	public SpriteBatch batch;
	public static ESPGame game;
	private boolean hasLevel;
	private boolean firstTimePlaying;
	private int schwierigkeit;
	private Einstellungen einstellungen;
	private Music currentMusic;

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
		playNextSong();

		// TODO start level with difficulty
		newGame();

		// setScreen(new HighscoreScreen());
		// setScreen(new HighscoreScreen(new Highscore(6, 200, "Test", 1, new
		// Date().getTime())));
		setScreen(new MainMenu());
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

	public void changeMenu(ESPMenu menu) {
		if (screen != null)
			screen.dispose();
		setScreen(menu);
	}

	@Override
	public void dispose() {
		super.dispose();

		System.out.println("Exit registered.");
		saveSettings();
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
			e = new Einstellungen(fileManager.getOptionsFile());
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
			System.out.println("Gespeicherte Property war nicht im richtigen Format");
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("Generelle IO Exception beim Laden der Settings");
		} catch (NullPointerException e1) {
			System.out.println("Eine Property nicht vorhanden!");
			e1.printStackTrace();
		} // TODO handle exceptions
		finally {
			if (e == null) {
				e = Einstellungen.getDefaultEinstellungen();
				System.out.println("Fehler bei den einstellungen! Erstelle Defaults.");
			}
		}
		
		setSchwierigkeit(e.getSchwierigkeit());
		setEinstellungen(e);
	}

	public void saveSettings() {
		try {
			einstellungen.save(fileManager.getOptionsFile());
			System.out.println("Einstellungen erfolgreich gespeichert.");
		} catch (IOException e) {
			e.printStackTrace();
			// TODO handle exception
		}
	}

	public void toMenu() {
		levelBeenden();
		screen.dispose();
		setScreen(new MainMenu());
	}

	public void playMusic(Music music) {
		if (currentMusic != null) {
			currentMusic.stop();
		}
		music.play();
		music.setLooping(false);
		music.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(Music music) {
				System.out.println("Music ended detected!");
				playNextSong();
			}
		});

		music.setVolume(getEinstellungen().getMusicVolume());
		if (getEinstellungen().isMusicMute()) {
			music.setVolume(0);
		}
		currentMusic = music;
	}

	public void playNextSong() {
		Music m = AssetLoader.get().getMusic(AssetContainer.MUSIC);
		playMusic(m);
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

	public Einstellungen getEinstellungen() {
		return einstellungen;
	}

	private void setEinstellungen(Einstellungen einstellungen) {
		this.einstellungen = einstellungen;
	}

	public Music getMusicPlaying() {
		return currentMusic;
	}

	public float getSoundVolume() {
		Einstellungen e = getEinstellungen();
		if (e.isSoundMute())
			return 0;
		return e.getSoundVolume();
	}
}
