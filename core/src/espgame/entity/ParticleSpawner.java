package espgame.entity;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import espgame.ESPGame;

public class ParticleSpawner extends Entity{

	private ParticleEffect effect;
	
	public ParticleSpawner(Vector2 position, Vector2 velocity, ParticleEffect effect, int duration) {
		super(position.x, position.y);
		setVelocity(velocity);
		setlifespan(duration);
		
		this.effect = effect;
		disableInterruptions();
		
		effect.start();
		ESPGame.getLevel().addParticleSystem(effect);
	}

	@Override
	public void render(SpriteBatch batch) {
	}

	@Override
	public void update() {
		position.add(velocity);
		
		effect.setPosition(position.x, position.y);
		ticklifespan();
	}
	
	@Override
	public void onRemove() {
		super.onRemove();
		effect.setDuration(0);
	}

}
