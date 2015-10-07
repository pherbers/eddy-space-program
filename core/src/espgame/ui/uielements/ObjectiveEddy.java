package espgame.ui.uielements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import espgame.ESPGame;
import espgame.resources.AssetContainer;
import espgame.resources.AssetLoader;

public class ObjectiveEddy extends Image {

	public static final int NO_TICK_DIFF = 2;

	private Drawable tick;
	private Drawable backround;
	private Drawable border;
	private boolean erledigt;

	public ObjectiveEddy(Drawable eddy) {
		super(eddy);

		tick = new Image(AssetLoader.get().getTexture(AssetContainer.UI_TICK)).getDrawable();
		backround = new Image(AssetLoader.get().getTexture(AssetContainer.UI_OBJECTIVE_BACKGROUND)).getDrawable();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

		float imageX = getImageX();
		float imageY = getImageY();
		float imageWidth = getImageWidth();
		float imageHeight = getImageHeight();
		float x = getX();
		float y = getY();
		float scaleX = getScaleX();
		float scaleY = getScaleY();

		if (backround != null)
			backround.draw(batch, x + imageX, y + imageY, imageWidth * scaleX, imageHeight * scaleY);

		getDrawable().draw(batch, x + imageX, y + imageY, imageWidth * scaleX, imageHeight * scaleY);

		int diff = ESPGame.getLevel().getSchwierigkeit();
		if (erledigt && diff < NO_TICK_DIFF) {
			float w = (imageWidth * scaleX) / 3;
			float h = (imageHeight * scaleY) / 3;
			tick.draw(batch, (x + imageX), (y + imageY), w, h);
		}

		border.draw(batch, x + imageX, y + imageY, imageWidth * scaleX, imageHeight * scaleY);
	}

	public boolean isErledigt() {
		return erledigt;
	}

	public void setErledigt(boolean erledigt) {
		this.erledigt = erledigt;
	}

	public void setBorder(Drawable border) {
		this.border = border;
	}
}
