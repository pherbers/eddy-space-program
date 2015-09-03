package espgame.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import espgame.level.Level;

public class LevelDisplayer extends Table {
	public static final Color LABEL_COLOR = Color.WHITE;

	private Level level;
	private Label progress, punkte;

	public LevelDisplayer(Level level, Skin skin, BitmapFont font) {
		super(skin);
		this.level = level;

		progress = new Label("", skin);
		LabelStyle style = new LabelStyle(font, LABEL_COLOR);
		style.background = skin.getDrawable("esptextfield");
		progress.setStyle(style);

		punkte = new Label("", skin);
		punkte.setStyle(style);

		add(progress).fill().expand();
		row().expand();
		add(punkte).fill().expand();

		update();
	}

	public void update() {
		progress.setText("Level: " + level.getLevel());
		punkte.setText("Punkte: " + level.getPunkte());
	}
}
