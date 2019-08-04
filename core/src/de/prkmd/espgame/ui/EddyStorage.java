package de.prkmd.espgame.ui;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.prkmd.espgame.level.Level;
import de.prkmd.espgame.resources.AssetContainer;
import de.prkmd.espgame.resources.AssetLoader;
import de.prkmd.espgame.ui.uielements.EddySelectionImage;

public class EddyStorage extends Table {

	private Level level;
	private EddySelectionImage blau, gruen, rot;
	private ArrayList<EddySelectionImage> list;
	private LevelDisplayer displayer;

	public EddyStorage(Level level, Skin skin) {
		this.level = level;

		AssetLoader loader = AssetLoader.get();
		BitmapFont font = loader.getFont(AssetContainer.FONT_SMALL);

		Image r = new Image(loader.getTexture(AssetContainer.EDDY_ROT));
		Image g = new Image(loader.getTexture(AssetContainer.EDDY_GRUEN));
		Image b = new Image(loader.getTexture(AssetContainer.EDDY_BLAU));
		rot = new EddySelectionImage(r.getDrawable());
		gruen = new EddySelectionImage(g.getDrawable());
		blau = new EddySelectionImage(b.getDrawable());

		list = new ArrayList<EddySelectionImage>();
		list.add(rot);
		list.add(blau);
		list.add(gruen);

		for (int i = 0; i < list.size(); i++) {
			add(list.get(i));
			row();
		}

		displayer = new LevelDisplayer(level, skin, font);
		add(displayer).padTop(8).fill();
	}

	public void update() {
		for (int i = 0; i < list.size(); i++) {
			EddySelectionImage im = list.get(i);
			im.setActive(i == level.getSelectedEddy());
			im.setCount(level.getReserve(i));
		}
		displayer.update();
	}
}
