package espgame.ui.uielements;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class ESPProgressbar extends ProgressBar {

	public static final int TEXT_OFFSET_X = 15;
	public static final int TEXT_OFFSET_Y = 5;

	private String text;
	private BitmapFont font;

	public ESPProgressbar(float min, float max, float stepSize, boolean vertical, ProgressBarStyle style) {
		this(min, max, stepSize, vertical, style, "", null);
	}

	public ESPProgressbar(float min, float max, float stepSize, boolean vertical, ProgressBarStyle style, String text,
			BitmapFont font) {
		super(min, max, stepSize, vertical, style);
		setText(text);
		setFont(font);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

		if (font == null)
			return;
		font.draw(batch, getText(), getX() * getScaleX() + TEXT_OFFSET_X, getY() * getScaleY() + TEXT_OFFSET_Y);
	}

	public BitmapFont getFont() {
		return font;
	}

	public void setFont(BitmapFont font) {
		this.font = font;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
