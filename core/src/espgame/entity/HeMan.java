package espgame.entity;

import java.io.IOException;
import java.util.Random;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import espgame.ESPGame;
import espgame.mechanics.TextDisplayer;
import espgame.resources.AssetContainer;
import espgame.resources.AssetLoader;
import espgame.resources.Fontsize;
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
	public static final int ROTATION_INSTANCE = 3;

	private Random r;
	private Vector2 target;
	private Vector2 start;
	private boolean showedUp;
	private boolean paused = false;
	private boolean introplayed = false;
	private int pauseTimer = PAUSETIMERMAX;

	private Sound enterSound, getSound, twinkle;

	@Deprecated
	public HeMan(float x, float y) {
		super(x, y);
		init();
		setPosition(x, y);
		setVelocity(new Vector2(-.5f, -.5f));

	}

	public HeMan() {
		super(0, 0);
		init();
		System.out.println("HeMan deployed.");
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

		AssetLoader loader = AssetLoader.get();
		enterSound = loader.getSound(AssetContainer.SOUND_HEMAN_ENTER);
		getSound = loader.getSound(AssetContainer.SOUND_HEMAN_GET);
		twinkle = loader.getSound(AssetContainer.SOUND_TWINKLE);
		sprite = new Sprite(loader.getTexture(AssetContainer.HEMAN));

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
		sprite.setSize(radius * 2, radius * 2);
		sprite.setOriginCenter();
		sprite.setCenter(position.x, position.y);
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
		sprite.draw(batch);
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
		sprite.setCenter(position.x, position.y);
		sprite.rotate(ROTATION_INSTANCE + new Random().nextInt(3));
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

		// TODO is this distance ok? Magic number?
		if (getPosition().dst2(ESPGame.getLevel().getPlanet().getPosition()) > 5000 * 5000) {
//			System.out.println("HeMan deleted becaouse distance! Last seen at: " + position);
			remove();
		}

		// Summon intro
		float f = position.dst(ESPGame.getLevel().getPlanet().getPosition());
		if (f < INTRODISTANZ && !introplayed) {
//			System.out.println("He-Man befindet sich in der Nähe zum Planeten. Intro!");
			initIntro();
		}

//		System.out.println("HeMan update. Pos: " + position.x + ", " + position.y);
	}

	private void onEnter() {
		twinkle.stop();
		enterSound.play();
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
			twinkle.play();
			// Game.print("I AM SUCCESS!");
		} catch (Exception e) {
			// Game.print("I AM ERROR!");
			e.printStackTrace();
		}
		introplayed = true;
		// TODO remove try catch?
		// Game.print("I AM DONE!");
	}

	public boolean isShowedUp() {
		return showedUp;
	}

	public void collect() {
		enterSound.stop();
		getSound.play();

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

		TextDisplayer d = ESPGame.getLevel().createTextDisplayer(getPosition(), VectorUtils.randomNormalized().scl(.5f),
				TEXTDURATION, "+" + r, Fontsize.Gross);
		d.setColor(Color.RED);
		System.out.println("my pos: "+getPosition()+" text pos: "+d.getPosition());
		d = ESPGame.getLevel().createTextDisplayer(getPosition(), VectorUtils.randomNormalized().scl(.5f), TEXTDURATION, "+" + g, Fontsize.Gross);
		d.setColor(Color.GREEN);
		d = ESPGame.getLevel().createTextDisplayer(getPosition(), VectorUtils.randomNormalized().scl(.5f), TEXTDURATION, "+" + b, Fontsize.Gross);
		d.setColor(Color.BLUE);
	}
}
