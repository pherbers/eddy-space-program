package espgame.ui;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import espgame.entity.Kanone;

public class KanoneDisplayer extends Table {

	public static final float ANIMATE_DURATION = .1f;

	private Kanone kanone;
	private ProgressBar forceBar;
	private int i = 0;

	public KanoneDisplayer(Kanone kanone, Skin skin) {
		this.kanone = kanone;
		forceBar = new ProgressBar(Kanone.MINFORCE, Kanone.MAXFORCE * 10, 1, false, skin);
		// forceBar.setAnimateDuration(ANIMATE_DURATION);

		add(forceBar);
	}

	public void update() {
		if (kanone == null)
			return;

		int cd = kanone.getCoolodown();
		float force = kanone.getKraft();

		forceBar.setValue(kanone.getKraft() * 10);
	}

	public void setKanone(Kanone kanone) {
		this.kanone = kanone;

	}

}
