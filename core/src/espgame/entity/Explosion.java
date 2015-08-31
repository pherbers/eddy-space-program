package espgame.entity;

import java.util.ArrayList;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import espgame.ESPGame;
import espgame.level.Level;
import espgame.resources.AssetContainer;
import espgame.resources.AssetLoader;
import espgame.resources.Fontsize;
import espgame.util.VectorUtils;

public class Explosion extends Entity {

	public static final int TEXT_DURATION = 220;

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

		Sound explosion = AssetLoader.get().getSound(AssetContainer.SOUND_EXPLOSION);
		explosion.play();
	}

	@Override
	public void onRemove() {
		System.out.println("Explosion entfernt. Removed Count: " + getRemovedEddyCount());
		if (getRemovedEddyCount() != 0) {
			Vector2 v = VectorUtils.up();
			v.scl(0.3f);
			ESPGame.getLevel().createTextDisplayer(position, v, TEXT_DURATION, "-" + getRemovedEddyCount(), Fontsize.Mittel);
		}
	}

	@Override
	public void remove() {
		if (lifespan == 0)
			active = false;
		if (!active) // TODO if active && keine fartikel mehr
			super.remove();
		// TODO patikel entfernen??
	}

	public void removeObjects() {
		Eddy e;
		Level l = ESPGame.getLevel();

		for (int i = 0; i < l.getEddyCount(); i++) {
			Eddy eddy = l.getEddy(i);
			destroyList.add(eddy);
			// eddy.remove();
		}

		radius = explosionRadius;
		collidable = true;
		// System.out.println("destroylist größe: " + destroyList.size());
		for (int i = 0; i < destroyList.size(); i++) {
			e = destroyList.get(i);
			if (e.checkCollision(this)) {
				l.removeEddy(e);
				removedEddyCount++;
				System.out.println("Explosion hat eddy gefunden und entfernt.");

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
