package espgame.ui;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import espgame.level.Level;
import espgame.resources.AssetContainer;
import espgame.resources.AssetLoader;
import espgame.ui.uielements.EddySelectionImage;

public class EddyStorage extends Table {

	private Level level;
	private EddySelectionImage blau, gruen, rot;
	private ArrayList<EddySelectionImage> list;

	public EddyStorage(Level level) {
		this.level = level;

		AssetLoader loader = AssetLoader.get();

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

		
		for(int i = 0;i<list.size();i++){
			add(list.get(i));
			row();
		}
	}

	public void update() {
		for(int i = 0;i<list.size();i++){
			list.get(i).setActive(i == level.getSelectedEddy());
		}
	}

}
