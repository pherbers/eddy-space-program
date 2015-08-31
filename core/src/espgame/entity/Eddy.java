package espgame.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import espgame.ESPGame;
import espgame.level.Orbit;

import java.io.IOException;
import java.util.Random;

/**
 * Created by Patrick on 26.08.2015.
 */

public class Eddy extends Entity {

	private int points, invested, state, tier;

	public enum Color {
		ROT, BLAU, GRUEN, GELB, CYAN, MAGENTA, WEISS, SCHWARZ
	};

	private static final int POITSTIER0[] = { 15, 25, 50 };
	private static final int POITSTIER1[] = { 50, 100, 125 };
	private static final int POITSTIER2[] = { 200, 250, 300 };

	public static final int RADIUS = 24;

	private Orbit orbit;
	private Color farbe;
	private float gravityDrag;
	private static final float BASEGRAVITYDRAG = 20;
	// private ConfigurableEmitter emitter;
	private float c = 0.0f;
	private float rotation;
	private boolean clockwise;
	private boolean highlighted = false;
	public static boolean highlight = false;
	private Color highlightColor;

	public static final float GRAVITYEASY = 1.0f, GRAVITYNORMAL = 0.9999f, GRAVITYHARD = 0.9998f;

	private static float gravity = GRAVITYNORMAL;

	public Eddy(float x, float y, float vx, float vy, Color farbe, Orbit orbit) {
		super(x, y);
		setVelocity(vx, vy);
		this.orbit = orbit;
		state = 0; // 0 = Abschussphase, 1 = Orbitphase, 2 = OutOfOrbit, 3 =
		// Einsammelphase
		this.farbe = farbe;
		// if (farbe == Color.SCHWARZ)
		// gravityDrag = 0;
		// else
		gravityDrag = gravity;
		radius = RADIUS;

		// zufaellige drehung setzen
		Random r = new Random();
		rotation = r.nextFloat();
		rotation -= 0.5f;
		rotation *= 5f;
		// Drehung setzen
		Vector2 v = new Vector2(position.x - orbit.getPosition().x, position.y - orbit.getPosition().y);
		float gamma = MathUtils.atan2(v.y, v.y);
		float theta = MathUtils.atan2(velocity.y * 0.1f + v.y, velocity.x * 0.1f + v.x);
		// Game.print(gamma + "\t" + theta);
		clockwise = v.y * velocity.x < v.x * velocity.y;
		System.out.print(clockwise);
		/*
		 * try { emitter =
		 * Game.generateEmitter("res/particles/EddyParticle.xml"); } catch
		 * (IOException e) { e.printStackTrace(); }
		 */
		setColor();
		// ESPGame.getLevel().addParticleEmitter(emitter);
		if (getColor() == Color.SCHWARZ)
			gravityDrag = 1.0f;

		// Highlight setzen
		Color color;
		// switch (Game.getLevel().getSelectedEddy()) {
		switch (0) {
		case 0:
			color = Color.ROT;
			break;
		case 1:
			color = Color.BLAU;
			break;
		default:
			color = Color.GRUEN;
		}
		Color joined = Eddy.joinColor(getColor(), color);
		if (joined != null && joined != Color.SCHWARZ) {
			setHighlighted(true);
			setHighlightedColor(joined);
		} else
			setHighlighted(false);
	}

	public void render(SpriteBatch batch) {
		if (visible) {
			if (highlight && highlighted) {
				// Sprites.EDDY_HIGHLIGHT.draw((float) (getX() - radius - 8),
				// (float) (getY() - radius - 8),
				// (float) (radius * 2 + 16), (float) (radius * 2 + 16),
				// toSlickColor(highlightColor));
			}
			sprite.draw(batch);
			// g.drawString(state + " " + velocity.getBetrag(), (float)
			// position.getX(), (float) position.getY());
			// if (Game.DEBUG)
			// g.drawLine((float) getX(), (float) getY(), 0, 0);
		}
	}

	public void update() {
		updateScore();
		// emitter.setPosition((float) getX(), (float) getY(), false);
		sprite.rotate(rotation);
		float x = position.x - orbit.getPosition().x;
		float y = position.y - orbit.getPosition().y;
		switch (state) {
		case 0:
			// Vector r = new Vector(-x, -y);
			// Game.print(Vector.Skalar(r, velocity));
			// if(Vector.Skalar(r, velocity) < 0)
			// setState(1);
			// velocity.multiply(1.02);
			Vector2 w = new Vector2(position.x - orbit.getPosition().x, position.y - orbit.getPosition().y);
			w.scl(4.7f / position.dst(orbit.getPosition()));
			if (clockwise)
				w.rotate90(1);
			else
				w.rotate90(-1);
			velocity.set((1 - c) * velocity.x + c * w.x, (1 - c) * velocity.y + c * w.y);
			c += 0.001f;
			if (c >= 0.1f)
				state = 1;
			// if(velocity.getBetrag() > 4.7)
			// state = 1;

		case 1:
			float entfernung = position.dst(orbit.getPosition());
			// position = position.add(velocity);
			if (entfernung > orbit.getRadius()) {
				setState(2);
				break;
			}

			// Drehen des v-Vektors, sodass er orthogonal zum radius ist
			/*
			 * double vBetrag = velocity.getBetrag(); double alpha = Math.atan(y
			 * / x); // winkel im orbit Game.print(x + "\t" + y + "\t" + alpha);
			 * velocity.setX(Math.sin(alpha - gravityDrag) * vBetrag);
			 * velocity.setY(Math.cos(alpha - gravityDrag) * vBetrag);
			 */

			Vector2 v = new Vector2(x / (entfernung * entfernung) * -BASEGRAVITYDRAG,
					y / (entfernung * entfernung) * -BASEGRAVITYDRAG);
			velocity = velocity.add(v);
			position = position.add(velocity);
			velocity.scl(gravityDrag);
			// System.out.println(velocity.len());
			break;
		case 2:
			position = position.add(velocity);
			float entf = position.dst(orbit.getPosition());
			if (entf < orbit.getRadius()) {
				setState(1);
			} else if (entf > 2000 && isCollidable()) {
				// TODO: ESPGame.getLevel().removeEddy(this);
			}
			break;
		case 3: // TODO: Einsammeln
			/*
			 * Vector2 schiffpos = ESPGame.getLevel().getSchiff().position;
			 * velocity.set((schiffpos.getX() - position.getX()),
			 * (schiffpos.getY() - position.getY())); velocity.multiply(20.0 /
			 * velocity.getBetrag()); position = position.add(velocity); break;
			 */
		case 4: // Eingesammelt

		}
		sprite.setCenter(position.x, position.y);

	}

	public static Eddy joinEddys(Eddy e1, Eddy e2) {
		float x = (e1.getX() + e2.getX()) * 0.5f;
		float y = (e1.getY() + e2.getY()) * 0.5f;
		float vx = (e1.velocity.x + e2.velocity.x) * 0.5f;
		float vy = (e1.velocity.y + e2.velocity.y) * 0.5f;
		Eddy joinedEddy = new Eddy(x, y, vx, vy, joinColor(e1.farbe, e2.farbe), e1.getOrbit());
		// TODO: Sounds
		// if (!Sounds.POP.playing())
		// Sounds.POP.play();
		return joinedEddy;
	}

	/*
	 * private void setEmitterColor(Color color) {
	 * 
	 * ConfigurableEmitter.ColorRecord cr = (ConfigurableEmitter.ColorRecord)
	 * (emitter.colors .get(0)); cr.col = toSlickColor(color); cr =
	 * (ConfigurableEmitter.ColorRecord) (emitter.colors.get(1)); cr.col =
	 * org.newdawn.slick.Color.white; }
	 */

	/*
	 * private org.newdawn.slick.Color toSlickColor(Color c){
	 * org.newdawn.slick.Color col; switch (c) { case ROT: col =
	 * org.newdawn.slick.Color.red; break; case GRUEN: col =
	 * org.newdawn.slick.Color.green; break; case BLAU: col =
	 * org.newdawn.slick.Color.blue; break; case GELB: col =
	 * org.newdawn.slick.Color.yellow; break; case CYAN: col =
	 * org.newdawn.slick.Color.cyan; break; case MAGENTA: col =
	 * org.newdawn.slick.Color.magenta; break; case WEISS: col =
	 * org.newdawn.slick.Color.white; break; case SCHWARZ: col =
	 * org.newdawn.slick.Color.black; break; default: col =
	 * org.newdawn.slick.Color.white; } return col; }
	 */

	private void setColor() {
		/*
		 * switch (farbe) { case ROT: sprite =
		 * Sprites.EDDY_ROT.getScaledCopy((int) radius * 2, (int) radius * 2);
		 * break; case GRUEN: sprite = Sprites.EDDY_GRUEN.getScaledCopy((int)
		 * radius * 2, (int) radius * 2); break; case BLAU: sprite =
		 * Sprites.EDDY_BLAU.getScaledCopy((int) radius * 2, (int) radius * 2);
		 * break; case GELB: sprite = Sprites.EDDY_GELB.getScaledCopy((int)
		 * radius * 2, (int) radius * 2); break; case CYAN: sprite =
		 * Sprites.EDDY_CYAN.getScaledCopy((int) radius * 2, (int) radius * 2);
		 * break; case MAGENTA: sprite =
		 * Sprites.EDDY_MAGENTA.getScaledCopy((int) radius * 2, (int) radius *
		 * 2); break; case WEISS: sprite =
		 * Sprites.EDDY_WEISS.getScaledCopy((int) radius * 2, (int) radius * 2);
		 * break; case SCHWARZ: sprite = Sprites.EDDY_WEISS.getScaledCopy((int)
		 * radius * 2, (int) radius * 2); sprite.setImageColor(0.5f, 0.5f,
		 * 0.5f); break;
		 * 
		 * } setEmitterColor(farbe);
		 */
		sprite = new Sprite(new Texture("sprites/eddys/EddyRot.png"));
		sprite.setCenter(position.x, position.y);
		sprite.setSize(radius * 2, radius * 2);
		sprite.setOriginCenter();
	}

	public Explosion createCollideExplosion() {
		// TODO magic number
		return ESPGame.getLevel().createExplosion(getPosition(), getRadius() * 1.5f, 15);
	}

	/*
	 * public Explosion createCollideExplosion() { return
	 * Game.getLevel().createExplosion(position, getRadius() * 1.5, 15, false,
	 * 1.2, 600, true); }
	 */

	@Override
	public void onRemove() {
		// emitter.wrapUp();
		// Game.getLevel().removeEmitter(emitter);
	}

	public Color getColor() {
		return farbe;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getInvested() {
		return invested;
	}

	public void setInvested(int invested) {
		this.invested = invested;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
		if (state == 3) {
			explodeable = false;
			collidable = false;
		} else if (state == 4)
			visible = false;
	}

	public int getTier() {
		return tier;
	}

	public void setTier(int tier) {
		this.tier = tier;
	}

	public Orbit getOrbit() {
		return orbit;
	}

	public void setOrbit(Orbit orbit) {
		this.orbit = orbit;
	}

	public Color getFarbe() {
		return farbe;
	}

	public void setFarbe(Color farbe) {
		this.farbe = farbe;
		setColor();
	}

	public void remove() {
		ESPGame.getLevel().removeEddy(this);
	}

	public float getGravityDrag() {
		return gravityDrag;
	}

	public void setGravityDrag(float gravityDrag) {
		this.gravityDrag = gravityDrag;
	}

	public void disableParticles() {
		// emitter.wrapUp();
	}

	public void enableparticles() {

	}

	private void updateScore() {
		switch (farbe) {
		case ROT:
		case BLAU:
		case GRUEN:
			// points = POITSTIER0[Game.game.getSchwierigkeit()];
			break;
		case MAGENTA:
		case CYAN:
		case GELB:
			// points = POITSTIER1[Game.game.getSchwierigkeit()];
			break;
		case WEISS:
			// points = POITSTIER2[Game.game.getSchwierigkeit()];
			break;
		case SCHWARZ:
			points = -100;
			break;
		default:
			points = 0;
			break;
		}
	}

	public static Color joinColor(Color color1, Color color2) {
		if (color1 == null || color2 == null || color1 == Color.SCHWARZ || color2 == Color.SCHWARZ)
			return null;
		switch (color1) {
		case ROT:
			switch (color2) {
			case BLAU:
				return Color.MAGENTA;
			case GRUEN:
				return Color.GELB;
			case ROT:
				return null;
			case CYAN:
				return Color.WEISS;
			case GELB:
				return null;
			case MAGENTA:
				return null;
			case WEISS:
				return Color.SCHWARZ;
			default:
				break;
			}
			break;
		case BLAU:
			switch (color2) {
			case BLAU:
				return null;
			case GRUEN:
				return Color.CYAN;
			case ROT:
				return Color.MAGENTA;
			case CYAN:
				return null;
			case GELB:
				return Color.WEISS;
			case MAGENTA:
				return null;
			case WEISS:
				return Color.SCHWARZ;
			default:
				break;
			}
			break;
		case CYAN: {
			switch (color2) {
			case BLAU:
				return null;
			case GRUEN:
				return null;
			case ROT:
				return Color.WEISS;
			case CYAN:
				return null;
			case GELB:
				return null;
			case MAGENTA:
				return null;
			case WEISS:
				return Color.SCHWARZ;
			default:
				break;
			}
		}

			break;
		case GELB: {
			switch (color2) {
			case BLAU:
				return Color.WEISS;
			case GRUEN:
				return null;
			case ROT:
				return null;
			case CYAN:
				return null;
			case GELB:
				return null;
			case MAGENTA:
				return null;
			case WEISS:
				return Color.SCHWARZ;
			default:
				break;
			}
		}
			break;
		case GRUEN: {
			switch (color2) {
			case BLAU:
				return Color.CYAN;
			case GRUEN:
				return null;
			case ROT:
				return Color.GELB;
			case CYAN:
				return null;
			case GELB:
				return null;
			case MAGENTA:
				return Color.WEISS;
			case WEISS:
				return Color.SCHWARZ;
			default:
				break;
			}
		}
			break;
		case MAGENTA: {
			switch (color2) {
			case BLAU:
				return null;
			case GRUEN:
				return Color.WEISS;
			case ROT:
				return null;
			case CYAN:
				return null;
			case GELB:
				return null;
			case MAGENTA:
				return null;
			case WEISS:
				return Color.SCHWARZ;
			default:
				break;
			}
		}
			break;
		case WEISS: {
			return Color.SCHWARZ;
		}
		default:
			break;
		}
		return null;
	}

	public boolean isHighlighted() {
		return highlighted;
	}

	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}

	public void setHighlightedColor(Color c) {
		this.highlightColor = c;
	}

	public static float getGravity() {
		return gravity;
	}

	public static void setGravity(float gravity) {
		Eddy.gravity = gravity;
	}

}