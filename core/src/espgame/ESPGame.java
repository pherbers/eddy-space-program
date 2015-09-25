package espgame;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import espgame.entity.Entity;
import espgame.level.Level;
import espgame.resources.AssetLoader;
import espgame.ui.menus.MainMenu;

public class ESPGame extends Game {

	private static Level level;

	public SpriteBatch batch;
	public static ESPGame game;
	private boolean hasLevel;
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

		// TODO start level with difficulty
		setScreen(new MainMenu());
		//newGame();
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
		for (Texture t: AssetLoader.get().getTextureContainer()) {
			t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		}
	}

	public Level newGame() {
		screen.dispose();
		levelBeenden();
		setLevel(new Level(0, this));
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

	public static void setLevel(Level level) {
		ESPGame.level = level;
		game.hasLevel = true;
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

}
