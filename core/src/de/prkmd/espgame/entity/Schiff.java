package de.prkmd.espgame.entity;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.prkmd.espgame.ESPGame;
import de.prkmd.espgame.level.Level;
import de.prkmd.espgame.level.Orbit;
import de.prkmd.espgame.resources.AssetContainer;
import de.prkmd.espgame.resources.AssetLoader;
import de.prkmd.espgame.resources.Fontsize;
import de.prkmd.espgame.util.VectorUtils;

public class Schiff extends Entity {

	private final static double ORBIT_VELOCITY = -0.002;
	private final static double ORBIT_VELOCITY_EINSAMMELN = -0.04;

	private Sprite schiff;
	private Animation<TextureRegion> fluganim;
	private int state = 0; // 0 = schiff, 1 = flug
	private ArrayList<Eddy> eddys;

	private float omega; // Winkel im orbit
	private float distance; // Entfernung vom Mittelpunkt
	private int checkCountdown;
	private double transition;
	private float stateTime = 0.0f;

	private ParticleEffect engineParticles;

	public Schiff(float omega, float distance, Orbit orbit) {
		super(0, 0);

		Texture shipActive = AssetLoader.get().getTexture(AssetContainer.SHIP_ACTIVE);
		fluganim = new Animation<>(0.375f, new TextureRegion(shipActive, 0, 0, shipActive.getWidth(), shipActive.getHeight() / 2),
				new TextureRegion(shipActive, 0, shipActive.getHeight() / 2, shipActive.getWidth(), shipActive.getHeight() / 2));
		fluganim.setPlayMode(Animation.PlayMode.LOOP);
		this.omega = omega;
		this.distance = distance;
		explodeable = false;
		schiff = new Sprite(AssetLoader.get().getTextureContainer().get(AssetContainer.SHIP_IDLE));

		updatePosition();
	}

	@Override
	public void render(SpriteBatch batch) {
		// // g.drawImage(sprite, (float)position.getX()-sprite.getWidth()/2,
		// // (float)position.getY()-sprite.getHeight()/2);
		// if (Game.DEBUG)
		// g.drawRect((float) position.getX(), (float) position.getY(), (float)
		// 10, (float) 20);
		// // schiff.drawCentered((float) getX(), (float) getY());
		// // g.setAntiAlias(true);
		// fluganim.draw((float) position.getX() - fluganim.getWidth() / 2,
		// (float) position.getY() - fluganim.getHeight() / 2);
		// // g.setAntiAlias(true);
		schiff.draw(batch);
	}

	@Override
	public void update() {
		float rotation = (float) (omega / Math.PI * 180f) + 90f;
		schiff.setRotation(rotation);
		schiff.setCenter(position.x, position.y);
		TextureRegion tr = fluganim.getKeyFrame(stateTime);
		schiff.setRegion(tr);
		stateTime += Level.UPDATE_TIME;
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

			engineParticles.setPosition(position.x, position.y);
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
				engineParticles.setDuration(0);
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
		position.x = MathUtils.cos(omega) * distance;
		position.y = MathUtils.sin(omega) * distance;
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
		engineParticles = new ParticleEffect(ESPGame.getLevel().particleContainer.ship);
		ESPGame.getLevel().addParticleSystem(engineParticles);
		engineParticles.start();
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
