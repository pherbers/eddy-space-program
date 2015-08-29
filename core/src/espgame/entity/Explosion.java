package espgame.entity;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import espgame.ESPGame;
import espgame.level.Level;
import espgame.util.VectorUtils;

public class Explosion extends Entity {

	private int removedEddyCount = 0;
	private float explosionRadius;
	private boolean active = true;
	private ArrayList<Eddy> destroyList;

	public Explosion(Vector2 position, float explosionRadius, int lifespan) {
		super(position.x, position.y);
		disableInterruptions();

		this.explosionRadius = explosionRadius;
		this.lifespan = lifespan;
		radius = 0;

		destroyList = new ArrayList<Eddy>();

		// TODO Particle here

		// TODO play explosion sound
	}

	@Override
	public void onRemove() {
		if (getRemovedEddyCount() != 0) {
			Vector2 v = VectorUtils.up();
			v.scl(0.3f);

			// TODO display text
		}
	}

	@Override
	public void remove() {
		if (lifespan == 0)
			active = false;
		// if (!active && particlelife < 0)
		// super.remove();
		// TODO patikel entfernen??
	}

	public void removeObjects() {
		Entity e;
		Level l = ESPGame.getLevel();

		for (int i = 0; i < l.getEddyCount(); i++) {
			Eddy eddy = l.getEddy(i);
			destroyList.add(eddy);
		}

		radius = explosionRadius;
		collidable = true;
		for (int i = 0; i < destroyList.size(); i++) {
			e = destroyList.get(i);
			if (e.checkCollision(this)) {
				if (e instanceof Eddy) {
					l.removeEddy((Eddy) e);
					removedEddyCount++;
				}
			}
		}
		radius = 0;
		collidable = false;
		destroyList.clear();
	}

	@Override
	public void render(SpriteBatch batch) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		if (active) {
			removeObjects();
		}
		ticklifespan();
	}

	public boolean isActive() {
		return active;
	}

	public int getRemovedEddyCount() {
		return removedEddyCount;
	}

}
