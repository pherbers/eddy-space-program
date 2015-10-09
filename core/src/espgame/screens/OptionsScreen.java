package espgame.screens;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import espgame.ESPGame;
import espgame.resources.AssetContainer;
import espgame.resources.AssetLoader;
import espgame.resources.Einstellungen;
import espgame.ui.uielements.ESPSlider;

public class OptionsScreen extends ESPMenu {

	public static final int NILS_CHANCE = 33;

	public static final float SLIDER_MIN = 0;
	public static final float SLIDER_MAX = 1;
	public static final float SLIDER_STEPSIZE = 0.00001f;
	public static final float SLIDER_BUTTON_MOD = SLIDER_STEPSIZE * 8504;
	public static final float SLIDER_PADDING = 5;

	public static final float TOGGLEPADDING = 30;

	public static final float SLIDER_SIZE_FACTOR = 4.1337f;

	private ESPSlider musicSlider, soundSlider;
	private Table musicTable, soundTable;
	private Label schwierigkeitsLB;
	private Button easyBT, mediumBT, hardBT;
	private Button toggleMusic, toggleSound;

	public OptionsScreen() {
		super(STAR_PERCENTAGE * 1.7f);
	}

	@Override
	public void init() {
		table.add().padBottom(20);
		table.row();

		musicTable = new Table(skin);
		toggleMusic = getToggleButton(AssetContainer.BUTTONMUSIK, AssetContainer.BUTTONMUSIK_A,
				AssetContainer.BUTTONMUSIK_D);
		toggleMusic.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				ESPGame.game.getEinstellungen().setMusicMute(toggleMusic.isChecked());
				ESPGame.game.updateMusicVolume();
			}
		});
		musicSlider = new ESPSlider(SLIDER_MIN, SLIDER_MAX, SLIDER_STEPSIZE, SLIDER_BUTTON_MOD, SLIDER_PADDING, skin) {
			@Override
			public void valueChanged() {
				float value = musicSlider.getValue();
				ESPGame.game.getEinstellungen().setMusicVolume(value);
				ESPGame.game.updateMusicVolume();
			}
		};
		musicTable.add(toggleMusic).right().padRight(TOGGLEPADDING);
		musicTable.add(musicSlider).expand().fill();
		table.add(musicTable).expand().fillX().bottom().padBottom(10);
		table.row();

		soundTable = new Table(skin);
		toggleSound = getToggleButton(AssetContainer.BUTTONTON, AssetContainer.BUTTONTON_A, AssetContainer.BUTTONTON_D);
		toggleSound.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				ESPGame.game.getEinstellungen().setSoundMute(toggleSound.isChecked());
			}
		});
		soundSlider = new ESPSlider(SLIDER_MIN, SLIDER_MAX, SLIDER_STEPSIZE, SLIDER_BUTTON_MOD, SLIDER_PADDING, skin) {
			@Override
			public void valueChanged() {
				ESPGame.game.getEinstellungen().setSoundVolume(soundSlider.getValue());
			}
		};
		soundTable.add(toggleSound).right().padRight(TOGGLEPADDING);
		soundTable.add(soundSlider).expand().fill();
		table.add(soundTable).expand().fillX().top().padTop(10);

		Music m = AssetLoader.get().getMusic(AssetContainer.MUSIC);
		musicSlider.setValue(m.getVolume());

		table.row();
		Table mid = new Table(skin);
		Table buttons = new Table(skin);
		easyBT = getImageButton(AssetContainer.DIFF_EASY_D, AssetContainer.DIFF_EASY);
		mediumBT = getImageButton(AssetContainer.DIFF_NORMAL_D, AssetContainer.DIFF_NORMAL);
		hardBT = getImageButton(AssetContainer.DIFF_HARD_D, AssetContainer.DIFF_HARD);
		schwierigkeitsLB = new Label("", skin);

		buttons.add(new Label("Wähle eine Schwierigkeit:", skin)).pad(15);
		buttons.row();
		buttons.add(easyBT).space(15);
		buttons.row();
		buttons.add(mediumBT).space(15);
		buttons.row();
		buttons.add(hardBT).space(15);
		buttons.row();
		buttons.add(schwierigkeitsLB).center().expand().top().space(30);
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
		Button backBT = getImageButton(AssetContainer.ZURUECK, AssetContainer.ZURUECK_A);
		backBT.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				// TODO lifecycle ???
				ESPGame.game.setScreen(new MainMenu());
				return super.touchDown(event, x, y, pointer, button);
			}
		});
		table.add(backBT).padBottom(40).padTop(25);

		Einstellungen e = ESPGame.game.getEinstellungen();
		musicSlider.setValue(e.getMusicVolume());
		soundSlider.setValue(e.getSoundVolume());
		toggleMusic.setChecked(e.isMusicMute());
		toggleSound.setChecked(e.isSoundMute());

		updateSchwierigkeitLB();
		resizeSliders();
		stage.setDebugAll(true);
	}

	private void updateSchwierigkeitLB() {
		easyBT.setChecked(false);
		mediumBT.setChecked(false);
		hardBT.setChecked(false);

		int diff = ESPGame.game.getSchwierigkeit();
		ESPGame.game.getEinstellungen().setSchwierigkeit(diff);
		switch (diff) {
		case 0:
			easyBT.setChecked(true);
			schwierigkeitsLB.setText(
					"Leicht:\n-Kombinierbare Eddys werden hervorgehoben\n-Mehr Eddys zu Spielbeginn\n-Mehr Eddys bei jeder neuen Runde\n-Verringerte Gravitation\n-Verringerte Punkte");
			break;
		case 1:
			mediumBT.setChecked(true);
			schwierigkeitsLB.setText("Mittel\n-Standard Spielmodus");
			break;
		case 2:
			hardBT.setChecked(true);
			schwierigkeitsLB.setText(
					"Schwer\n-Weniger He-Mans\n-Eddys im Orbit werden nicht abgehakt\n-Erhöhte Gravitation\n-Erhöhte Punkte");
			break;
		default:
			schwierigkeitsLB.setText("???");
			break;
		}
	}

	private Table setupCreditTable() {
		Table t = new Table(skin);

		String nils = "Nils Bomhoff";
		if (new Random().nextInt(100) < NILS_CHANCE) {
			nils = "Nils Bornhoff";
		}

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
		t.add(new Label(nils + " as He-Man:", skin)).expandX();
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
