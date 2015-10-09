package espgame.screens;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import espgame.level.Level;
import espgame.resources.AssetLoader;

public abstract class LoadingScreen implements Screen {

	public static final boolean PARTY = false;
	public static final float DELTA_MAXIMUM = 0.42f;
	public static final float PUNKT_MAXIMUM = 3;

	protected Stage stage;
	protected Table table;
	private ExtendViewport viewport;
	private OrthographicCamera camera;

	private Skin skin;
	private Texture icon;
	private float deltaCounter;
	private int punktCounter;

	private Label loadingLB, progressLB;

	public LoadingScreen() {
		skin = AssetLoader.get().getSkin();
		icon = AssetLoader.get().getLoadingIcon();

		deltaCounter = 0;
		punktCounter = 1;
	}

	public void init() {
		AssetLoader.get().collect();
		AssetLoader.get().prepareLoading();
	}

	private void bindTextures() {
		for (Texture t : AssetLoader.get().getTextureContainer()) {
			t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		}
	}

	public abstract void everythingLoaded();

	@Override
	public void show() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(1920, 1080, camera);
		stage.setViewport(viewport);
		TextureRegionDrawable dr = new TextureRegionDrawable(new TextureRegion(icon));

		table = new Table();
		table.setFillParent(true);
		stage.addActor(table);
		Image i = new Image(dr);
		loadingLB = new Label("", skin);
		table.add(loadingLB).expandY();
		table.row();
		table.add(i);
		table.row();
		progressLB = new Label("", skin);
		table.add(progressLB).expandY();

		init();
	}

	@Override
	public void render(float delta) {
		Random r = new Random();
		if (PARTY) {
			Gdx.gl.glClearColor(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1);
		} else {
			Gdx.gl.glClearColor(0, 0, 0, 1);
		}
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		AssetLoader l = AssetLoader.get();

		deltaCounter += delta;
		if (deltaCounter >= DELTA_MAXIMUM) {
			punktCounter++;
			if (punktCounter > PUNKT_MAXIMUM) {
				punktCounter = 1;
			}
			deltaCounter = 0;
		}
		String s = "Ladevorgang";
		for (int i = 0; i < punktCounter; i++) {
			s += ".";
		}
		loadingLB.setText(s);
		int p = (int) (l.getProgress() * 100);
		progressLB.setText(p + "%");

		if (l.isFinished()) {
			wrapUp();
		}

		stage.act(delta);
		stage.draw();
	}

	private void wrapUp() {
		AssetLoader.get().storeAssets();
		bindTextures();
		everythingLoaded();
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
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
