package espgame.level;

import java.io.File;
import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import espgame.entity.Entity;

public class Planet extends Entity {

	private Orbit myOrbit;
	private float rotationspeed = 0.025f;

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
		System.out.println(new File("sprites\\planeten\\moon.png").exists());
		return new Sprite(new Texture("sprites\\planeten\\moon.png"));

	}

	@Override
	public void render(SpriteBatch batch) {
		sprite.draw(batch);
		myOrbit.render();
	}

	@Override
	public void update() {
		sprite.rotate(rotationspeed);
	}

	public Orbit getOrbit() {
		return myOrbit;
	}

	public void setOrbit(Orbit orbit) {
		this.myOrbit = orbit;
	}

}
