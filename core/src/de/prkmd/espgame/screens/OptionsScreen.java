package de.prkmd.espgame.screens;

import java.io.IOException;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import de.prkmd.espgame.ESPGame;
import de.prkmd.espgame.resources.AssetContainer;
import de.prkmd.espgame.resources.AssetLoader;
import de.prkmd.espgame.resources.Einstellungen;
import de.prkmd.espgame.ui.uielements.ESPSlider;

public class OptionsScreen extends ESPMenu {

	public static final int NILS_CHANCE = 33;

	public static final float SLIDER_MIN = 0;
	public static final float SLIDER_MAX = 1;
	public static final float SLIDER_STEPSIZE = 0.00001f;
	public static final float SLIDER_BUTTON_MOD = SLIDER_STEPSIZE * 8504;
	public static final float SLIDER_PADDING = 5;

	public static final float TOGGLEPADDING = 30;
	public static final float SLIDER_SIZE_FACTOR = 4.1337f;
	public static final String RESET_WARN_TEXT = "Das Spiel wird beendet und alle angelegten Dateien\n(z.B. Highscores) werden gelöscht. Sicher?";

	private ESPSlider musicSlider, soundSlider;
	private Table musicTable, soundTable;
	private Label schwierigkeitsLB;
	private Button easyBT, mediumBT, hardBT;
	private Button toggleMusic, toggleSound;
	private Label resetWarnLB;

	private LevelOverlay overlay;

	public OptionsScreen() {
		super(STAR_PERCENTAGE * 1.7f);
	}

	public OptionsScreen(LevelOverlay previousScreen) {
		super(STAR_PERCENTAGE * 1.7f);
		this.overlay = previousScreen;
	}

	@Override
	public void init() {
		if(overlay != null) {
			drawBackground = false;
			backgroundGroup.addActor(new Actor(){
				Texture overlay = AssetLoader.get().getTexture(AssetContainer.OVERLAY_BLACK);
				@Override
				public void draw(Batch batch, float parentAlpha) {
					super.draw(batch, parentAlpha);
					batch.draw(overlay, 0, 0, stage.getWidth(), stage.getHeight());
				}
			});
		}
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

		Table additionalOptions = new Table(skin);

		final Button fullScreenBT = getImageButton(
				ESPGame.isFullScreen()?
						AssetContainer.UI_FULLSCREEN_ON:AssetContainer.UI_FULLSCREEN_OFF,
				ESPGame.isFullScreen()?
						AssetContainer.UI_FULLSCREEN_ON_A:AssetContainer.UI_FULLSCREEN_OFF_A);
		fullScreenBT.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				ESPGame.setFullScreen(!ESPGame.isFullScreen());

				String fullscreenID = ESPGame.isFullScreen()?AssetContainer.UI_FULLSCREEN_ON:AssetContainer.UI_FULLSCREEN_OFF;
				String fullscreenID_A = ESPGame.isFullScreen()?AssetContainer.UI_FULLSCREEN_ON_A:AssetContainer.UI_FULLSCREEN_OFF_A;
				TextureRegionDrawable off = new TextureRegionDrawable(new TextureRegion(AssetLoader.get().getTexture(fullscreenID)));
				TextureRegionDrawable on = new TextureRegionDrawable(new TextureRegion(AssetLoader.get().getTexture(fullscreenID_A)));
				fullScreenBT.getStyle().up = off;
				fullScreenBT.getStyle().down = on;
				fullScreenBT.getStyle().over = on;
			}
		});
		Button resetBT = getImageButton(AssetContainer.RESET, AssetContainer.RESET_A);
		resetBT.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				hardReset();
			}

			@Override
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				super.enter(event, x, y, pointer, fromActor);
				resetWarnLB.setText(RESET_WARN_TEXT);
			}

			@Override
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				super.exit(event, x, y, pointer, toActor);
				resetWarnLB.setText("");
			}
		});
		resetWarnLB = new Label("", skin);

		table.row();
		additionalOptions.add(fullScreenBT).expandX().uniform();
		additionalOptions.add(resetWarnLB).expandX().uniform();
		additionalOptions.add(resetBT).expandX().uniform();
		table.add(additionalOptions).pad(10).expandX().fill();

		table.row();
		Table mid = new Table(skin);
		Table buttons = new Table(skin);
		easyBT = getToggleButton(AssetContainer.DIFF_EASY_D, AssetContainer.DIFF_EASY, AssetContainer.DIFF_EASY);
		mediumBT = getToggleButton(AssetContainer.DIFF_NORMAL_D, AssetContainer.DIFF_NORMAL,
				AssetContainer.DIFF_NORMAL);
		hardBT = getToggleButton(AssetContainer.DIFF_HARD_D, AssetContainer.DIFF_HARD, AssetContainer.DIFF_HARD);
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
				if(overlay != null) {
					overlay.setMenu(new PauseScreen(overlay));
					return true;
				} else {
					ESPGame.game.changeScreen(new MainMenu());
					return true;
				}
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
		// stage.setDebugAll(true);
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

	private void hardReset() {
		System.out.println("File reset angefordert!");
		boolean success;
		try {
			success = ESPGame.game.getFileManager().delete();
		} catch (IOException e) {
			e.printStackTrace();
			success = false;
		}
		System.out.println("File reset durchgeführt! Erfolg: " + success);

		if (!success) {
			// TODO what do?
		}

		Gdx.app.exit();
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
//		if(overlay != null)
//			overlay.resize(width, height);
	}

}
