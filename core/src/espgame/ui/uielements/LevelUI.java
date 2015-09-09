package espgame.ui.uielements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import espgame.level.Level;
import espgame.resources.AssetContainer;
import espgame.resources.AssetLoader;
import espgame.ui.EddyStorage;
import espgame.ui.KanoneDisplayer;
import espgame.ui.ObjectiveDisplayer;

public class LevelUI extends Table {

	public static final boolean SHOW_MERGED_PROGRESSBARS = false;

	private Level level;
	private ImageButton endBT;
	private EddyStorage eddyStorage;
	private KanoneDisplayer kanoneDisplayer;
	private ObjectiveDisplayer objectiveDisplayer;

	public LevelUI(Level level, Skin skin) {
		super(skin);
		this.level = level;

		AssetLoader loader = AssetLoader.get();

		endBT = new ImageButton(new Image(loader.getTexture(AssetContainer.UI_LOGO)).getDrawable());
		endBT.setVisible(false);
		Table leftTable = new Table(skin);
		eddyStorage = new EddyStorage(level, skin);
		kanoneDisplayer = new KanoneDisplayer(level.getKanone(), skin, loader.getFont(AssetContainer.FONT_SMALL),
				SHOW_MERGED_PROGRESSBARS);
		
		objectiveDisplayer = new ObjectiveDisplayer(level.getObjective(), skin);
		setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		leftTable.add(eddyStorage).padLeft(3).padTop(3).left();
		System.out.println(eddyStorage.getWidth());
		leftTable.row();
		leftTable.add(kanoneDisplayer).left().padTop(9);
		add(leftTable).expand().left();
		add(endBT).bottom().padBottom(50);
		add(objectiveDisplayer).expand().center().right();
	}

	public void update() {
		eddyStorage.update();
		kanoneDisplayer.update();
		objectiveDisplayer.updateCheck();
		endBT.setVisible(level.isGameover());
	}

	public void updateObjective() {
		objectiveDisplayer.update();
	}
}
