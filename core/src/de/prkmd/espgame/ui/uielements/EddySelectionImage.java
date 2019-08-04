package de.prkmd.espgame.ui.uielements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import de.prkmd.espgame.resources.AssetContainer;
import de.prkmd.espgame.resources.AssetLoader;

public class EddySelectionImage extends Image {

	public static final int TEXT_OFFSET_X = -45;
	public static final int TEXT_OFFSET_Y = 25;
	
	public static final float DISABLED_FACTOR = 2f;

	private boolean active = false;
	private Drawable backround;
	private Drawable foreground;
	private Drawable eddy;
	private int count = 0;

	private BitmapFont font;
	private Color color;

	public EddySelectionImage(Drawable eddy) {
		super(eddy);
		this.eddy = eddy;
		AssetLoader loader = AssetLoader.get();
		backround = new Image(loader.getTexture(AssetContainer.UI_EDDY_SELECTOR)).getDrawable();
		foreground = new Image(loader.getTexture(AssetContainer.UI_SELECTION)).getDrawable();

		color = Color.WHITE;
		font = AssetLoader.get().getFont(AssetContainer.FONT_MEDIUM);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		// super.draw(batch, parentAlpha);
		validate();

		Color color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		
		Color originalColor = batch.getColor();
		Color disabledColor = new Color(color.r/DISABLED_FACTOR, color.g/DISABLED_FACTOR, color.b/DISABLED_FACTOR, color.a * parentAlpha);

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
		if(getCount()==0){
			batch.setColor(disabledColor);
		}
		eddy.draw(batch, x + imageX, y + imageY, imageWidth * scaleX, imageHeight * scaleY);
		batch.setColor(originalColor);
		if (isActive()) {
			foreground.draw(batch, x + imageX, y + imageY, imageWidth * scaleX, imageHeight * scaleY);
		}

		font.draw(batch, "x" + getCount(), x + imageX + imageWidth + TEXT_OFFSET_X, y + imageY + TEXT_OFFSET_Y);
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
