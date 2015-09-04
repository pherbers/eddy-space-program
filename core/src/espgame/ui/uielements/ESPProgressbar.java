package espgame.ui.uielements;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class ESPProgressbar extends ProgressBar {

	private Drawable backround, foreground;

	public ESPProgressbar(float min, float max, Skin skin) {
		this(min, max, skin, null, null);
	}

	public ESPProgressbar(float min, float max, Skin skin, Drawable backround, Drawable foreground) {
		super(min, max, 1, false, skin);

		this.setBackround(backround);
		this.setForeground(foreground);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		// TODO Auto-generated method stub
		super.draw(batch, parentAlpha);
	}

	public Drawable getBackround() {
		return backround;
	}

	public void setBackround(Drawable backround) {
		this.backround = backround;
	}

	public Drawable getForeground() {
		return foreground;
	}

	public void setForeground(Drawable foreground) {
		this.foreground = foreground;
	}
}
