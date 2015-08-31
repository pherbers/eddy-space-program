package espgame.entity;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import espgame.ESPGame;
import espgame.level.Orbit;
import espgame.resources.AssetContainer;
import espgame.resources.AssetLoader;
import espgame.resources.Fontsize;
import espgame.util.VectorUtils;

public class Schiff extends Entity {

	private final static double ORBIT_VELOCITY = -0.002;
	private final static double ORBIT_VELOCITY_EINSAMMELN = -0.04;

	private Sprite idle;
	private Sprite[] flug;
	// private Animation fluganim; /TODO delete?
	private int state = 0; // 0 = idle, 1 = flug
	private ArrayList<Eddy> eddys;

	private float omega; // Winkel im orbit
	private float distance; // Entfernung vom Mittelpunkt
	private int checkCountdown;
	private double transition;

	public Schiff(float omega, float distance, Orbit orbit) {
		super(0, 0);
		// idle = Sprites.SCHIFF_IDLE;
		// flug = Sprites.SCHIFF_FLUG;
		// fluganim = new Animation(flug, 500);
		this.omega = omega;
		this.distance = distance;
		explodeable = false;
		// idle = new Sprite(new Texture("sprites/misc/schiff_idle.png"));
		idle = new Sprite(AssetLoader.get().getTextureContainer().get(AssetContainer.SHIP_IDLE));

		updatePosition();
	}

	@Override
	public void render(SpriteBatch batch) {
		// // g.drawImage(sprite, (float)position.getX()-sprite.getWidth()/2,
		// // (float)position.getY()-sprite.getHeight()/2);
		// if (Game.DEBUG)
		// g.drawRect((float) position.getX(), (float) position.getY(), (float)
		// 10, (float) 20);
		// // idle.drawCentered((float) getX(), (float) getY());
		// // g.setAntiAlias(true);
		// fluganim.draw((float) position.getX() - fluganim.getWidth() / 2,
		// (float) position.getY() - fluganim.getHeight() / 2);
		// // g.setAntiAlias(true);
		idle.draw(batch);
	}

	@Override
	public void update() {
		float rotation = (float) (omega / Math.PI * 180f) + 90f;
		idle.setRotation(rotation);
		idle.setCenter(position.x, position.y);
		// flug[0].setRotation(rotation);
		// flug[1].setRotation(rotation);
		switch (state) {
		case 0:
			if (transition > 0.0) {
				omega += transition * transition * ORBIT_VELOCITY_EINSAMMELN
						+ 2 * transition * (1 - transition) * -0.005
						+ (1 - transition) * (1 - transition) * ORBIT_VELOCITY;
				transition -= 0.01;
			} else
				omega += ORBIT_VELOCITY;
			updatePosition();
			break;
		case 1:
			if (transition < 1.0) {
				omega += transition * transition * ORBIT_VELOCITY_EINSAMMELN + 2 * transition * (1 - transition) * -0.01
						+ (1 - transition) * (1 - transition) * ORBIT_VELOCITY;
				transition += 0.05;
			} else
				omega += ORBIT_VELOCITY_EINSAMMELN;

			updatePosition();
			for (Eddy e : eddys) {
				if (e.getState() < 3) {
					double eddyOmega = Math
							.acos(e.getPosition().dot(position) / (e.getPosition().len() * position.len()));
					// Game.print(eddyOmega);
					if (eddyOmega < 0.3) { // Wenn der Eddy innerhalb von 0.6
											// radianten threshold kommt
						e.setState(3);
					}
				}
			}
			for (Eddy e : eddys) {
				Vector2 v = new Vector2(position.x - e.getPosition().x, position.y - e.getPosition().y);
				if (v.len() < e.getRadius() && e.getState() == 3) {
					e.setState(4);
					e.disableParticles();
					ESPGame.getLevel().modPunkte(e.getPoints());
					ESPGame.getLevel().createTextDisplayer(position, VectorUtils.up().scl(0.5f), 180, "+" + e.getPoints(), Fontsize.Mittel);
				}
			}
			boolean eingesammelt = true;
			for (Eddy e : eddys) {
				if (e.getState() != 4) {
					eingesammelt = false;
				}
			}
			if (eingesammelt) {
				state = 0;
				for (Eddy e : eddys) {
					e.remove();
				}
				eddys.clear();
				ESPGame.getLevel().nextLevel();
				// sammel.wrapUp();
			}
			break;
		}
		if (checkCountdown > 0)
			checkCountdown--;
		else if (checkCountdown == 0)
			ESPGame.getLevel().checkObjective();
	}

	public void resetCheckCountdown() {
		checkCountdown = 60;
	}

	private void updatePosition() {
		// TODO remove cast to float?
		position.x = (float) (Math.cos(omega) * distance);
		position.y = (float) (Math.sin(omega) * distance);
		// if (sammel != null)
		// sammel.setPosition(getXF(), getYF(), false);
		// TODO particle
	}

	public void einsammeln(ArrayList<Eddy> eddys) {
		this.eddys = eddys;
		for (Eddy e : eddys)
			e.setCollidable(false);
		// try {
		// // sammel = Game.generateEmitter("res/particles/schiffB.xml");
		// // sammel.setImageName(Sprites.CIRCLE.getResourceReference());
		// // Game.getLevel().addParticleEmitter(sammel);
		// // TODO particle
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		state = 1;
		// active = flug;
	}

	public boolean isBusy() {
		if (state == 1) {
			return true;
		}
		return false;
	}
}
