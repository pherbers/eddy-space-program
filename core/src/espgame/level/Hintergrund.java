package espgame.level;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import espgame.ESPGame;

public class Hintergrund {
	public static final int MAXSTERNDISTANZ = 150, MINDISZANZ = 70;
	public static final int MAXMEMBERS = 5;
	public static final boolean SHOWSTARS = false;
	float sizeX, sizeY;
	ArrayList<Stern> sterne;
	int sterneAnzahl;
	boolean offset;

	public Hintergrund(float sizeX, float sizeY, int anzahlSterne, boolean offset) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		sterneAnzahl = anzahlSterne;
		this.offset = offset;
		if (offset) {
			this.sizeX /= ESPGame.getGamescale();
			this.sizeY /= ESPGame.getGamescale();
		}
		sterne = new ArrayList<Stern>(sterneAnzahl);
		init();
	}

	public void init() {
		Random r = new Random();
		if (offset)
			for (int i = 0; i < sterneAnzahl; i++) {
				sterne.add(new Stern(r.nextFloat() * sizeX - ESPGame.getRenderWidth() / ESPGame.getGamescale(),
						r.nextFloat() * sizeY - ESPGame.getRenderHeight() / ESPGame.getGamescale(),
						r.nextFloat() * 5f + 0.5f));
			}
		else
			for (int i = 0; i < sterneAnzahl; i++) {
				sterne.add(new Stern(r.nextFloat() * sizeX, r.nextFloat() * sizeY, r.nextFloat() * 5f + 0.5f));
			}

		// Sternbilder
		if (SHOWSTARS) {
			for (Stern a : sterne) {
				for (Stern b : sterne) {
					if (a.getDistance(b) < MAXSTERNDISTANZ && a.getDistance(b) > MINDISZANZ) {
						if (a.hasParent()) {
							if (a.getDistance(b) < a.getDistance(a.getParent()))
								a.setParent(b);
						} else {
							a.setParent(b);
						}
					}
				}
			}

			for (Stern s : sterne) {
				if (r.nextInt(100) < 6) {
					s.addToSternbild(0);
				}
			}
		}
	}

	public void render() {
		// g.setColor(Color.black);
		// if (offset)
		// g.fillRect((float) -sizeX / 2f, (float) -sizeY / 2f, (float) sizeX,
		// (float) sizeY);
		// else
		// g.fillRect((float) 0, (float) 0, (float) sizeX, (float) sizeY);
		// g.setColor(Color.white);
		// for (Stern s : sterne)
		// s.render(g);
		// TODO render
	}

	private class Stern {
		double x, y, size;
		private boolean inSternbild;
		private Stern parent;
		private Vector2 position;

		public Stern(float x, float y, float size) {
			this.x = x;
			this.y = y;
			this.size = size;
			position = new Vector2(x, y);
		}

		void render() {
			// // g.fillOval((float)x, (float) y, (float) size, (float) size);
			// Sprites.STERN.draw((float) x, (float) y, (float) size, (float)
			// size);
			// if (parent != null && inSternbild) {
			// g.drawLine((float) x, (float) y, (float) parent.x, (float)
			// parent.y);
			// }
			// TODO stern rendern
		}

		@Deprecated
		public double getDistance(Stern s) {
			return position.dst2(s.position);
		}

		@Deprecated
		public boolean hasParent() {
			return parent != null;
		}

		@Deprecated
		public void setParent(Stern s) {
			parent = s;
		}

		@Deprecated
		public Stern getParent() {
			return parent;
		}

		@Deprecated
		public void addToSternbild(int c) {
			inSternbild = true;
			c++;
			if (hasParent() && c <= MAXMEMBERS && parent.getParent() != this)
				parent.addToSternbild(c);
		}
	}
}
