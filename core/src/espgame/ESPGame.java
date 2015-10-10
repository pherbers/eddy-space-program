package espgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import espgame.level.Level;
import espgame.resources.AssetContainer;
import espgame.resources.AssetLoader;
import espgame.resources.Einstellungen;
import espgame.resources.FileManager;
import espgame.screens.LoadingScreen;
import espgame.screens.ESPMenu;
import espgame.screens.MainMenu;

import java.io.IOException;
import java.util.Random;

public class ESPGame extends Game {
	public static final String PROJECT_TITLE = "Eddy Space Program";
	public static final String PROJECT_VERSION = "Version 1.1 [Early Acces 1]";

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

		fileManager = new FileManager(this);
		try {
			fileManager.initFiles();
		} catch (IOException e) {
			e.printStackTrace();
			// TODO handle
		}

		setScreen(new LoadingScreen() {
			@Override
			public void everythingLoaded() {
				loadSettings();
				playNextSong();
				System.out.println("Everything loaded.");

				changeMenu(new MainMenu());
			}
		});
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
		if (einstellungen == null) {
			System.err.println("Wollte die Einstellungen speichern. Aber diese existieren nicht.");
			return;
		}
		try {
			boolean b = einstellungen.save(fileManager.getOptionsFile());
			System.out.println("Einstellungen gespeichert. Erfolg: " + b);
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
		currentMusic = music;
		updateMusicVolume();
	}

	public void playNextSong() {
		Music m = AssetLoader.get().getMusic(AssetContainer.MUSIC);
		playMusic(m);
	}

	public void updateMusicVolume() {
		// System.out.println("Updating Music Volume. Vol:
		// "+getEinstellungen().getMusicVolume()+" Mute:
		// "+getEinstellungen().isMusicMute());
		Music music = getMusicPlaying();
		music.setVolume(getEinstellungen().getMusicVolume());
		if (getEinstellungen().isMusicMute()) {
			music.setVolume(0);
		}
	}

	public static void setFullScreen(boolean fullscreen) {
		Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width,
				Gdx.graphics.getDesktopDisplayMode().height, fullscreen);
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
