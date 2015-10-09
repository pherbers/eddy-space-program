package espgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import espgame.level.Level;
import espgame.resources.AssetContainer;
import espgame.resources.AssetLoader;
import espgame.ui.menus.ESPMenu;

/**
 * Created by Patrick on 07.10.2015.
 */
public class PauseScreen extends ScreenAdapter {

    public Stage stage;
    public Table table;
    private ExtendViewport viewport;
    private OrthographicCamera camera;
    protected Skin skin;

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(1920, 1080, camera);
        stage.setViewport(viewport);
        stage.addActor(new Actor(){
            Texture overlay = AssetLoader.get().getTexture(AssetContainer.OVERLAY_BLACK);
            @Override
            public void draw(Batch batch, float parentAlpha) {
                super.draw(batch, parentAlpha);
                batch.draw(overlay, 0, 0, stage.getWidth(), stage.getHeight());
            }
        });

        table = new Table();
        table.setFillParent(true);

        stage.addActor(table);
        skin = AssetLoader.get().getSkin();
        table.add(ESPMenu.getImageButton(AssetContainer.BEENDEN, AssetContainer.BEENDEN_A));

    }

    @Override
    public void render(float delta) {
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
    }

}
