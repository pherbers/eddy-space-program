package espgame.ui;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import espgame.level.Objective;
import espgame.resources.AssetContainer;
import espgame.resources.AssetLoader;

public class ObjectiveDisplayer extends Table {

	private Objective objective;
	private ArrayList<Image> eddyImgList;
	private Image tick;

	public ObjectiveDisplayer(Objective objective) {
		this.objective=objective;
		
		eddyImgList = new ArrayList<Image>();
		AssetLoader loader = AssetLoader.get();

		eddyImgList.add(new Image(loader.getTexture(AssetContainer.EDDY_ROT)));
		eddyImgList.add(new Image(loader.getTexture(AssetContainer.EDDY_BLAU)));
		eddyImgList.add(new Image(loader.getTexture(AssetContainer.EDDY_GRUEN)));
		eddyImgList.add(new Image(loader.getTexture(AssetContainer.EDDY_MAGENTA)));
		eddyImgList.add(new Image(loader.getTexture(AssetContainer.EDDY_CYAN)));
		eddyImgList.add(new Image(loader.getTexture(AssetContainer.EDDY_GELB)));
		eddyImgList.add(new Image(loader.getTexture(AssetContainer.EDDY_WEISS)));
		tick = new Image(loader.getTexture(AssetContainer.UI_TICK));
	}
	
	public void update(){
		clearChildren();
		
		System.out.println("Objective displayer update");
		
		int[] list = objective.getColorCount();
		for(int i = 0;i<list.length;i++){
			int count = list[i];
			for(int j=0;j<count;j++){
				add(new Image(eddyImgList.get(i).getDrawable()));
				row();
			}
		}
	}
	
	public void setObjective(Objective objective){
		this.objective=objective;
	}

}
