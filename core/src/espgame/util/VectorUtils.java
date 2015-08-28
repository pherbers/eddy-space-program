package espgame.util;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;

public abstract class VectorUtils {

	public static Vector2 up() {
		return new Vector2(0f, -1.5f);
	}

	public static Vector2 down() {
		return new Vector2(0f, 1.5f);
	}

	public static Vector2 random(int maxX, int maxY) {
		Random r = new Random();
		return new Vector2(r.nextInt(maxX), r.nextInt(maxY));
	}
	
	public static Vector2 random() {
		Random r = new Random();
		return new Vector2(r.nextInt(), r.nextInt());
	}
}
