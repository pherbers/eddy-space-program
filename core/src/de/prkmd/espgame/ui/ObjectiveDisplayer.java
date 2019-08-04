package de.prkmd.espgame.ui;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import de.prkmd.espgame.ESPGame;
import de.prkmd.espgame.entity.Eddy;
import de.prkmd.espgame.level.Objective;
import de.prkmd.espgame.resources.AssetContainer;
import de.prkmd.espgame.resources.AssetLoader;
import de.prkmd.espgame.ui.uielements.ObjectiveEddy;

public class ObjectiveDisplayer extends Table {

	private Drawable borderTop, borderSide, borderOL, borderUL, backgroundTop, backgroundStruct, backgroundBot;

	private Objective objective;
	private ArrayList<Image> eddyImgList;
	private ArrayList<ObjectiveEddy> displayedEddys;
	private Image tick;

	public ObjectiveDisplayer(Objective objective, Skin skin) {
		this.objective = objective;

		eddyImgList = new ArrayList<Image>();
		AssetLoader loader = AssetLoader.get();

		borderOL = skin.getDrawable("espborderCornerOL");
		borderUL = skin.getDrawable("espborderCornerOL");
		borderTop = skin.getDrawable("objectiveBack");
		borderSide = skin.getDrawable("espborderHorizontal");

		backgroundBot = new Image(loader.getTexture(AssetContainer.UI_OBJECTIVE_BOT)).getDrawable();
		backgroundStruct = new Image(loader.getTexture(AssetContainer.UI_OBJECTIVE_LEFT)).getDrawable();
		backgroundTop = new Image(loader.getTexture(AssetContainer.UI_OBJECTIVE_TOP)).getDrawable();

		// borderTop = skin.getDrawable("esptextfield");

		eddyImgList.add(new Image(loader.getTexture(AssetContainer.EDDY_ROT)));
		eddyImgList.add(new Image(loader.getTexture(AssetContainer.EDDY_BLAU)));
		eddyImgList.add(new Image(loader.getTexture(AssetContainer.EDDY_GRUEN)));
		eddyImgList.add(new Image(loader.getTexture(AssetContainer.EDDY_GELB)));
		eddyImgList.add(new Image(loader.getTexture(AssetContainer.EDDY_CYAN)));
		eddyImgList.add(new Image(loader.getTexture(AssetContainer.EDDY_MAGENTA)));
		eddyImgList.add(new Image(loader.getTexture(AssetContainer.EDDY_WEISS)));
		tick = new Image(loader.getTexture(AssetContainer.UI_TICK));

//		displayedEddys = new ArrayList<ObjectiveEddy>();
	}

	public void update() {
		clearChildren();
		displayedEddys = new ArrayList<ObjectiveEddy>();

		for (int i = 0; i < objective.size(); i++) {
			int col = objective.get(i).getIndex();
			ObjectiveEddy e = new ObjectiveEddy(eddyImgList.get(col).getDrawable());
			displayedEddys.add(e);
			add(e);
			row();
		}

		for (int i = 0; i < displayedEddys.size(); i++) {
			Drawable d = backgroundStruct;
			if (i == 0) {
				d = backgroundTop;
			}
			if (i == displayedEddys.size()-1) {
				d = backgroundBot;
			}
			displayedEddys.get(i).setBorder(d);
		}

		updateCheck();
	}

	public void updateCheck() {
		if(displayedEddys==null)return;
		
		boolean[] check = updateDone();
		for (int i = 0; i < displayedEddys.size(); i++) {
			ObjectiveEddy e = displayedEddys.get(i);
			e.setErledigt(check[i]);
		}
	}

	private boolean[] updateDone() {
		int i = ESPGame.getLevel().getEddyCount();
		boolean[] check = new boolean[objective.size()];

		for (int j = 0; j < check.length; j++)
			check[j] = false;

		while (i > 0) {
			for (int j = 0; j < objective.size(); j++) {
				if (check[j])
					continue;
				Eddy e = ESPGame.getLevel().getEddy(i - 1);
				if (e.getColor() == objective.get(j) && (e.getState() != 2)) {
					check[j] = true;
					break;
				}
			}
			i--;
		}
		return check;
	}

	public void setObjective(Objective objective) {
		this.objective = objective;
	}

}
