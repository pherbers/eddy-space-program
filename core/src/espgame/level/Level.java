package espgame.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

import espgame.ESPGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Patrick on 26.08.2015.
 */
public class Level implements Screen {

	private float deltaTime;

	private static final float UPDATE_TIME = 1f / 60f;

	private static final float PLANET_SIZE = 128;
	private static final float PLANET_ORBIT_RADIUS = 400;
	private static final float PLANET_ORBIT_FORCE = 1;

	private List<Entity> entities;
	private final ESPGame game;
	public OrthographicCamera camera;

	public Level(final ESPGame game) {
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		camera.translate(1500, 1500);
	}

	@Override
	public void show() {
		entities = new ArrayList<Entity>();
		Planet planet = new Planet(PLANET_SIZE, PLANET_ORBIT_RADIUS, PLANET_ORBIT_FORCE);
		entities.add(planet);
	}

	@Override
	public void render(float delta) {
		deltaTime += delta;
		if (deltaTime > UPDATE_TIME) {
			update();
			deltaTime -= UPDATE_TIME;
		}

		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.position.set(0,0,0);
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		
		game.batch.begin();
		for(int i = 0;i<entities.size();i++){
			Entity e = entities.get(i);
			e.render(game.batch);
		}
		game.batch.end();

		System.out.println("level renders");
	}

	public void update() {
		System.out.println("level update");
		for(int i = 0;i<entities.size();i++){
			Entity e = entities.get(i);
			e.update();
		}
	}

	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(false, width, height);
		
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}
}
