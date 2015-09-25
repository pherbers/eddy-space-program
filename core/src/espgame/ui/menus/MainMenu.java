package espgame.ui.menus;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import espgame.ESPGame;
import espgame.entity.Eddy;
import espgame.resources.AssetContainer;
import espgame.resources.AssetLoader;

import java.util.Random;

/**
 * Created by Patrick on 09.09.2015.
 */
public class MainMenu extends ESPMenu {

    @Override
    public void init() {
        TextButton btnStartGame = new TextButton("Start Gaem", skin);
        btnStartGame.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ESPGame.game.newGame();
            }
        });
        TextButton btnSettings = new TextButton("Settings", skin);
        btnSettings.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ESPGame.game.newGame();
            }
        });
        TextButton btnHighscores = new TextButton("Highscores", skin);
        btnHighscores.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ESPGame.game.newGame();
            }
        });
        TextButton btnCredits = new TextButton("Credits", skin);
        btnCredits.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ESPGame.game.newGame();
            }
        });
        TextButton btnExit = new TextButton("Exit", skin);
        btnExit.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ESPGame.game.newGame();
            }
        });
        Table buttonGroup = new Table();
        buttonGroup.add(btnStartGame).width(300).height(50).right().top().padRight(100).padTop(25);
        buttonGroup.row();
        buttonGroup.add(btnSettings).width(300).height(50).right().top().padRight(100).padTop(25);
        buttonGroup.row();
        buttonGroup.add(btnCredits).width(300).height(50).right().top().padRight(100).padTop(25);
        buttonGroup.row();
        buttonGroup.add(btnHighscores).width(300).height(50).right().top().padRight(100).padTop(25);
        buttonGroup.row();
        buttonGroup.add(btnExit).width(300).height(50).right().top().padRight(100).padTop(25);
        table.add(buttonGroup).expand().right().top().padRight(100).padTop(100);
        backgroundGroup.addActor(new EddyActor());
    }

    public class EddyActor extends Actor {
        Sprite eddy;
        float f = 0f;
        boolean animplaying = true, moveup = true;
        public EddyActor() {
            eddy = new Sprite(AssetLoader.get().getTexture(AssetContainer.MENU_EDDY));
            eddy.setX(-700);
        }

        @Override
        public void act(float delta) {
            super.act(delta);

            if(animplaying) {
                eddy.setX(eddy.getX() + 100 * delta);
                eddy.setY(Math.abs(MathUtils.sin(eddy.getX() * 0.035f) * 32) - 32);
                if (eddy.getX() >= 90)
                    animplaying = false;

            } else {
                f += delta;
                if(f > 0.5f) {
                    f = 0;
                    float winkel = new Random().nextFloat() * 1.3f;
                    Vector2 vel = new Vector2(MathUtils.cos(winkel), MathUtils.sin(winkel));
                    vel.scl(300);
                    backgroundGroup.addActor(new EddyParticle(new Vector2(700, 340), vel));
                }
            }
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            super.draw(batch, parentAlpha);
            eddy.draw(batch);
        }


    }

    static int lastColor;

    public class EddyParticle extends Actor {
        Sprite sprite;
        Vector2 pos, vel;
        float rotation;
        public EddyParticle(Vector2 pos, Vector2 vel) {
            Random r = new Random();
            this.pos = pos;
            this.vel = vel;
            int col = r.nextInt(3);
            if (col == lastColor)
                col = (col+1) % 3;
            lastColor = col;
            boolean b = r.nextFloat() < 0.85;
            String asset = "";
            if(col == 0)
                asset = b ? AssetContainer.EDDY_ROT : AssetContainer.EDDY_MAGENTA;
            else if(col == 1)
                asset = b ? AssetContainer.EDDY_BLAU : AssetContainer.EDDY_CYAN;
            else if(col == 2)
                asset = b ? AssetContainer.EDDY_GRUEN : AssetContainer.EDDY_GELB;
            this.sprite = new Sprite(AssetLoader.get().getTexture(asset));
            sprite.setSize(64, 64);
            sprite.setOriginCenter();
            sprite.setCenter(pos.x, pos.y);
            rotation = new Random().nextFloat() * 400 - 200;
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            vel.add(0, -200 * delta);
            pos.add(vel.x * delta, vel.y * delta);
            sprite.setCenter(pos.x, pos.y);
            sprite.rotate(rotation * delta);
            if(pos.y < -100)
                remove();
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            super.draw(batch, parentAlpha);
            sprite.draw(batch);
        }
    }

}
