package espgame.level;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Planet extends Entity {

    private Orbit myOrbit;

    public Planet(float radius, float orbitRadius, float orbitForce) {
        super(0, 0);
        myOrbit = new Orbit(position, orbitRadius, orbitForce);
        explodeable = false;
        this.radius = radius;
        sprite = new Sprite(randomSprite());
        sprite.scale(((radius * 2) / 512));
    }

    public Planet(float radius, float orbitRadius, float orbitForce, Sprite sprite) {
        super(0, 0);
        myOrbit = new Orbit(position, orbitRadius, orbitForce);
        explodeable = false;
        this.radius = radius;
        this.sprite = sprite;
    }

    private Sprite randomSprite() {
        Random r = new Random();
        double p = r.nextDouble();
        /*if (p < 0.65)
            return Sprites.PLANET_ERDE;
        if (p < 0.7)
            return Sprites.PLANET_DEATHSTAR;
        if (p < 0.75)
            return Sprites.PLANET_MAJORA;
        if (p < 0.8)
            return Sprites.PLANET_CHEESE;*/

        // return Sprites.PLANET_MOON;
        return new Sprite(new Texture("sprites/planeten/moon.png"));

    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(sprite, -radius, -radius);
        myOrbit.render();
    }

    @Override
    public void update() {

    }

    public Orbit getOrbit() {
        return myOrbit;
    }

    public void setOrbit(Orbit orbit) {
        this.myOrbit = orbit;
    }

}
