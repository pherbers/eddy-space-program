package espgame.level;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import espgame.ESPGame;
import espgame.entity.Eddy;
import espgame.entity.Eddy.Color;
import espgame.entity.Entity;
import espgame.entity.Explosion;
import espgame.entity.HeMan;
import espgame.entity.Kanone;
import espgame.entity.Schiff;
import espgame.input.KanoneController;
import espgame.mechanics.TextDisplayer;
import espgame.resources.AssetContainer;
import espgame.resources.AssetLoader;
import espgame.resources.Fontsize;

/**
 * Created by Patrick on 26.08.2015.
 */
public class Level implements Screen {

	private float deltaTime;

	private static final float UPDATE_TIME = 1f / 60f;

	private static final float PLANET_SIZE = 128;
	private static final float PLANET_ORBIT_RADIUS = 400;
	private static final float PLANET_ORBIT_FORCE = 1;
	public static final int HEMANCOUNTDOWNBASE = 180;
	public static final int HEMANJITTER = 25;
	public static final int HEMANLEVEL = 2;
	public static final int EDDYCAP = 10;

	public static final float STAR_PERCENTAGE = 0.0005f;

	private int hemanCoutdown = HEMANCOUNTDOWNBASE;

	private LinkedList<Entity> addQueue, removeQueue;

	private Objective objective;
	private Schiff schiff;
	private List<Entity> entities;
	private List<Eddy> eddys;
	private final ESPGame game;
	private HeMan heman;
	private Hintergrund hintergrund;
	public OrthographicCamera camera;
	private boolean spawnHeman = false;
	private int reserveRot, reserveBlau, reserveGruen;
	private boolean paused, running, gameover;
	private int schwierigkeit;
	private int hemandelay = HEMANLEVEL;
	private int points, level;
	private int selectedEddy = 0;
	private int shake_mag, shake_dur;

	private Stage stage;
	private Table table;
	private ShapeRenderer shapeRenderer;
	private Viewport viewport;

	private Planet planet;
	private Kanone kanone;
	private KanoneController kanonec;
	private InputMultiplexer input;

	private TextButton btn;

	public Level(int schwierigkeit, final ESPGame game) {
		this.schwierigkeit = schwierigkeit;
		this.game = game;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		camera.position.set(0, 0, 0);

		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);
		Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
		btn = new TextButton("test", skin);
		// btn.setPosition(300, 300);
		// btn.setSize(300, 60);
		btn.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				btn.setText("Test success!");
				System.out.println("print");
			}

			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
			}
		});
		stage.addActor(btn);
		stage.addActor(new TextButton("test2",skin));
	}

	@Override
	public void show() {
		// Init & Setup
		entities = new ArrayList<Entity>();
		eddys = new ArrayList<Eddy>();
		addQueue = new LinkedList<Entity>();
		removeQueue = new LinkedList<Entity>();
		objective = new Objective();
		hintergrund = new Hintergrund(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), STAR_PERCENTAGE);
		level = 1;
		points = 0;
		Planet planet = new Planet(PLANET_SIZE, PLANET_ORBIT_RADIUS, PLANET_ORBIT_FORCE);
		schiff = new Schiff(ESPGame.getRandomOmega(), planet.getOrbit().getRadius() + 50, planet.getOrbit());

		this.planet = planet;

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

		this.kanone = new Kanone(new Random().nextFloat() * 360);
		entities.add(planet);
		entities.add(schiff);
		entities.add(kanone);
		this.kanonec = new KanoneController(kanone);

		this.input = new InputMultiplexer();
		input.addProcessor(kanonec);

		Gdx.input.setInputProcessor(input);

		// BEGIN RUNDE 1
		setReserved(3, 3, 3);
		newObjective(level);

		spawnHeman = true;
	}

	@Override
	public void render(float delta) {
		// Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		deltaTime += delta;
		if (deltaTime > UPDATE_TIME) {
			update();
			deltaTime -= UPDATE_TIME;
		}

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batch.setProjectionMatrix(camera.combined);
		
		game.batch.begin();
		hintergrund.render(game.batch);
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(game.batch);
		}
		for (int i = 0; i < eddys.size(); i++) {
			Eddy e = eddys.get(i);
			e.render(game.batch);
		}
		game.batch.end();

		stage.act(delta);
		stage.draw();
	}

	public void update() {
		// setSchwierigkeit(ESPGame.game.getSchwierigkeit());
		// TODO wieso haben wir das gemacht? wir ändern die schwierigkeit aber
		// gehen nie wieder darauf ein

		if (gameover && running) {
			// ui.setGameOver(true);
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

		 //shakey cam
		 if (shake_dur == 0) {
		 camera.position.x = 0;
		 camera.position.y = 0;
		 shake_mag = 0;
		 } else {
		 camera.position.x = shakeValue();
		 camera.position.y = shakeValue();
		 shake_dur--;
		 }
		camera.update();

		// Entities sicher entfernen
		while (!removeQueue.isEmpty()) {
			removeQueue.peek().onRemove();
			if (removeQueue.peek() instanceof Eddy)
				eddys.remove(removeQueue.pop());
			else
				entities.remove(removeQueue.pop());
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
						dominantEddy.createCollideExplosion();
					} else {
						Color col = Eddy.joinColor(dominantEddy.getFarbe(), noobEddy.getFarbe());
						if (col == null) {
							dominantEddy.createCollideExplosion();
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
						eddys.get(i).createCollideExplosion();
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
			if (!schiff.isBusy()) {
				gameover = !objective.checkObjective(getReserve());
			}

			// TODO obsolete?
			// // Kollision mit Planet
			// if (eddys.get(i).checkCollision(planet)) {
			// eddys.remove(i);
			// }
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
				System.out.println("He-Man ist nun in der EntityListe.");
			}
		}

	}

	public Explosion createExplosion(Vector2 position, float explosionsRadius, int lifespan) {
		Explosion e = new Explosion(position, explosionsRadius, lifespan);
		addEntity(e);

		e.removeObjects();

		return e;
	}

	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(false, width, height);
		// TODO match hintergrund to new screensize?
		camera.update();
		hintergrund.resize(width, height);
		stage.getViewport().update(width, height, true);
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
		shapeRenderer.dispose();
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

	public int getReserveRot() {
		return reserveRot;
	}

	public void setReserveRot(int reserveRot) {
		this.reserveRot = reserveRot;
	}

	public int getReserveBlau() {
		return reserveBlau;
	}

	public void setReserveBlau(int reserveBlau) {
		this.reserveBlau = reserveBlau;
	}

	public int getReserveGruen() {
		return reserveGruen;
	}

	public void setReserveGruen(int reserveGruen) {
		this.reserveGruen = reserveGruen;
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
			reserveRot += mod;
			break;
		case 1:
			reserveBlau += mod;
			break;
		case 2:
			reserveGruen += mod;
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
		/*
		 * if (temp != selectedEddy && Game.game.getSchwierigkeit() == 0)
		 * setHighlighted();
		 */
	}

	public int getSelectedEddy() {
		return selectedEddy;
	}

	public void togglePause() {
		if (!gameover) {
			paused = !paused;
			System.out.println("Is it paused? " + paused);
			if (paused) {
				// TODO Disable Kanonen Mauslistener
				// TODO Show PauseMenü
				// container.getInput().removeMouseListener(kanone);
				// Game.game.requestMenu(new
				// PauseMenu(Game.game.getContainer()));
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

	public void onRemove() {
		// TODO remove mouse listener
		// container.getInput().removeMouseListener(kanone);
	}

	public void endLevel() {
		running = false;
		// Game.game.requestMenu(new HighscoresMenu(container,
		// new Highscore(level, points, Game.getPlayerName(),
		// Game.getLevel().getSchwierigkeit())));
		// TODO show highscores
	}

	public void shakeScreen(int magnitude, int duration) {
		if (shake_dur < duration) {
			shake_dur = duration;
		}
		if (shake_mag < magnitude) {
			shake_mag = magnitude;
		}
	}

	public int shakeValue() {
		Random r = new Random();
		int i = r.nextInt(shake_mag + 1);
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

}
