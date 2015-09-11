package espgame.level;

import java.util.Random;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;

public class Hintergrund {
	int sizeX, sizeY;
	float sterneProzent;
	boolean offset;
	Texture texture;

	public Hintergrund(int sizeX, int sizeY, float prozentSterne) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		sterneProzent = prozentSterne;

		init();
	}

	public void init() {
		Random r = new Random();
		Pixmap pixmap;
		pixmap = new Pixmap(sizeX, sizeY, Pixmap.Format.RGB888);
		int sterneAnzahl = (int) (sizeX * sizeY * sterneProzent);
		for (int i = 0; i < sterneAnzahl; i++) {
			pixmap.setColor(Color.WHITE);
			pixmap.fillCircle(r.nextInt(sizeX), r.nextInt(sizeY),r.nextInt(2));
		}
		if (texture != null)
			texture.dispose();
		texture = new Texture(pixmap);
		texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		pixmap.dispose();
	}

	public void resize(int x, int y) {
		this.sizeX = x;
		this.sizeY = y;
		texture.dispose();
		init();
	}

	public void render(Batch batch) {
		batch.draw(texture, -sizeX / 2, -sizeY / 2, sizeX, sizeY);
	}

	public void renderMenu(Batch batch) {
		batch.draw(texture,0,0, sizeX, sizeY);
	}

}
