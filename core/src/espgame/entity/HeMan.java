package espgame.entity;

import java.io.IOException;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import espgame.ESPGame;
import espgame.mechanics.TextDisplayer;
import espgame.util.VectorUtils;

public class HeMan extends Entity {
	public static final float RADIUS = Eddy.RADIUS * 1.337f;
	public static final float THRESHOLDMULT = 2.1337f;
	public static final float THRESHOLDBOT = RADIUS * THRESHOLDMULT;
	public static final float THRESHOLDTOP = ESPGame.getLevel().getPlanet().getOrbit().getRadius()
			- RADIUS * THRESHOLDMULT;
	public static final double THRESHOLDINTRO = ESPGame.getRenderWidth() * 2.56;
	public static final float VELOCITIYMULTYPLY = 0.013f;
	public static final float SPAWNVELOCITYMULTIPLY = 4f;
	public static final int ADDITIONALREWARDEDDYS = 2;
	public static final int TEXTDURATION = 180;
	public static final int PAUSETIMERMAX = 300;
	public static final int INTRODISTANZ = 950;
	public static final int BACKUPLIFESPAWN = 250;

	private Random r;
	private Vector2 target;
	private Vector2 start;
	private boolean showedUp;
	private boolean paused = false;
	private int pauseTimer = PAUSETIMERMAX;

	public HeMan(float x, float y) {
		super(x, y);
		init();
		setPosition(x, y);
		setVelocity(new Vector2(-.5f, -.5f));
	}

	public HeMan() {
		super(0, 0);
		init();
	}

	private void init() {
		r = new Random();
		showedUp = false;
		start = new Vector2();
		target = new Vector2();
		setOrbitable(false);
		double x = calculatePosition();
		double alpha = r.nextDouble() * Math.PI * 2;
		target.set((float) (Math.cos(alpha) * x), (float) (Math.sin(alpha) * x));
		radius = RADIUS;
		setlifespan(BACKUPLIFESPAWN);

		// sprite = Sprites.HEMAN.getScaledCopy((int) radius * 2, (int) radius *
		// 2);
		// TODO set sprite

		velocity = new Vector2(target);
		if (r.nextBoolean())
			velocity.rotate90(1);
		else
			velocity.rotate90(-1);
		// TODO does this work? do it better with 'nextFloat' ?

		start = new Vector2(velocity);
		start.scl(10);
		start = start.add(target);
		velocity.scl(-1 * VELOCITIYMULTYPLY);

		// try {
		// emitterR = Game.generateEmitter("res/particles/hemanR.xml");
		// emitterG = Game.generateEmitter("res/particles/hemanG.xml");
		// emitterB = Game.generateEmitter("res/particles/hemanB.xml");
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// Game.getLevel().addParticleEmitter(emitterR);
		// Game.getLevel().addParticleEmitter(emitterG);
		// Game.getLevel().addParticleEmitter(emitterB);
		// TODO particles here

		setPosition(start);

		ESPGame.getLevel().setHeman(this);
	}

	private float calculatePosition() {
		float p = ESPGame.getLevel().getPlanet().getRadius();
		float o = ESPGame.getLevel().getPlanet().getOrbit().getRadius();

		float d = p + o * .5f * (r.nextFloat() + .15f);
		if (d < THRESHOLDBOT)
			d = THRESHOLDBOT;
		if (d > THRESHOLDTOP)
			d = THRESHOLDTOP;
		return d;
	}

	@Override
	public void render(SpriteBatch batch) {
		// Game.drawImage(sprite, getX() - radius, getY() - radius);
		// if (Game.DEBUG) {
		// g.drawString(position.toString(), (float) getX(), (float) getY());
		// g.drawRect((float) target.getX(), (float) target.getY(), 10, 10);
		// }
		// if (!showedUp &&
		// position.getDistance(Game.getLevel().getPlanet().getPosition()) <
		// THRESHOLDINTRO)
		// if (Game.DEBUG)
		// g.drawLine((float) getX(), (float) getY(), (float) target.getX(),
		// (float) target.getY());
	}

	@Override
	public void update() {
		if (paused) {
			pauseTimer--;
			if (pauseTimer == 0)
				paused = false;
		} else {
			position = position.add(velocity);
		}
		// emitterR.setPosition((float) getX(), (float) getY(), false);
		// emitterG.setPosition((float) getX(), (float) getY(), false);
		// emitterB.setPosition((float) getX(), (float) getY(), false);
		// TODO particles

		if (!showedUp && isInScreen()) {
			onEnter();
			showedUp = true;
		}
		if (showedUp && !isInScreen()) {
			ticklifespan();
		}
		// TODO implement isInScreen

		// TODO is this distance ok? Magic number?
		if (getPosition().dst2(ESPGame.getLevel().getPlanet().getPosition()) > 5000 * 5000) {
			System.out.println("HeMan deleted becaouse distance! Last seen at: " + position);
			remove();
		}

		// Summon intro
		float f = position.dst(ESPGame.getLevel().getPlanet().getPosition());
		if (f < INTRODISTANZ) {
			// TODO der heman hat die introdistanz zum ersten mal unterschritten
			// initIntro();
		}
	}

	private void onEnter() {
		// Sounds.TWINKLE.stop();
		// Sounds.HEMANENTER.play();
	}

	private void initIntro() {
		try {
			Vector2 m = new Vector2(velocity);
			m.scl(SPAWNVELOCITYMULTIPLY);

			// introSpawner = new ParticleSpawner(position, m, 900,
			// Game.generateEmitter("res/particles/hemanIntro.xml"));
			// Game.getLevel().addEntity(introSpawner);
			// TODO partikels

			paused = true;
			// Sounds.TWINKLE.play();
			// TODO play twinkle sound
			// Game.print("I AM SUCCESS!");
		} catch (Exception e) {
			// Game.print("I AM ERROR!");
			e.printStackTrace();
		}
		// Game.print("I AM DONE!");
	}

	public boolean isShowedUp() {
		return showedUp;
	}

	public void collect() {
		// Sounds.HEMANENTER.stop();
		// Sounds.HEMANGET.play();
		// TODO sound

		// try {
		// getSpawner = new ParticleSpawner(position, new Vector(), 5,
		// Game.generateEmitter("res/particles/hemanGet.xml"));
		// getSpawner.setEmitterImage(Sprites.STERN.getResourceReference());
		// Game.getLevel().addEntity(getSpawner);
		// } catch (IOException e1) {
		// e1.printStackTrace();
		// }
		// TODO farticle

		remove();

		int r = 1, g = 1, b = 1;
		for (int i = 0; i < ADDITIONALREWARDEDDYS; i++) {
			switch (new Random().nextInt(3)) {
			case 0:
				r++;
				break;
			case 1:
				g++;
				break;
			case 2:
				b++;
				break;
			default:
				i--;
			}
		}
		ESPGame.getLevel().modReserve(0, r);
		ESPGame.getLevel().modReserve(1, g);
		ESPGame.getLevel().modReserve(2, b);

		TextDisplayer d = ESPGame.getLevel().createTextDisplayer(getPosition(), VectorUtils.random().scl(.5f),
				TEXTDURATION, "+" + r);
		d.setColor(Color.RED);
		d = ESPGame.getLevel().createTextDisplayer(getPosition(), VectorUtils.random().scl(.5f), TEXTDURATION, "+" + g);
		d.setColor(Color.GREEN);
		d = ESPGame.getLevel().createTextDisplayer(getPosition(), VectorUtils.random().scl(.5f), TEXTDURATION, "+" + b);
		d.setColor(Color.BLUE);
	}
}
