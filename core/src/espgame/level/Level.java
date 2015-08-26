package espgame.level;

import com.badlogic.gdx.Screen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrick on 26.08.2015.
 */
public class Level implements Screen{

    private float deltaTime;

    private static final float UPDATE_TIME = 1f / 60f;

    private static final float PLANET_SIZE = 128;
    private static final float PLANET_ORBIT_RADIUS = 400;
    private static final float PLANET_ORBIT_FORCE = 1;

    private List<Entity> entities;

    @Override
    public void show() {
        entities = new ArrayList<>();
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
    }

    public void update() {

    }

    @Override
    public void resize(int width, int height) {

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
