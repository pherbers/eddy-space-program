package espgame.entity;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import espgame.ESPGame;
import espgame.level.Orbit;
import espgame.resources.AssetContainer;
import espgame.resources.AssetLoader;
import espgame.resources.ParticleContainer;

public class Eddy extends Entity {

	private int points, invested, state, tier;

	public enum Color {
		ROT(0), BLAU(1), GRUEN(2), GELB(3), CYAN(4), MAGENTA(5), WEISS(6), SCHWARZ(7);

		private int index;

		private Color(int index) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}
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
	public static boolean highlight = true;
	private Sprite highlightSprite;
	private Color highlightColor;

	private ParticleEffect particleEffect;

	public static final float GRAVITYEASY = 1.0f, GRAVITYNORMAL = 0.9999f, GRAVITYHARD = 0.9998f;

	public static final float EDDY_DESPAWN_DISTANCE = 2000;

	private static float gravity = GRAVITYNORMAL;

	public Eddy(float x, float y, float vx, float vy, Color farbe, Orbit orbit) {
		super(x, y);
		setVelocity(vx, vy);
		this.orbit = orbit;
		state = 0; // 0 = Abschussphase, 1 = Orbitphase, 2 = OutOfOrbit, 3 =
					// Einsammelphase
		this.farbe = farbe;
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
		clockwise = v.y * velocity.x < v.x * velocity.y;
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
		switch (ESPGame.getLevel().getSelectedEddy()) {
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
				highlightSprite.draw(batch);
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
			break;
		case 2:
			position = position.add(velocity);
			float entf = position.dst(orbit.getPosition());
			if (entf < orbit.getRadius()) {
				setState(1);
			} else if (entf > EDDY_DESPAWN_DISTANCE && isCollidable()) {
				ESPGame.getLevel().removeEddy(this);
			}
			break;
		case 3: // TODO: Einsammeln
			Vector2 schiffpos = ESPGame.getLevel().getSchiff().position;
			velocity.set((schiffpos.x - position.x), (schiffpos.y - position.y));
			velocity.scl(20.0f / velocity.len());
			position = position.add(velocity);
			break;
		case 4: // Eingesammelt

		}
		sprite.setCenter(position.x, position.y);
		highlightSprite.setCenter(position.x, position.y);
		particleEffect.setPosition(position.x, position.y);
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

	public static com.badlogic.gdx.graphics.Color toGdxColor(Color c) {
		com.badlogic.gdx.graphics.Color col;
		switch (c) {
		case ROT:
			col = com.badlogic.gdx.graphics.Color.RED;
			break;
		case GRUEN:
			col = com.badlogic.gdx.graphics.Color.GREEN;
			break;
		case BLAU:
			col = com.badlogic.gdx.graphics.Color.BLUE;
			break;
		case GELB:
			col = com.badlogic.gdx.graphics.Color.YELLOW;
			break;
		case CYAN:
			col = com.badlogic.gdx.graphics.Color.CYAN;
			break;
		case MAGENTA:
			col = com.badlogic.gdx.graphics.Color.MAGENTA;
			break;
		case WEISS:
			col = com.badlogic.gdx.graphics.Color.WHITE;
			break;
		case SCHWARZ:
			col = com.badlogic.gdx.graphics.Color.BLACK;
			break;
		default:
			col = com.badlogic.gdx.graphics.Color.WHITE;
		}
		return col;
	}

	private void setColor() {
		String spriteKey = AssetContainer.EDDY_ROT;
		ParticleEffect effect;
		ParticleContainer container = ESPGame.getLevel().particleContainer;
		switch (farbe) {
		case ROT:
			spriteKey = AssetContainer.EDDY_ROT;
			effect = container.eddyRot;
			break;
		case GRUEN:
			spriteKey = AssetContainer.EDDY_GRUEN;
			effect = container.eddyGruen;
			break;
		case BLAU:
			spriteKey = AssetContainer.EDDY_BLAU;
			effect = container.eddyBlau;
			break;
		case GELB:
			spriteKey = AssetContainer.EDDY_GELB;
			effect = container.eddyGelb;
			break;
		case CYAN:
			spriteKey = AssetContainer.EDDY_CYAN;
			effect = container.eddyCyan;
			break;
		case MAGENTA:
			spriteKey = AssetContainer.EDDY_MAGENTA;
			effect = container.eddyMagenta;
			break;
		case WEISS:
			spriteKey = AssetContainer.EDDY_WEISS;
			effect = container.eddyWeiss;
			break;
		default:
			spriteKey = AssetContainer.EDDY_WEISS;
			effect = container.eddySchwarz;
			break;
		}
		// setEmitterColor(farbe);
		sprite = new Sprite(AssetLoader.get().getTexture(spriteKey));
		if (farbe == Color.SCHWARZ)
			sprite.setColor(com.badlogic.gdx.graphics.Color.GRAY);
		sprite.setSize(radius * 2, radius * 2);
		sprite.setOriginCenter();
		sprite.setCenter(position.x, position.y);

		particleEffect = new ParticleEffect(effect);
		ESPGame.getLevel().addParticleSystem(particleEffect);
		particleEffect.start();

		highlightSprite = new Sprite(AssetLoader.get().getTexture(AssetContainer.EDDY_HIGHLIGHT));
		highlightSprite.setSize(radius * 2 + 16, radius * 2 + 16);
		highlightSprite.setOriginCenter();
		highlightSprite.setCenter(position.x, position.y);
	}

	public Explosion createCollideExplosion(Color opposingColor) {
		// TODO magic number
		Explosion exp = ESPGame.getLevel().createExplosion(getPosition(), getRadius() * 1.5f, 15);
		exp.setEmitterColors(toGdxColor(this.farbe), toGdxColor(opposingColor));
		return exp;
	}

	@Override
	public void onRemove() {
		particleEffect.setDuration(0);
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
		else if (state == 2)
			particleEffect.setDuration(0);
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
		particleEffect.setDuration(0);
	}

	public void enableparticles() {

	}

	private void updateScore() {
		switch (farbe) {
		case ROT:
		case BLAU:
		case GRUEN:
			points = POITSTIER0[ESPGame.getLevel().getSchwierigkeit()];
			break;
		case MAGENTA:
		case CYAN:
		case GELB:
			points = POITSTIER1[ESPGame.getLevel().getSchwierigkeit()];
			break;
		case WEISS:
			points = POITSTIER2[ESPGame.getLevel().getSchwierigkeit()];
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
		this.highlightSprite.setColor(toGdxColor(c));
	}

	public static float getGravity() {
		return gravity;
	}

	public static void setGravity(float gravity) {
		Eddy.gravity = gravity;
	}

}