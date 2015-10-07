package espgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import espgame.ESPGame;
import espgame.resources.AssetContainer;
import espgame.resources.AssetLoader;
import espgame.ui.menus.ESPMenu;
import espgame.ui.menus.MainMenu;
import espgame.ui.uielements.ESPSlider;

public class OptionsScreen extends ESPMenu {

	public static final float SLIDER_MIN = 0;
	public static final float SLIDER_MAX = 1;
	public static final float SLIDER_STEPSIZE = 0.01f;
	public static final float SLIDER_BUTTON_MOD = SLIDER_STEPSIZE * 10;
	public static final float SLIDER_PADDING = 10;

	public static final float SLIDER_SIZE_FACTOR = 4.1337f;

	private ESPSlider musicSlider, soundSlider;
	private Table musicTable, soundTable;
	private Label schwierigkeitsLB;

	public OptionsScreen() {
		super(STAR_PERCENTAGE * 1.7f);
	}

	@Override
	public void init() {
		table.add().padBottom(20);
		table.row();

		musicTable = new Table(skin);
		TextButton toggleMusic = new TextButton("Toggle Music", skin);
		musicSlider = new ESPSlider(SLIDER_MIN, SLIDER_MAX, SLIDER_STEPSIZE, SLIDER_BUTTON_MOD, SLIDER_PADDING, skin) {
			@Override
			public void valueChanged() {
				Music m = AssetLoader.get().getMusic(AssetContainer.MUSIC);
				m.setVolume(musicSlider.getValue());
			}
		};
		musicTable.add(toggleMusic).right().padRight(SLIDER_PADDING);
		musicTable.add(musicSlider).expand().fill();
		table.add(musicTable).expand().fillX().bottom().padBottom(10);
		table.row();

		soundTable = new Table(skin);
		TextButton toggleSound = new TextButton("Toggle Sound", skin);
		soundSlider = new ESPSlider(SLIDER_MIN, SLIDER_MAX, SLIDER_STEPSIZE, SLIDER_BUTTON_MOD, SLIDER_PADDING, skin) {
			@Override
			public void valueChanged() {
				System.out.println("dummy");
				// TODO do
			}
		};
		soundTable.add(toggleSound).right().padRight(SLIDER_PADDING);
		soundTable.add(soundSlider).expand().fill();
		table.add(soundTable).expand().fillX().top().padTop(10);

		Music m = AssetLoader.get().getMusic(AssetContainer.MUSIC);
		musicSlider.setValue(m.getVolume());

		table.row();
		Table mid = new Table(skin);
		Table buttons = new Table(skin);
		TextButton easyBT = new TextButton("Leicht", skin);
		TextButton mediumBT = new TextButton("Mittel", skin);
		TextButton hardBT = new TextButton("Schwer", skin);
		schwierigkeitsLB = new Label("", skin);

		buttons.add(new Label("Wähle eine Schwierigkeit:", skin)).pad(15);
		buttons.row();
		buttons.add(easyBT).pad(15);
		buttons.row();
		buttons.add(mediumBT).pad(15);
		buttons.row();
		buttons.add(hardBT).pad(15);
		buttons.row();
		buttons.add(schwierigkeitsLB).pad(15).top().expand().uniform();
		mid.add(buttons).expand().fill().uniform();

		easyBT.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				ESPGame.game.setSchwierigkeit(0);
				updateSchwierigkeitLB();
				super.touchUp(event, x, y, pointer, button);
			}
		});
		mediumBT.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				ESPGame.game.setSchwierigkeit(1);
				updateSchwierigkeitLB();
				super.touchUp(event, x, y, pointer, button);
			}
		});
		hardBT.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				ESPGame.game.setSchwierigkeit(2);
				updateSchwierigkeitLB();
				super.touchUp(event, x, y, pointer, button);
			}
		});

		Table credits = setupCreditTable();
		mid.add(credits).expand().fill().pad(6).uniform();
		table.add(mid).expand().fill().uniform();

		table.row();
		table.add().padBottom(20);
		table.row();
		TextButton backBT = new TextButton("Zurück", skin);
		backBT.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				// TODO lifecycle ???
				ESPGame.game.setScreen(new MainMenu());
				return super.touchDown(event, x, y, pointer, button);
			}
		});
		table.add(backBT).padBottom(40).padTop(25);

		updateSchwierigkeitLB();
		resizeSliders();
		stage.setDebugAll(true);
	}

	private void updateSchwierigkeitLB() {
		switch (ESPGame.game.getSchwierigkeit()) {
		case 0:
			schwierigkeitsLB.setText(
					"Leicht:\n-Kombinierbare Eddys werden hervorgehoben\n-Mehr Eddys zu Spielbeginn\n-Mehr Eddys bei jeder neuen Runde\n-Verringerte Gravitation\n-Verringerte Punkte");
			break;
		case 1:
			schwierigkeitsLB.setText("Mittel\n-Standard Spielmodus");
			break;
		case 2:
			schwierigkeitsLB
					.setText("Schwer\n-Weniger He-Mans\n-Eddys im Orbit werden nicht abgehakt\n-Erhöhte Punkte");
			break;
		default:
			schwierigkeitsLB.setText("???");
			break;
		}
	}

	private Table setupCreditTable() {
		Table t = new Table(skin);

		Label header = new Label("Credits", skin);
		t.add(header).colspan(2).padBottom(20);
		t.row();

		t.add(new Label("Created by:", skin)).expandX();
		t.add(new Label("Music:", skin)).expandX();
		t.row();
		t.add(new Label("Nils Förster", skin)).expandX();
		t.add(new Label("Die Hübschen - Thunderplains", skin)).expandX();
		t.row();
		t.add(new Label("Patrick Herbers", skin)).expandX();
		t.row();
		t.add(new Label("", skin)).expandX();
		t.row();
		t.add(new Label("Starring:", skin)).expandX();
		t.add(new Label("Sounds by:", skin)).expandX();
		t.row();
		t.add(new Label("Etienne Gardé as Eddy:", skin)).expandX();
		t.add(new Label("soundjay.com", skin)).expandX();
		t.row();
		t.add(new Label("Nils Bomhoff as He-Man:", skin)).expandX();
		t.add(new Label("soundbible.com", skin)).expandX();
		t.row();
		t.add();
		t.add(new Label("He-Man Sound (c) Mattel", skin)).expandX();
		t.row();
		t.add(new Label("", skin)).expandX();
		t.row();
		t.add(new Label("Libraries used:", skin)).expandX();
		t.add(new Label("Airstrike Font by Iconain Fonts", skin)).expandX();
		t.row();
		t.add(new Label("libGDX", skin)).expandX();
		t.row();
		t.add(new Label("", skin)).expandX();
		t.row();
		t.add(new Label("Programs used:", skin)).expandX();
		t.row();
		t.add(new Label("Eclipse", skin)).expandX();
		t.row();
		t.add(new Label("Paint.net:", skin)).expandX();
		t.row();
		t.add(new Label("Audacity", skin)).expandX();

		return t;
	}

	public void resizeSliders() {
		float pad = Gdx.graphics.getWidth() / SLIDER_SIZE_FACTOR;
		musicTable.padLeft(pad).padRight(pad);
		soundTable.padLeft(pad).padRight(pad);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		resizeSliders();
	}

}
