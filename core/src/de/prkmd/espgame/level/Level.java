package de.prkmd.espgame.level;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import de.prkmd.espgame.ESPGame;
import de.prkmd.espgame.entity.Eddy;
import de.prkmd.espgame.entity.Eddy.Color;
import de.prkmd.espgame.entity.Entity;
import de.prkmd.espgame.entity.Explosion;
import de.prkmd.espgame.entity.HeMan;
import de.prkmd.espgame.entity.Kanone;
import de.prkmd.espgame.entity.Schiff;
import de.prkmd.espgame.input.KanoneController;
import de.prkmd.espgame.mechanics.Highscore;
import de.prkmd.espgame.mechanics.TextDisplayer;
import de.prkmd.espgame.resources.AssetContainer;
import de.prkmd.espgame.resources.AssetLoader;
import de.prkmd.espgame.resources.Fontsize;
import de.prkmd.espgame.resources.ParticleContainer;
import de.prkmd.espgame.screens.GameOverScreen;
import de.prkmd.espgame.screens.HighscoreScreen;
import de.prkmd.espgame.screens.LevelOverlay;
import de.prkmd.espgame.screens.PauseScreen;
import de.prkmd.espgame.ui.uielements.LevelUI;

public class Level implements Screen {

	private float deltaTime;

	public static final float UPDATE_TIME = 1f / 60f;

	private static final float PLANET_SIZE = 128;
	private static final float PLANET_ORBIT_RADIUS = 400;
	private static final float PLANET_ORBIT_FORCE = 1;
	public static final int HEMANCOUNTDOWNBASE = 180;
	public static final int HEMANJITTER = 25;
	public static final int HEMANLEVEL = 2;
	public static final int EDDYCAP = 9;
	public static final float DEFAULT_ZOOM = 1.0f;
	public static final float BEGIN_ZOOM = 0.42f;
	public static final float ZOOM_FACTOR = 0.01337f / 2;

	public static final int MIN_WORLD_WIDTH = 800, MAX_WORLD_WIDTH = 1920, MIN_WORLD_HEIGHT = 800,
			MAX_WORLD_HEIGHT = 1080;

	public static final float STAR_PERCENTAGE = 0.00015f;

	public static final float BACKGROUND_SCREEN_SHAKE_FACTOR = 0.5f;
	public static final float BACKGROUND_ZOOM_START = 0.6f;

	private int hemanCoutdown = HEMANCOUNTDOWNBASE;

	private LinkedList<Entity> addQueue, removeQueue;

	private Objective objective;
	private Schiff schiff;
	private List<Entity> entities;
	private List<Eddy> eddys;
	private final ESPGame game;
	private HeMan heman;
	private Hintergrund hintergrund;
	public OrthographicCamera camera, backgroundCam;
	public ExtendViewport worldViewport;
	private boolean spawnHeman = false;
	private int reserveRot, reserveBlau, reserveGruen;
	private boolean paused, running, gameover;
	private int schwierigkeit;
	private int hemandelay = HEMANLEVEL;
	private int points, level;
	private int selectedEddy = 0;
	private int shake_dur;
	private float zoomPercent;
	private float shake_mag, shake_linear;

	private Stage stage;

	private Planet planet;
	private Kanone kanone;
	private KanoneController kanonec;
	private InputMultiplexer input;

	private LevelUI ui;

	public final ParticleContainer particleContainer;
	private ArrayList<ParticleEffect> particleEffects;

	public Level(int schwierigkeit, final ESPGame game) {
		this.schwierigkeit = schwierigkeit;
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		camera.position.set(0, 0, 0);
		zoomPercent = 0;
		backgroundCam = new OrthographicCamera();
		backgroundCam.setToOrtho(false, 800, 480);
		backgroundCam.position.set(0, 0, 0);
		worldViewport = new ExtendViewport(1024, 768, 1920, 1080, camera);

		particleEffects = new ArrayList<ParticleEffect>();
		particleContainer = new ParticleContainer();

		System.out.println("Starting a new gaem. Schwierigkeit: " + schwierigkeit);

	}

	public void init() {
		// Init & Setup
		entities = new ArrayList<Entity>();
		eddys = new ArrayList<Eddy>();
		addQueue = new LinkedList<Entity>();
		removeQueue = new LinkedList<Entity>();

		// Particles
		particleContainer.loadParticles();

		objective = new Objective();
		hintergrund = new Hintergrund(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), STAR_PERCENTAGE);
		level = 1;
		points = 0;
		Planet planet = new Planet(PLANET_SIZE, PLANET_ORBIT_RADIUS, PLANET_ORBIT_FORCE);
		schiff = new Schiff(ESPGame.getRandomOmega(), planet.getOrbit().getRadius() + 50, planet.getOrbit());

		this.planet = planet;
		kanone = new Kanone(new Random().nextFloat() * 360);

		if (schwierigkeit == 2)
			hemandelay = HEMANLEVEL + 1;
		else
			hemandelay = HEMANLEVEL;

		switch (schwierigkeit) {
		case 0:
			Eddy.setGravity(Eddy.GRAVITYEASY);
			break;
		case 1:
			Eddy.setGravity(Eddy.GRAVITYNORMAL);
			break;
		case 2:
			Eddy.setGravity(Eddy.GRAVITYHARD);
			break;
		}

		running = true;
		gameover = false;
		paused = false;
		shake_dur = 0;
		shake_mag = 0;

		entities.add(planet);
		entities.add(schiff);
		entities.add(kanone);
		this.kanonec = new KanoneController(kanone);

		stage = new Stage(new ScreenViewport());
		// Gdx.input.setInputProcessor(stage); TODO dis
		Skin skin = AssetLoader.get().getSkin();
		ui = new LevelUI(this, skin);
		stage.addActor(ui);
		// stage.setDebugAll(true);

		// BEGIN RUNDE 1
		setReserved(3, 3, 3);
		newObjective(level);
		ui.updateObjective();

		worldViewport.apply(true);
	}

	@Override
	public void show() {
		this.input = new InputMultiplexer();
		input.addProcessor(new InputAdapter() {
			@Override
			public boolean keyDown(int keycode) {
				if (keycode == Input.Keys.ESCAPE) {
					enablePause();
					System.out.println("Pause");
					return true;
				}
				return false;
			}
		});
		input.addProcessor(stage);
		input.addProcessor(kanonec);

		Gdx.input.setInputProcessor(input);
	}

	@Override
	public void render(float delta) {
		// Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		deltaTime += delta;
		if (deltaTime > UPDATE_TIME) {
			update();
			deltaTime -= UPDATE_TIME;
		}

		render();

		stage.act(delta);
	}

	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		backgroundCam.update();
		// System.out.println("zoomPercent: " + zoomPercent);

		game.batch.setProjectionMatrix(backgroundCam.combined);
		game.batch.begin();
		hintergrund.render(game.batch);
		game.batch.flush();
		game.batch.setProjectionMatrix(camera.combined);
		for (int i = particleEffects.size() - 1; i >= 0; i--) {
			ParticleEffect p = particleEffects.get(i);
			p.draw(game.batch);
			if (p.isComplete()) {
				particleEffects.remove(p);
			}
		}
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(game.batch);
		}
		for (int i = 0; i < eddys.size(); i++) {
			Eddy e = eddys.get(i);
			e.render(game.batch);
		}

		// if(gameover){
		// game.batch.flush();
		// Gdx.gl.glClearColor(180, 20, 70, .5f);
		// Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// }

		game.batch.end();

		stage.draw();
	}

	public void update() {
		// setSchwierigkeit(ESPGame.game.getSchwierigkeit());
		// TODO wieso haben wir das gemacht? wir ändern die schwierigkeit aber
		// gehen nie wieder darauf ein

		if (gameover && running) {
			final GameOverScreen menu = new GameOverScreen();
			final LevelOverlay screen = new LevelOverlay(this, menu);
			menu.setOverlay(screen);
			ESPGame.game.setScreen(screen);
			// Enable Gameover Overlay
		} else {
			// ui.setGameOver(false);
			// Disable Gameover Overlay
		}

		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.update();
		}

		for (int i = 0; i < eddys.size(); i++) {
			Entity e = eddys.get(i);
			e.update();
		}

		// shakey cam
		if (shake_dur == 0) {
			camera.position.x = 0;
			camera.position.y = 0;
			shake_mag = 0;
		} else {
			camera.position.x = shakeValue();
			camera.position.y = shakeValue();
			backgroundCam.position.x = camera.position.x * BACKGROUND_SCREEN_SHAKE_FACTOR;
			backgroundCam.position.y = camera.position.y * BACKGROUND_SCREEN_SHAKE_FACTOR;
			shake_dur--;
			shake_mag -= shake_linear;
		}

		// cam zoomPercent
		zoomPercent += ZOOM_FACTOR;
		if (zoomPercent >= 1) {
			zoomPercent = 1;
		}
		camera.zoom = Interpolation.pow3Out.apply(BEGIN_ZOOM, DEFAULT_ZOOM, zoomPercent);
		backgroundCam.zoom = Interpolation.pow3Out.apply(BACKGROUND_ZOOM_START, DEFAULT_ZOOM, zoomPercent);


		for (int i = particleEffects.size() - 1; i >= 0; i--) {
			ParticleEffect p = particleEffects.get(i);
			p.update(UPDATE_TIME);
			if (p.isComplete())
				particleEffects.remove(i);
		}

		// Entities sicher entfernen
		while (!removeQueue.isEmpty()) {
			removeQueue.peek().onRemove();
			if (removeQueue.peek() instanceof Eddy) {
				eddys.remove(removeQueue.pop());
			} else {
				entities.remove(removeQueue.pop());
			}
		}

		// Entities sicher hinzufügen
		while (!addQueue.isEmpty()) {
			if (addQueue.peek() instanceof Eddy)
				eddys.add((Eddy) addQueue.pop());
			else
				entities.add(addQueue.pop());
		}

		// Eddy-Kollisionen
		for (int i = 0; i < eddys.size(); i++) {
			// Kollision mit anderen eddys
			for (int j = i + 1; j < eddys.size(); j++) {
				if (eddys.get(i).checkCollision(eddys.get(j))) {
					Eddy dominantEddy, noobEddy;
					if (eddys.get(i).getVelocity().len2() < eddys.get(j).getVelocity().len2()) {
						noobEddy = eddys.get(i);
						dominantEddy = eddys.get(j);
					} else {
						noobEddy = eddys.get(j);
						dominantEddy = eddys.get(i);
					}
					// The Joining Of The Eddies
					Color dc = dominantEddy.getFarbe();
					Color nc = noobEddy.getFarbe();
					if (dc == Color.SCHWARZ || nc == Color.SCHWARZ || (dc == nc)) {
						dominantEddy.createCollideExplosion(Color.SCHWARZ);
					} else {
						Color col = Eddy.joinColor(dominantEddy.getFarbe(), noobEddy.getFarbe());
						if (col == null) {
							dominantEddy.createCollideExplosion(noobEddy.getColor());
							removeEddy(noobEddy);
						} else {
							addEddy(Eddy.joinEddys(dominantEddy, noobEddy));
						}
					}
					removeEddy(dominantEddy);
					removeEddy(noobEddy);
					schiff.resetCheckCountdown();
				}
			}
			// Kollision mit Allem anderen
			for (int j = 0; j < entities.size(); j++) {
				Entity e = entities.get(j);

				if (entities.get(j) instanceof HeMan)
					if (eddys.get(i).getPosition().dst(e.getPosition()) < e.getRadius() + eddys.get(i).getRadius()) {
						((HeMan) entities.get(j)).collect();
						continue;
					}
				if (entities.get(j) instanceof Planet && eddys.get(i).isCollidable())
					if (eddys.get(i).getPosition().dst(e.getPosition()) < e.getRadius() + eddys.get(i).getRadius()) {
						eddys.get(i).createCollideExplosion(Color.SCHWARZ);
						removeEddy(i);
						continue;
					}

				if (e.isCollidable()) {
					if (eddys.get(i).checkCollision(e))
						removeEddy(i);
				}
			}

			if (reserveBlau > 10)
				reserveBlau = 10;
			if (reserveGruen > 10)
				reserveGruen = 10;
			if (reserveRot > 10)
				reserveRot = 10;

			// TODO obsolete?
			// // Kollision mit Planet
			// if (eddys.get(i).checkCollision(planet)) {
			// eddys.remove(i);
			// }
		}

		// Objective überprüfen
		if (!schiff.isBusy()) {
			gameover = !objective.checkObjective(getReserve()); // TODO nils:
																// ich musste es
																// bewegen
		}

		// He Man spawn
		if (spawnHeman) {
			// System.out.println("He-Man steht unmittelbar bevor! " +
			// hemanCoutdown);
			hemanCoutdown--;
			if (hemanCoutdown == 0) {
				hemanCoutdown = HEMANCOUNTDOWNBASE + new Random().nextInt(HEMANJITTER);
				spawnHeman = false;
				heman = new HeMan();
				addEntity(heman);
			}
		}

		// HUD updates
		ui.update();
	}

	public Explosion createExplosion(Vector2 position, float explosionsRadius, int lifespan) {
		Explosion e = new Explosion(position, explosionsRadius, lifespan);
		addEntity(e);

		e.removeObjects();

		return e;
	}

	@Override
	public void resize(int width, int height) {
		// camera.setToOrtho(false, width, height);
		// TODO match hintergrund to new screensize?
		// camera.update();
		worldViewport.setMinWorldWidth(MathUtils.clamp(width, MIN_WORLD_WIDTH, MAX_WORLD_WIDTH));
		worldViewport.setMinWorldHeight(MathUtils.clamp(height, MIN_WORLD_HEIGHT, MAX_WORLD_HEIGHT));
		worldViewport.update(width, height, true);
		backgroundCam.setToOrtho(true, width, height);
		backgroundCam.position.set(0, 0, 0);
		backgroundCam.update();
		hintergrund.resize((int) worldViewport.getWorldWidth(), (int) worldViewport.getWorldHeight());
		stage.getViewport().update(width, height, true);
		ui.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.x = 0; camera.position.y = 0;
		// viewport.update(width, height);
	}

	private void newObjective(int level) {
		objective.newObjective(level, getReserve());
		// ui.updateObjective();
		// TODO UI update
	}

	@Override
	public void pause() {
		input.removeProcessor(kanonec);
	}

	@Override
	public void resume() {
		input.addProcessor(kanonec);
	}

	@Override
	public void hide() {
		input.removeProcessor(kanonec);
	}

	@Override
	public void dispose() {
		Gdx.input.setInputProcessor(null);
		stage.dispose();
	}

	public void addEntity(Entity e) {
		addQueue.add(e);
	}

	public Planet getPlanet() {
		return planet;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public int getEddyCount() {
		return eddys.size();
	}

	public Eddy getEddy(int i) {
		return eddys.get(i);
	}

	public void addEddy(Eddy e) {
		addQueue.add(e);
		schiff.resetCheckCountdown();
	}

	// TODO obsolete?
	public void removeEddy(Eddy e) {
		removeQueue.add(e);
	}

	public void removeEddy(int i) {
		removeQueue.add(getEddy(i));
	}

	public void removeEntity(Entity e) {
		removeQueue.add(e);
	}

	public HeMan getHeman() {
		return heman;
	}

	public void setHeman(HeMan heman) {
		this.heman = heman;
	}

	public void setPunkte(int punkte) {
		this.points = punkte;
	}

	public void modPunkte(int mod) {
		setPunkte(getPunkte() + mod);
	}

	public int getPunkte() {
		return points;
	}

	public TextDisplayer createTextDisplayer(Vector2 pos, Vector2 vel, int duration, String text, Fontsize size) {
		System.out.println("Textdisplayer requested: \"" + text + "\"");

		BitmapFont font = null;
		switch (size) {
		case Klein:
			font = AssetLoader.get().getFont(AssetContainer.FONT_SMALL);
			break;
		case Mittel:
			font = AssetLoader.get().getFont(AssetContainer.FONT_MEDIUM);
			break;
		case Gross:
			font = AssetLoader.get().getFont(AssetContainer.FONT_BIG);
			break;
		default:
			System.err.println("Invalide Fontsize!!");
			break;
		}

		TextDisplayer td = new TextDisplayer(pos.x, pos.y, vel, text, font, duration);
		addEntity(td);
		return td;
	}

	public void nextLevel() {
		level++;
		int cap = Objective.getCap(level);
		if (schwierigkeit == 0)
			cap++;
		Random r = new Random();
		while (cap >= 0) {
			modReserve(r.nextInt(3), 1);
			cap--;
		}
		newObjective(level);
		ui.updateObjective();
		if (level % hemandelay == 0) {
			spawnHeman = true;
		}
	}

	public boolean checkObjective() {
		// prüfen, ob die Eddys abgeholt werden
		// können

		if (!schiff.isBusy()) {
			ArrayList<Eddy> eList = new ArrayList<Eddy>();
			for (Eddy.Color c : objective) {
				boolean complete = false;
				for (Eddy e : eddys) {
					if (e.getColor() == c && e.getState() == 1) {
						if (eList.contains(e))
							continue;
						complete = true;
						eList.add(e);
						break;
					}
				}
				if (!complete)
					return false;
			}
			schiff.einsammeln(eList);
			return true;
		}

		return false;
	}

	public int getReserve(int color) {
		switch (color) {
		case 0:
			return getReserveRot();
		case 1:
			return getReserveBlau();
		case 2:
			return getReserveGruen();

		default:
			throw new IllegalArgumentException("bla");
		}
	}

	public int getReserveRot() {
		return reserveRot;
	}

	public void setReserveRot(int reserveRot) {
		this.reserveRot = reserveRot;
		if (reserveRot > EDDYCAP) {
			reserveRot = EDDYCAP;
		}
		if (reserveRot < 0) {
			reserveRot = 0;
		}
	}

	public int getReserveBlau() {
		return reserveBlau;
	}

	public void setReserveBlau(int reserveBlau) {
		this.reserveBlau = reserveBlau;
		if (reserveBlau > EDDYCAP) {
			reserveBlau = EDDYCAP;
		}
		if (reserveBlau < 0) {
			reserveBlau = 0;
		}
	}

	public int getReserveGruen() {
		return reserveGruen;
	}

	public void setReserveGruen(int reserveGruen) {
		this.reserveGruen = reserveGruen;
		if (reserveGruen > EDDYCAP) {
			reserveGruen = EDDYCAP;
		}
		if (reserveGruen < 0) {
			reserveGruen = 0;
		}
	}

	public void setReserved(int r, int g, int b) {
		setReserveRot(r);
		setReserveGruen(g);
		setReserveBlau(b);
	}

	private int[] getReserve() {
		int[] reserve = new int[7];
		reserve[0] = getReserveRot();
		reserve[1] = getReserveBlau();
		reserve[2] = getReserveGruen();
		for (Eddy e : eddys) {
			if (e.getState() > 1)
				continue;
			int j;
			switch (e.getFarbe()) {
			case ROT:
				j = 0;
				break;
			case BLAU:
				j = 1;
				break;
			case GRUEN:
				j = 2;
				break;
			case MAGENTA:
				j = 3;
				break;
			case CYAN:
				j = 4;
				break;
			case GELB:
				j = 5;
				break;
			case WEISS:
				j = 6;
				break;
			default:
				continue;
			}
			reserve[j]++;
		}
		return reserve;
	}

	public int[] getReserved() {
		int[] c = new int[3];
		c[0] = getReserveRot();
		c[1] = getReserveGruen();
		c[2] = getReserveBlau();
		return c;
	}

	public int getReservedCount() {
		return reserveBlau + reserveGruen + reserveRot;
	}

	public void modReserve(int color, int mod) {
		switch (color) {
		case 0:
			setReserveRot(getReserveRot() + mod);
			break;
		case 1:
			setReserveBlau(getReserveBlau() + mod);
			break;
		case 2:
			setReserveGruen(getReserveGruen() + mod);
			break;
		default:
			throw new IllegalArgumentException();
		}
		if (mod != 0) {
			// double px = ui.getEddySelectorPositionForEddy(color).getX();
			// double py = ui.getEddySelectorPositionForEddy(color).getY() +
			// 128;
			// // px=Game.parseScreenToUIHeight(px);
			// // py=Game.parseScreenToUIWidth(py);
			// // pos = pos.add()
			// org.newdawn.slick.Color c = org.newdawn.slick.Color.green;
			// Vector vel = new Vector(Vector.UP);
			// String s = "+";
			// if (mod < 0) {
			// c = org.newdawn.slick.Color.red;
			// vel = new Vector(Vector.DOWN);
			// s = "";
			// }
			// vel.multiply(.33333);
			// hardreset.ui.TextDisplayer td = ui.displayText(px, py, vel, s +
			// mod + "!", 50);
			// // td.setColor(c);
			// TODO what have i done...?
		}
	}

	public void setSelectedEddy(int i) {
		int temp = selectedEddy;
		selectedEddy = i;
		if (selectedEddy > 2)
			selectedEddy = 2;
		else if (selectedEddy < 0)
			selectedEddy = 0;
		if (temp != selectedEddy && getSchwierigkeit() == 0)
			setHighlighted();
	}

	private void setHighlighted() {
		for (Eddy e : eddys) {
			Color color;
			switch (selectedEddy) {
			case 0:
				color = Color.ROT;
				break;
			case 1:
				color = Color.BLAU;
				break;
			case 2:
				color = Color.GRUEN;
				break;
			default:
				color = null;
			}
			Color joined = Eddy.joinColor(e.getColor(), color);
			if (joined != null && joined != Color.SCHWARZ) {
				e.setHighlighted(true);
				e.setHighlightedColor(joined);
			} else
				e.setHighlighted(false);
		}
	}

	public int getSelectedEddy() {
		return selectedEddy;
	}

	public void togglePause() {
		if (!gameover) {
			paused = !paused;
			System.out.println("Is it paused? " + paused);
			if (paused) {
				final PauseScreen menu = new PauseScreen();
				final LevelOverlay screen = new LevelOverlay(this, menu);
				menu.setOverlay(screen);
				ESPGame.game.setScreen(screen);
				System.out.println("Pause ein!");
			} else {
				// container.getInput().addMouseListener(kanone);
				// Game.closeMenu();
				// TODO Enable Kanonen Mauslistener
				// TODO Hide PauseMenü
				System.out.println("Pause aus");
			}
		}
	}

	public void enablePause() {
		final PauseScreen menu = new PauseScreen();
		final LevelOverlay screen = new LevelOverlay(this, menu);
		menu.setOverlay(screen);
		ESPGame.game.setScreen(screen);
		System.out.println("Pause ein!");
	}

	public void disablePause() {
		ESPGame.game.changeScreen(this);
	}

	public void onRemove() {
		// TODO remove mouse listener
		// container.getInput().removeMouseListener(kanone);
	}

	public void endLevel(boolean addHighscore) {
		running = false;

		if (addHighscore) {
			Highscore score = new Highscore(level, points, schwierigkeit);
			ESPGame.game.changeScreen(new HighscoreScreen(score));
		} else {
			ESPGame.game.toMenu();
		}
	}

	public void shakeScreen(float magnitude, int duration) {
		if (shake_dur < duration) {
			shake_dur = duration;
			shake_linear = shake_mag / shake_dur;
		}
		if (shake_mag < magnitude) {
			shake_mag = magnitude;
			shake_linear = shake_mag / shake_dur;
		}
	}

	public float shakeValue() {
		Random r = new Random();
		float i = r.nextFloat() * shake_mag;
		if (r.nextBoolean())
			i *= -1;
		return i;
	}

	public int getLevel() {
		return level;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public int getSchwierigkeit() {
		return schwierigkeit;
	}

	public void setSchwierigkeit(int schwierigkeit) {
		this.schwierigkeit = schwierigkeit;
	}

	public Schiff getSchiff() {
		return schiff;
	}

	public boolean isGameover() {
		return gameover;
	}

	public Kanone getKanone() {
		return kanone;
	}

	public Objective getObjective() {
		return objective;
	}

	public void addParticleSystem(ParticleEffect particleEffect) {
		particleEffects.add(particleEffect);
	}
}
