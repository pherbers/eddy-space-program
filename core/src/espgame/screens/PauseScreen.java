package espgame.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import espgame.ESPGame;
import espgame.level.Level;
import espgame.resources.AssetContainer;
import espgame.resources.AssetLoader;

public class PauseScreen extends ScreenAdapter {

    public Stage stage;
    public Table table;
    private ExtendViewport viewport;
    private OrthographicCamera camera;
    protected Skin skin;
    private LevelOverlay overlay;
    private Button optionsBtn;

    public PauseScreen(){};

    public PauseScreen(LevelOverlay overlay) {
        this.overlay = overlay;
    }

    @Override
    public void show() {
        stage = new Stage();
        InputMultiplexer input = new InputMultiplexer();
        input.addProcessor(stage);
        Gdx.input.setInputProcessor(input);
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
        input.addProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    ESPGame.getLevel().disablePause();
                    System.out.println("Pause");
                    return true;
                }
                return false;
            }
        });
        table = new Table();
        table.setFillParent(true);

        stage.addActor(table);
        skin = AssetLoader.get().getSkin();
        table.row();
        Button weiterBtn = ESPMenu.getImageButton(AssetContainer.WEITER, AssetContainer.WEITER_A);
        weiterBtn.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ESPGame.getLevel().disablePause();
            }
        });
        table.add(weiterBtn).pad(20);
        table.row();
        optionsBtn = ESPMenu.getImageButton(AssetContainer.OPTIONEN, AssetContainer.OPTIONEN_A);
        optionsBtn.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                overlay.setMenu(new OptionsScreen(overlay));
            }
        });
        table.add(optionsBtn).pad(20);
        table.row();
        Button exitBtn = ESPMenu.getImageButton(AssetContainer.MENU, AssetContainer.MENU_A);
        exitBtn.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ESPGame.getLevel().endLevel(true);
            }
        });
        table.add(exitBtn).pad(20);
        table.row();
        table.add(new Image(new TextureRegion(AssetLoader.get().getTexture(AssetContainer.UI_COLORS)))).pad(40);
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

    public void setOverlay(LevelOverlay overlayScreen) {
        this.overlay = overlayScreen;
    }
}
