package de.prkmd.espgame.entity;

import java.util.Random;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import de.prkmd.espgame.ESPGame;
import de.prkmd.espgame.mechanics.TextDisplayer;
import de.prkmd.espgame.resources.AssetContainer;
import de.prkmd.espgame.resources.AssetLoader;
import de.prkmd.espgame.resources.Fontsize;
import de.prkmd.espgame.util.VectorUtils;

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
	public static final int STERNSCHNUPPE_DAUER = 900;

	private Random r;
	private Vector2 target;
	private Vector2 start;
	private boolean showedUp;
	private boolean paused = false;
	private boolean introplayed = false;
	private int pauseTimer = PAUSETIMERMAX;

	private Sound enterSound, getSound, twinkle;
	private ParticleEffect particleEffect, introParticleEffect, getEffect;

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

		ParticleEffect effect = new ParticleEffect(ESPGame.getLevel().particleContainer.heman);
		ESPGame.getLevel().addParticleSystem(effect);
		effect.setPosition(this.position.x, this.position.y);
		effect.start();
		particleEffect = effect;

		getEffect = new ParticleEffect(ESPGame.getLevel().particleContainer.hemanGet);

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
		particleEffect.setPosition(position.x, position.y);

		if (!showedUp && isInScreen()) {
			onEnter();
			showedUp = true;
		}
		if (showedUp && !isInScreen()) {
			ticklifespan();
		}

		// TODO is this distance ok? Magic number?
		if (getPosition().dst2(ESPGame.getLevel().getPlanet().getPosition()) > 5000 * 5000) {
			// System.out.println("HeMan deleted becaouse distance! Last seen
			// at: " + position);
			remove();
		}

		// Summon intro
		float f = position.dst(ESPGame.getLevel().getPlanet().getPosition());
		if (f < INTRODISTANZ && !introplayed) {
			// System.out.println("He-Man befindet sich in der Nähe zum
			// Planeten. Intro!");
			initIntro();
		}

		// System.out.println("HeMan update. Pos: " + position.x + ", " +
		// position.y);
	}

	private void onEnter() {
		twinkle.stop();
		enterSound.play(ESPGame.game.getSoundVolume() * 1.2f);
	}

	private void initIntro() {
		try {
			Vector2 m = new Vector2(velocity);
			m.scl(SPAWNVELOCITYMULTIPLY);

			ParticleEffect effect = new ParticleEffect(ESPGame.getLevel().particleContainer.sternschnuppe);
			ParticleSpawner spawner = new ParticleSpawner(getPosition(), m, effect, STERNSCHNUPPE_DAUER);
			introParticleEffect = effect;
			ESPGame.getLevel().addEntity(spawner);

			paused = true;
			twinkle.play(ESPGame.game.getSoundVolume() * 0.9f);
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
		getSound.play(ESPGame.game.getSoundVolume() * 3.4f);

		getEffect.reset();
		getEffect.setPosition(position.x, position.y);
		ESPGame.getLevel().addParticleSystem(getEffect);

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
		d = ESPGame.getLevel().createTextDisplayer(getPosition(), VectorUtils.randomNormalized().scl(.5f), TEXTDURATION,
				"+" + g, Fontsize.Gross);
		d.setColor(Color.GREEN);
		d = ESPGame.getLevel().createTextDisplayer(getPosition(), VectorUtils.randomNormalized().scl(.5f), TEXTDURATION,
				"+" + b, Fontsize.Gross);
		d.setColor(Color.BLUE);
	}

	@Override
	public void onRemove() {
		super.onRemove();
		particleEffect.setDuration(0);
		introParticleEffect.setDuration(0);
	}
}
