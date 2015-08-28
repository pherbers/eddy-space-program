package espgame.level;

import java.io.File;
import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import espgame.entity.Entity;

public class Planet extends Entity {

	private Orbit myOrbit;
	private float rotationspeed = 0.25f;

	public Planet(float radius, float orbitRadius, float orbitForce) {
		super(0, 0);
		myOrbit = new Orbit(position, orbitRadius, orbitForce);
		explodeable = false;
		this.radius = radius;
		sprite = new Sprite(randomSprite());
		sprite.setCenter(0, 0);
		sprite.setOriginCenter();
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
		/*
		 * if (p < 0.65) return Sprites.PLANET_ERDE; if (p < 0.7) return
		 * Sprites.PLANET_DEATHSTAR; if (p < 0.75) return Sprites.PLANET_MAJORA;
		 * if (p < 0.8) return Sprites.PLANET_CHEESE;
		 */

		// return Sprites.PLANET_MOON;
		Texture t = new Texture("sprites\\planeten\\moon.png");
		t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		Sprite s = new Sprite(t);
		s.setSize(radius*2, radius*2);
		return s;

	}

	@Override
	public void render(SpriteBatch batch) {
		sprite.draw(batch);
		myOrbit.render();
	}

	@Override
	public void update() {
		rotation += rotationspeed;
		sprite.setRotation(rotation);
	}

	public Orbit getOrbit() {
		return myOrbit;
	}

	public void setOrbit(Orbit orbit) {
		this.myOrbit = orbit;
	}

}
