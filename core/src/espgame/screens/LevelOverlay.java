package espgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import espgame.level.Level;

public class LevelOverlay extends ScreenAdapter {

    private Level level;
    private Screen menu;
    private int width, height;

    public LevelOverlay(Level level, Screen menu) {
        this.level = level;
        this.menu = menu;
    }

    @Override
    public void show() {
        menu.show();
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        System.out.println(width +" " + height);
        menu.resize(width, height);
    }

    @Override
    public void resize(int width, int height) {
        if(height != this.height && width != this.width) {
            super.resize(width, height);
            level.resize(width, height);
            menu.resize(width, height);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        level.render();
        menu.render(delta);
    }

}
