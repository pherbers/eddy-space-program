package espgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import espgame.level.Level;


public class ESPGame extends Game {
	
	private static Level level;
	
	public SpriteBatch batch;
	Texture img;
	
	public ESPGame() {
		super();
	}
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		level = new Level(this);
		setScreen(level);
	}

	@Override
	public void render () {
		super.render();
//		Gdx.gl.glClearColor(1, 0, 0, 1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		batch.begin();
//		batch.draw(img, 0, 0);
//		batch.end();
	}
	
	public void update() {
		if (level != null)
			level.update();
		// if (activeMenu != null)
		// activeMenu.update(delta);
		// if (!hasLevel && level != null)
		// level = null;
		// setTabbedOut(container.hasFocus());
		// if (level != null && !isTabbedOut && !level.isPaused()) {
		// level.togglePause();
		// }
		//
		// if (requestedMenu != activeMenu)
		// setActiveMenu(requestedMenu);
	}

	public static Level getLevel() {
		return level;
	}
}
