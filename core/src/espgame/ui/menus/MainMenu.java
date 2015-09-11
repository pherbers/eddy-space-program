package espgame.ui.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import espgame.ESPGame;
import espgame.level.Hintergrund;
import espgame.level.Level;
import espgame.resources.AssetContainer;
import espgame.resources.AssetLoader;

/**
 * Created by Patrick on 09.09.2015.
 */
public class MainMenu extends ESPMenu {

    @Override
    public void init() {
        TextButton btn1 = new TextButton("Start Gaem", skin);
        btn1.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ESPGame.game.newGame();
            }
        });
        table.add(btn1).width(300).height(50).expand().right().top().padRight(100).padTop(100);
    }

}
