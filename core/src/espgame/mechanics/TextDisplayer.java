package espgame.mechanics;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import espgame.entity.Entity;
import espgame.resources.AssetLoader;

public class TextDisplayer extends Entity {

	public static final int MINLIFESPAN = 100;
	public static final Color DEFAULT_COLOR = Color.WHITE;

	private BitmapFont font;
	private String text;
	private Color color;

	@Deprecated
	public TextDisplayer(float x, float y, Vector2 velocity, String text, BitmapFont font, int duration, Color color) {
		super(x, y);
		this.font = font;
		this.text = text;
		this.velocity = velocity;
		this.color = color;
		setlifespan(duration);

		init();
	}

	public TextDisplayer(float x, float y, Vector2 velocity, String text, int duration) {
		super(x, y);
		this.font = AssetLoader.get().getFont();
		this.text = text;
		this.velocity = velocity;
		setlifespan(duration);

		init();
	}

	private void init() {
		disableInterruptions();
		if (lifespan < MINLIFESPAN)
			setlifespan(MINLIFESPAN);
		if (color == null) {
			color = DEFAULT_COLOR;
		}
	}

	@Override
	public void render(SpriteBatch batch) {
		font.setColor(getColor());
		font.draw(batch, text, position.x, position.y);
	}

	@Override
	public void update() {
		position.add(velocity);
		ticklifespan();
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
