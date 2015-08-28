package espgame.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import espgame.ESPGame;
import espgame.entity.*;
import espgame.input.KanoneController;

import java.util.ArrayList;
import java.util.List;

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

	private Planet planet;

	private Kanone kanone;
	private KanoneController kanonec;
	private InputMultiplexer input;

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
		this.planet = planet;
		entities.add(planet);
		Eddy e = new Eddy(300,0,0,4.7f,Eddy.Color.ROT, planet.getOrbit());
		e.setState(1);
		entities.add(e);

		this.kanone = new Kanone(0);
		entities.add(kanone);
		this.kanonec = new KanoneController(kanone);

		this.input = new InputMultiplexer();
		input.addProcessor(kanonec);

		Gdx.input.setInputProcessor(input);

	}

	@Override
	public void render(float delta) {
		deltaTime += delta;
		if (deltaTime > UPDATE_TIME) {
			update();
			deltaTime -= UPDATE_TIME;
		}

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.position.set(0, 0, 0);
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		
		game.batch.begin();
		for(int i = 0;i<entities.size();i++){
			Entity e = entities.get(i);
			e.render(game.batch);
		}
		game.batch.end();
	}

	public void update() {
		for(int i = 0;i<entities.size();i++){
			Entity e = entities.get(i);
			e.update();
		}
	}

	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(false, width, height);
		camera.update();
	}

	@Override
	public void pause() {
		input.removeProcessor(kanonec);
	}

	@Override
	public void resume() {
		input.addProcessor(kanonec);
	}

	@Override
	public void hide() {
		input.removeProcessor(kanonec);
	}

	@Override
	public void dispose() {
		Gdx.input.setInputProcessor(null);

	}

	public void addEntity(Entity e) {
		entities.add(e);
	}

	public Planet getPlanet() {
		return planet;
	}

	public OrthographicCamera getCamera(){
		return camera;
	}
}
