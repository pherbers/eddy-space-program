package espgame.level;

import java.io.File;
import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import espgame.entity.Entity;
import espgame.resources.AssetContainer;
import espgame.resources.AssetLoader;

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
		String spriteKey;
		if (p < 0.65) spriteKey = AssetContainer.PLANET_VARIANT;
		else if (p < 0.7) spriteKey = AssetContainer.PLANET_SECRET_1;
		else if (p < 0.75) spriteKey = AssetContainer.PLANET_SECRET_2;
		else if (p < 0.8) spriteKey = AssetContainer.PLANET_SECRET_3;
		else spriteKey = AssetContainer.PLANET_MAIN;

		// return Sprites.PLANET_MOON;
		Texture t = AssetLoader.get().getTexture(spriteKey);
		Sprite s = new Sprite(t);
		s.setSize(radius*2, radius*2);
		return s;

	}

	@Override
	public void render(SpriteBatch batch) {
		sprite.draw(batch);
		myOrbit.render(batch);
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
