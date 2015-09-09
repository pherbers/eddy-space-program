package espgame.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import espgame.ESPGame;
import espgame.level.Planet;
import espgame.resources.AssetContainer;
import espgame.resources.AssetLoader;
import espgame.resources.Fontsize;
import espgame.util.VectorUtils;

/**
 * Created by Patrick on 26.08.2015.
 */

public class Kanone extends Entity {

    private static final float MAX_WINKEL = 90f, MIN_WINKEL = -90f;
    public static final int MAXCOOLDOWN = 60, MAXFORCE = 10, MINFORCE = 1;
    private static final float SURFACE_DISTANCE = 24;
    
    public static final int NO_AMMO_DUR = 90;
    public static final String NO_AMMO_TEXT = "Keine Eddys!";
    public static final float SHAKE_MAGNITUDE = 3;
    public static final int SHAKE_DURATION = 15;

    private Sprite base, top;
    //private Shape farbe;
    //private org.newdawn.slick.Color farbeFill;

    // private boolean active = false;
    private float alpha; // Drehung der Kanone auf der Oberflaeche
    private float beta; // Drehung der Kanone zum schiessen
    private float omega; // Offset für Kanonenrotation auf Planeten
    private float sinalpha, cosalpha;
    private int cooldown;
    private float kraft;
    private Planet planet;
    private Sprite farbBackground;

    // Maussteuerung
    private Vector2 mausposition;
    private boolean mouseDown, shoot = false;

    public Kanone(float omega) {
        super(0, 0);
        this.omega = omega;
        planet = ESPGame.getLevel().getPlanet();
        sinalpha = MathUtils.sin(alpha);
        cosalpha = MathUtils.cos(alpha);
        position = new Vector2(-cosalpha * (planet.getRadius() + 16),
                -sinalpha * (planet.getRadius() + 16));
        explodeable = false;
        collidable = false;
        beta = 0;
        kraft = 0;
        base = new Sprite(AssetLoader.get().getTexture(AssetContainer.KANONE_BASE));
        top = new Sprite(AssetLoader.get().getTexture(AssetContainer.KANONE_TOP));
        Pixmap p = new Pixmap(32, 32, Pixmap.Format.RGB888);
        p.setColor(Color.WHITE);
        p.fill();
        farbBackground = new Sprite(new Texture(p));
        p.dispose();
        farbBackground.setColor(Color.RED);

        top.setOriginCenter();
        base.setOriginCenter();
        farbBackground.setOriginCenter();

        updatePosition();
//        farbe = new Rectangle((float) planet.getRadius(), 0, 30, 30);
//        farbe = farbe.transform(Transform.createTranslateTransform(15, -15));
//        farbe = farbe.transform(Transform.createRotateTransform((float) alpha));
        // setzeFarbe();

        mausposition = new Vector2(position.x*2,position.y*2);

    }

    private void setzeFarbe() {
        int x = ESPGame.getLevel().getSelectedEddy();
        switch (x) {
            case 0:
                farbBackground.setColor(Color.RED);
                break;
            case 1:
                farbBackground.setColor(new Color(0.0f, 0.2f, 1.0f, 1.0f));
                break;
            case 2:
                farbBackground.setColor(Color.GREEN);
            default:
                break;
        }
    }

	/*
	 * public void aktivieren() { active = true; }
	 *
	 * public void deaktivieren() { active = false; }
	 */

	/*
	 * private void drehen(double winkel) { if (winkel != 0.0) { beta += winkel;
	 * if (beta < MIN_WINKEL) { beta = MIN_WINKEL; return; } else if (beta >
	 * MAX_WINKEL) { beta = MAX_WINKEL; return; } top.setRotation((float)
	 * ((alpha + beta) / Math.PI * 180) + 90f); farbe =
	 * farbe.transform(Transform.createRotateTransform((float) winkel, (float)
	 * (position.getX()), (float) (position.getY()))); } //Game.print(beta);
	 *
	 * }
	 */

    private void setDrehung(float winkel) {
        beta = winkel;
        // Game.print(beta);
        if (beta < MIN_WINKEL) {
            beta = MIN_WINKEL;

        } else if (beta > MAX_WINKEL) {
            beta = MAX_WINKEL;
        }
        top.setRotation(alpha + beta + 90f);
        /*farbe = new Rectangle(15, -15, 30, 30);
        farbe = farbe.transform(Transform.createTranslateTransform(
                (float) (planet.getRadius()), 0));
        farbe = farbe.transform(Transform
                .createRotateTransform((float) alpha));
        farbe = farbe.transform(Transform.createRotateTransform(
                (float) beta, (float) (position.getX()),
                (float) (position.getY())));*/

    }

    @Override
    public void render(SpriteBatch batch) {

        // g.setAntiAlias(true);
        base.draw(batch);
//        g.setColor(farbeFill);
//        g.fill(farbe);
//        g.setColor(org.newdawn.slick.Color.white);
        farbBackground.draw(batch);
        top.draw(batch);
        /*if (Game.DEBUG) {
            g.drawRect((float) position.getX(), (float) position.getY(), 10, 5);
            g.drawLine((float) position.getX(), (float) position.getY(),
                    (float) (position.getX() + Math.cos(alpha + beta) * kraft
                            * 10),
                    (float) (position.getY() + Math.sin(alpha + beta) * kraft
                            * 10));

            g.drawString("" + kraft, -400, -300);
        }*/
        // g.setAntiAlias(false);
    }

    public void updatePosition() {
        float planetRotation = planet.getRotation() % 360;
        alpha = planetRotation + omega;
        cosalpha = MathUtils.cosDeg(alpha);
        sinalpha = MathUtils.sinDeg(alpha);
        position.x = -cosalpha * (planet.getRadius() + SURFACE_DISTANCE);
        position.y = -sinalpha * (planet.getRadius() + SURFACE_DISTANCE);

        top.setCenter(position.x, position.y);
        base.setCenter(position.x, position.y);
        base.setRotation(alpha + 90f);
        top.setRotation(alpha + beta + 90f);
        farbBackground.setCenter(position.x - MathUtils.cosDeg(beta + alpha) * 16, position.y - MathUtils.sinDeg(beta + alpha) * 16);
        farbBackground.setRotation(alpha + beta + 90f);

    }

    @Override
    public void update() {
        setzeFarbe();
//        forcebar.setValue(kraft);
//        cooldownbar.setValue(cooldown);
		/*
		 * double drehwinkel = 0.0; if (Game.game.isKey_left()) drehwinkel -=
		 * DREHGESCHWINDIGKEIT; if (Game.game.isKey_right()) drehwinkel +=
		 * DREHGESCHWINDIGKEIT; drehen(drehwinkel); if (Game.game.isKey_down())
		 * kraft -= 0.1; if (Game.game.isKey_up()) kraft += 0.1;
		 */
        updateDrehung();
        updatePosition();
        if (mouseDown)
            kraft += 0.1;
        if (kraft > MAXFORCE)
            shoot = true;
        if (shoot) {
            shoot = false;
            if (cooldown == 0 && kraft > MINFORCE) {
                float x = MathUtils.cosDeg(alpha + beta + 180) * kraft;
                float y = MathUtils.sinDeg(alpha + beta + 180) * kraft;
                Eddy.Color farbe;
                int selectedEddy = ESPGame.getLevel().getSelectedEddy();
                switch (selectedEddy) {
                    case 0:
                        farbe = Eddy.Color.ROT;
                        break;
                    case 1:
                        farbe = Eddy.Color.BLAU;
                        break;
                    case 2:
                        farbe = Eddy.Color.GRUEN;
                        break;
                    default:
                        farbe = Eddy.Color.ROT;
                        break;
                }
                if (hasAmmo(selectedEddy)) {
                    Eddy eddy = new Eddy(position.x + x / (float)Math.hypot(x, y) * 64,
                            position.y + y / (float)Math.hypot(x, y) * 64,
                            x, y, farbe, ESPGame.getLevel().getPlanet().getOrbit()); // Farbe
                    // bestimmen
                    ESPGame.getLevel().addEntity(eddy);
                    
                    Sound pop = AssetLoader.get().getSound(AssetContainer.SOUND_POP);
                    pop.stop();
                    pop.play();
                    //ESPGame.print("Neuer Eddie erstellt!");
                    //if (!Sounds.POP.playing()) {
                    //Sounds.POP.play((float) Math.sqrt(kraft), 1.0f);
                    //}
                    ESPGame.getLevel().modReserve(selectedEddy, -1);
                } else {
                    Sound empty = AssetLoader.get().getSound(AssetContainer.SOUND_KANON_EMPTY);
                    empty.stop();
                    empty.play();
                    ESPGame.getLevel().shakeScreen(SHAKE_MAGNITUDE, SHAKE_DURATION);
                    ESPGame.getLevel().createTextDisplayer(getPosition(), VectorUtils.randomNormalized().scl(0.5f), NO_AMMO_DUR, NO_AMMO_TEXT, Fontsize.Klein);
                }
                cooldown = MAXCOOLDOWN;
            }
            kraft = 0.0f;
        }
        if (cooldown > 0)
            cooldown--;
    }

    public boolean hasAmmo(int farbe) {
        int c;
        switch (farbe) {
            case 0:
                c = ESPGame.getLevel().getReserveRot();
                break;
            case 1:
                c = ESPGame.getLevel().getReserveBlau();
                break;
            case 2:
                c = ESPGame.getLevel().getReserveGruen();
                break;
            default:
                throw new IllegalArgumentException(
                        "Wrong arguments in 'hasAmmo()' in 'Kanone.java'. Only valid integer-arguments: 0, 1, 2.");
        }
        return c > 0;
    }

	public void mousePressed(int button, int x, int y) {
		if (button == 0)
			mouseDown = true;
		else if (button == 1) {
			// int i = ESPGame.getLevel().getSelectedEddy();
			// i++;
			// i = i % 3;
			// ESPGame.getLevel().setSelectedEddy(i);
		}
	}

	public void startChargeup() {
		mouseDown = true;
	}

	public void shoot() {
		mouseDown = false;
		shoot = true;
	}
	
	public int getCoolodown(){
		return cooldown;
	}
	
	public float getKraft(){
		return kraft;
	}

	public void updateMousePosition(int newx, int newy) {
		Vector3 worldCoords = ESPGame.getLevel().getCamera().unproject(new Vector3(newx, newy, 0));
		mausposition.set(worldCoords.x, worldCoords.y);
		updateDrehung();
	}

	public void updateDrehung() {
		float winkel = MathUtils.atan2(mausposition.y - getY(), mausposition.x - getX()) * MathUtils.radiansToDegrees
				- alpha + 180;
		if (winkel > 180)
			winkel -= 360;
		else if (winkel < -180)
			winkel += 360;
		setDrehung(winkel);
	}
}
