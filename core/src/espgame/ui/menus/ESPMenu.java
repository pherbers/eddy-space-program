package espgame.ui.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import espgame.ESPGame;
import espgame.level.Hintergrund;
import espgame.level.Level;
import espgame.resources.AssetLoader;

/**
 * Created by Patrick on 11.09.2015.
 */
public abstract class ESPMenu implements Screen {
	
	protected static final float STAR_PERCENTAGE = 0.0001f;

	public Stage stage;
	public Table table;
	public Hintergrund hintergrund;
	private ExtendViewport viewport;
	private OrthographicCamera camera;
	protected Skin skin;
	private float starPercentage;

	public ESPMenu(float starPercentage) {
		this.starPercentage = starPercentage;
	}

	public ESPMenu() {
		this(Level.STAR_PERCENTAGE);
	}

	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(1920, 1080, camera);
		stage.setViewport(viewport);

		table = new Table();
		table.setFillParent(true);
		hintergrund = new Hintergrund(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), starPercentage);

		Actor hintergrundActor = new Actor() {
			@Override
			public void draw(Batch batch, float parentAlpha) {
				super.draw(batch, parentAlpha);
				hintergrund.renderMenu(batch);
			}
		};
		stage.addActor(hintergrundActor);
		stage.addActor(table);
		skin = AssetLoader.get().getSkin();
		init();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		viewport.setMinWorldWidth(MathUtils.clamp(width, Level.MIN_WORLD_WIDTH, Level.MAX_WORLD_WIDTH));
		viewport.setMinWorldHeight(MathUtils.clamp(height, Level.MIN_WORLD_HEIGHT, Level.MAX_WORLD_HEIGHT));
		camera.setToOrtho(false, width, height);
		camera.position.set(0, 0, 0);
		camera.update();
		stage.getViewport().update(width, height, true);
		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		hintergrund.resize((int) viewport.getWorldWidth(), (int) viewport.getWorldHeight());
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
	
	public abstract void init();

}
