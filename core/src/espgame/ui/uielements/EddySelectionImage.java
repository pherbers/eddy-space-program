package espgame.ui.uielements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable;

import espgame.resources.AssetContainer;
import espgame.resources.AssetLoader;

public class EddySelectionImage extends Image {

	private boolean active = false;
	private Drawable backround;
	private Drawable foreground;
	private Drawable eddy;
	private int count = 0;

	public EddySelectionImage(Drawable eddy) {
		super(eddy);
		this.eddy = eddy;
		AssetLoader loader = AssetLoader.get();
		backround = new Image(loader.getTexture(AssetContainer.UI_EDDY_SELECTOR)).getDrawable();
		foreground = new Image(loader.getTexture(AssetContainer.UI_SELECTION)).getDrawable();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		validate();

		Color color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

		float imageX = getImageX();
		float imageY = getImageY();
		float imageWidth = getImageWidth();
		float imageHeight = getImageHeight();
		float x = getX();
		float y = getY();
		float scaleX = getScaleX();
		float scaleY = getScaleY();

		// if (eddy instanceof TransformDrawable) {
		// float rotation = getRotation();
		// if (scaleX != 1 || scaleY != 1 || rotation != 0) {
		// ((TransformDrawable)eddy).draw(batch, x + imageX, y + imageY,
		// getOriginX() - imageX, getOriginY() - imageY,
		// imageWidth, imageHeight, scaleX, scaleY, rotation);
		// return;
		// }
		// }

		backround.draw(batch, x + imageX, y + imageY, imageWidth * scaleX, imageHeight * scaleY);
		eddy.draw(batch, x + imageX, y + imageY, imageWidth * scaleX, imageHeight * scaleY);
		if (isActive()) {
			foreground.draw(batch, x + imageX, y + imageY, imageWidth * scaleX, imageHeight * scaleY);
		}
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
