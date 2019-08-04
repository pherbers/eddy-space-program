package de.prkmd.espgame.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.prkmd.espgame.entity.Kanone;
import de.prkmd.espgame.ui.uielements.ESPProgressbar;

public class KanoneDisplayer extends Table {

	public static final float ANIMATE_DURATION = .1f;
	public static final String PROGRESSBAR_LABEL_KRAFT = "Kraft";
	public static final String PROGRESSBAR_LABEL_CD = "Cooldown";

	private Kanone kanone;
	private ESPProgressbar forceBar, cooldownBar;
	private boolean mergeBars;

	private float oldForce = 0;

	public KanoneDisplayer(Kanone kanone, Skin skin, BitmapFont font, boolean mergeBars) {
		this.kanone = kanone;
		this.mergeBars = mergeBars;

		ProgressBarStyle style = new ProgressBarStyle();
		style.knobAfter = skin.getDrawable("textfield");
		style.knobBefore = skin.getDrawable("esptextfield");

		forceBar = new ESPProgressbar(0, Kanone.MAXFORCE * 10, 1, false, style, PROGRESSBAR_LABEL_KRAFT, font);
		cooldownBar = new ESPProgressbar(0, Kanone.MAXCOOLDOWN, 1, false, style, PROGRESSBAR_LABEL_CD, font);
		
		add(forceBar).padTop(2).expand();
		if (!mergeBars) {
			row();
			add(new Label("", skin));
			row();
			add(cooldownBar).padTop(-20);
		}
	}

	public void update() {
		if (kanone == null)
			return;

		float cd = kanone.getCoolodown();
		float force = kanone.getKraft();
		if (mergeBars) {
			float maxCD = Kanone.MAXCOOLDOWN;
			if (cd > 0) {
				float cooldownProgress = cd / maxCD;
				System.out.println(cooldownProgress);
				forceBar.setValue(oldForce * cooldownProgress);
			} else {
				oldForce = force * 10;
				forceBar.setValue(force * 10);
			}
		} else {
			forceBar.setValue(force * 10);
			cooldownBar.setValue(cd);
		}
	}

	public void setKanone(Kanone kanone) {
		this.kanone = kanone;

	}

}
