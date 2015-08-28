package espgame.mechanics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import espgame.entity.Entity;

public class TextDisplayer extends Entity{

	public static final int MINLIFESPAN = 100;

	private BitmapFont font;
	private String text;
	private Color color;

	public TextDisplayer(float x, float y, Vector2 velocity, String text, BitmapFont font, int duration, Color color) {
		super(x, y);
		this.font = font;
		this.text=text;
		this.velocity = velocity;
		this.color = color;
		
		disableInterruptions();
		setlifespan(duration);
		if (lifespan < MINLIFESPAN)
			setlifespan(MINLIFESPAN);
	}

	@Override
	public void render(SpriteBatch batch) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        batch.begin();
		font.setColor(color);
        font.draw(batch, text, position.x, position.y);
        batch.end();
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
