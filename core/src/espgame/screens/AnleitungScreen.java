package espgame.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import espgame.ESPGame;
import espgame.resources.AssetContainer;
import espgame.resources.AssetLoader;

public class AnleitungScreen extends ESPMenu {

	public static final int BUTTONPADDING = 20;

	public AnleitungScreen() {
		super(STAR_PERCENTAGE * 0);
	}

	@Override
	public void init() {
		AssetLoader l = AssetLoader.get();
		Texture t = l.getTexture(AssetContainer.UI_ANLEITUNG);
		TextureRegionDrawable anleitung = new TextureRegionDrawable(new TextureRegion(t));
		Button backBT = getImageButton(AssetContainer.MENU, AssetContainer.MENU_A);
		backBT.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				ESPGame.game.changeMenu(new MainMenu());
			}
		});

		table.add(new Image(anleitung)).expand();
		table.row();
		table.add(backBT).padBottom(BUTTONPADDING).padTop(BUTTONPADDING);

		// stage.setDebugAll(true);
	}

}
