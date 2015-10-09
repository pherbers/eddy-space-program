package espgame.resources;

import static espgame.resources.AssetContainer.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import espgame.ESPGame;

public class AssetLoader {

	private static AssetLoader loader;
	private ArrayList<TempAsset> assetList;

	private AssetContainer<Texture> textureContainer;
	private AssetContainer<Sound> soundContainer;
	private AssetContainer<Music> musicContainer;
	private AssetContainer<BitmapFont> fontContainer;

	private AssetManager manager;

	private Skin skin;

	public static AssetLoader get() {
		if (loader == null)
			loader = new AssetLoader();
		return loader;
	}

	private AssetLoader() {
		textureContainer = new AssetContainer<Texture>() {
			@Override
			protected Texture handleMissing() {
				System.err.println("Wanting to access a texture. But it is missing.");
				return null;
			}
		};
		soundContainer = new AssetContainer<Sound>() {
			@Override
			protected Sound handleMissing() {
				System.err.println("Wanting to access a sound. But it is missing.");
				return null;
			}
		};
		musicContainer = new AssetContainer<Music>() {
			@Override
			protected Music handleMissing() {
				System.err.println("Wanting to access a music. But it is missing.");
				return null;
			}
		};
		fontContainer = new AssetContainer<BitmapFont>() {

			@Override
			protected BitmapFont handleMissing() {
				System.err.println("Wanting to access a font. But it is missing.");
				return null;
			}
		};
		assetList = new ArrayList<TempAsset>();
	}

	public void collect() {
		assetList.add(new TempAsset("sprites/misc/schiff_idle.png", textureContainer, SHIP_IDLE, Texture.class));
		assetList.add(new TempAsset("sprites/misc/schiff_active.png", textureContainer, SHIP_ACTIVE, Texture.class));
		assetList.add(new TempAsset("sprites/misc/Kanone_base.png", textureContainer, KANONE_BASE, Texture.class));
		assetList.add(new TempAsset("sprites/misc/Kanone_top.png", textureContainer, KANONE_TOP, Texture.class));
		assetList.add(new TempAsset("sprites/misc/Orbit.png", textureContainer, ORBIT, Texture.class));

		assetList.add(new TempAsset("sprites/eddys/EddyBlau.png", textureContainer, EDDY_BLAU, Texture.class));
		assetList.add(new TempAsset("sprites/eddys/EddyCyan.png", textureContainer, EDDY_CYAN, Texture.class));
		assetList.add(new TempAsset("sprites/eddys/EddyGelb.png", textureContainer, EDDY_GELB, Texture.class));
		assetList.add(new TempAsset("sprites/eddys/EddyGruen.png", textureContainer, EDDY_GRUEN, Texture.class));
		assetList.add(new TempAsset("sprites/eddys/EddyMagenta.png", textureContainer, EDDY_MAGENTA, Texture.class));
		assetList.add(new TempAsset("sprites/eddys/EddyRot.png", textureContainer, EDDY_ROT, Texture.class));
		assetList.add(new TempAsset("sprites/eddys/Eddyweiss.png", textureContainer, EDDY_WEISS, Texture.class));
		assetList
				.add(new TempAsset("sprites/eddys/EddyHighlight.png", textureContainer, EDDY_HIGHLIGHT, Texture.class));
		assetList.add(new TempAsset("sprites/eddys/HeMan.png", textureContainer, HEMAN, Texture.class));
		assetList.add(new TempAsset("sprites/eddys/pointyeddy.png", textureContainer, MENU_EDDY, Texture.class));
		assetList.add(new TempAsset("sprites/eddys/pointynils.png", textureContainer, MENU_NILS, Texture.class));

		assetList.add(
				new TempAsset("sprites/gui/EddySelectorBG.png", textureContainer, UI_EDDY_SELECTOR, Texture.class));
		assetList.add(new TempAsset("sprites/gui/selection.png", textureContainer, UI_SELECTION, Texture.class));
		assetList.add(new TempAsset("sprites/gui/haken.png", textureContainer, UI_TICK, Texture.class));
		assetList.add(new TempAsset("sprites/gui/selectionBot.png", textureContainer, UI_OBJECTIVE_BOT, Texture.class));
		assetList.add(
				new TempAsset("sprites/gui/selectionLeft.png", textureContainer, UI_OBJECTIVE_LEFT, Texture.class));
		assetList.add(new TempAsset("sprites/gui/selectionTop.png", textureContainer, UI_OBJECTIVE_TOP, Texture.class));
		assetList.add(
				new TempAsset("sprites/gui/selectionBg.png", textureContainer, UI_OBJECTIVE_BACKGROUND, Texture.class));

		assetList.add(new TempAsset("sprites/gui/anleitung.png", textureContainer, UI_ANLEITUNG, Texture.class));
		assetList.add(new TempAsset("sprites/gui/colors.png", textureContainer, UI_COLORS, Texture.class));
		assetList.add(new TempAsset("sprites/gui/credits.png", textureContainer, UI_CREDITS, Texture.class));
		assetList.add(new TempAsset("sprites/gui/anleitung.png", textureContainer, UI_ANLEITUNG, Texture.class));
		assetList.add(new TempAsset("sprites/gui/esp_title.png", textureContainer, UI_ESP_TITLE, Texture.class));
		assetList.add(new TempAsset("sprites/gui/gameover.png", textureContainer, UI_GAMEOVER, Texture.class));
		assetList.add(new TempAsset("sprites/gui/logo.png", textureContainer, UI_LOGO, Texture.class));
		assetList.add(new TempAsset("sprites/gui/logoSMALL.png", textureContainer, UI_LOGO_SM, Texture.class));
		assetList.add(new TempAsset("sprites/gui/logoSMALLalt.png", textureContainer, UI_LOGO_SM_ALT, Texture.class));
		assetList.add(new TempAsset("sprites/gui/logoTiny.png", textureContainer, UI_LOGO_TINY, Texture.class));

		assetList.add(new TempAsset("sprites/planeten/earth.png", textureContainer, PLANET_MAIN, Texture.class));
		assetList.add(new TempAsset("sprites/planeten/moon.png", textureContainer, PLANET_VARIANT, Texture.class));
		assetList.add(new TempAsset("sprites/planeten/majora.png", textureContainer, PLANET_SECRET_1, Texture.class));
		assetList.add(
				new TempAsset("sprites/planeten/death_star.png", textureContainer, PLANET_SECRET_2, Texture.class));
		assetList.add(new TempAsset("sprites/planeten/cheese.png", textureContainer, PLANET_SECRET_3, Texture.class));

		assetList.add(new TempAsset("sound/pop.wav", soundContainer, SOUND_POP, Sound.class));
		assetList.add(new TempAsset("sound/empty.wav", soundContainer, SOUND_KANON_EMPTY, Sound.class));
		assetList.add(new TempAsset("sound/HeManEnter.ogg", soundContainer, SOUND_HEMAN_ENTER, Sound.class));
		assetList.add(new TempAsset("sound/ratschlag.ogg", soundContainer, SOUND_HEMAN_GET, Sound.class));
		assetList.add(new TempAsset("sound/button.wav", soundContainer, SOUND_BUTTON_PRESS, Sound.class));
		assetList.add(new TempAsset("sound/twinkle.wav", soundContainer, SOUND_TWINKLE, Sound.class));
		assetList.add(new TempAsset("sound/explosionB.wav", soundContainer, SOUND_EXPLOSION, Sound.class));

		assetList.add(new TempAsset("music/music.ogg", musicContainer, MUSIC, Music.class));

		assetList.add(new TempAsset("font/airstrike_small.fnt", fontContainer, FONT_SMALL, BitmapFont.class));
		assetList.add(new TempAsset("font/airstrike_med.fnt", fontContainer, FONT_MEDIUM, BitmapFont.class));
		assetList.add(new TempAsset("font/airstrike_big.fnt", fontContainer, FONT_BIG, BitmapFont.class));

		assetList.add(new TempAsset("sprites/gui/buttons/Anleitung.png", textureContainer, ANLEITUNG, Texture.class));
		assetList.add(
				new TempAsset("sprites/gui/buttons/Anleitung_a.png", textureContainer, ANLEITUNG_A, Texture.class));
		assetList.add(new TempAsset("sprites/gui/buttons/Beenden.png", textureContainer, BEENDEN, Texture.class));
		assetList.add(new TempAsset("sprites/gui/buttons/Beenden_a.png", textureContainer, BEENDEN_A, Texture.class));
		assetList.add(
				new TempAsset("sprites/gui/buttons/ButtonMinus.png", textureContainer, BUTTONMINUS, Texture.class));
		assetList.add(
				new TempAsset("sprites/gui/buttons/ButtonMinus_a.png", textureContainer, BUTTONMINUS_A, Texture.class));
		assetList.add(
				new TempAsset("sprites/gui/buttons/ButtonMusik.png", textureContainer, BUTTONMUSIK, Texture.class));
		assetList.add(
				new TempAsset("sprites/gui/buttons/ButtonMusik_a.png", textureContainer, BUTTONMUSIK_A, Texture.class));
		assetList.add(
				new TempAsset("sprites/gui/buttons/ButtonMusik_d.png", textureContainer, BUTTONMUSIK_D, Texture.class));
		assetList.add(new TempAsset("sprites/gui/buttons/ButtonPlus.png", textureContainer, BUTTONPLUS, Texture.class));
		assetList.add(
				new TempAsset("sprites/gui/buttons/ButtonPlus_a.png", textureContainer, BUTTONPLUS_A, Texture.class));
		assetList.add(new TempAsset("sprites/gui/buttons/ButtonTon.png", textureContainer, BUTTONTON, Texture.class));
		assetList.add(
				new TempAsset("sprites/gui/buttons/ButtonTon_a.png", textureContainer, BUTTONTON_A, Texture.class));
		assetList.add(
				new TempAsset("sprites/gui/buttons/ButtonTon_d.png", textureContainer, BUTTONTON_D, Texture.class));
		assetList.add(new TempAsset("sprites/gui/buttons/diff_easy.png", textureContainer, DIFF_EASY, Texture.class));
		assetList.add(
				new TempAsset("sprites/gui/buttons/diff_easy_d.png", textureContainer, DIFF_EASY_D, Texture.class));
		assetList.add(new TempAsset("sprites/gui/buttons/diff_hard.png", textureContainer, DIFF_HARD, Texture.class));
		assetList.add(
				new TempAsset("sprites/gui/buttons/diff_hard_d.png", textureContainer, DIFF_HARD_D, Texture.class));
		assetList.add(
				new TempAsset("sprites/gui/buttons/diff_normal.png", textureContainer, DIFF_NORMAL, Texture.class));
		assetList.add(
				new TempAsset("sprites/gui/buttons/diff_normal_d.png", textureContainer, DIFF_NORMAL_D, Texture.class));
		assetList.add(new TempAsset("sprites/gui/buttons/Highscores.png", textureContainer, HIGHSCORES, Texture.class));
		assetList.add(
				new TempAsset("sprites/gui/buttons/Highscores_a.png", textureContainer, HIGHSCORES_A, Texture.class));
		assetList.add(new TempAsset("sprites/gui/buttons/left.png", textureContainer, LEFT, Texture.class));
		assetList.add(new TempAsset("sprites/gui/buttons/left_a.png", textureContainer, LEFT_A, Texture.class));
		assetList.add(new TempAsset("sprites/gui/buttons/Menu.png", textureContainer, MENU, Texture.class));
		assetList.add(new TempAsset("sprites/gui/buttons/Menu_a.png", textureContainer, MENU_A, Texture.class));
		assetList.add(new TempAsset("sprites/gui/buttons/NeuesSpiel.png", textureContainer, NEUESSPIEL, Texture.class));
		assetList.add(
				new TempAsset("sprites/gui/buttons/NeuesSpiel_a.png", textureContainer, NEUESSPIEL_A, Texture.class));
		assetList.add(new TempAsset("sprites/gui/buttons/Optionen.png", textureContainer, OPTIONEN, Texture.class));
		assetList.add(new TempAsset("sprites/gui/buttons/Optionen_a.png", textureContainer, OPTIONEN_A, Texture.class));
		assetList.add(new TempAsset("sprites/gui/buttons/Reset.png", textureContainer, RESET, Texture.class));
		assetList.add(new TempAsset("sprites/gui/buttons/Reset_a.png", textureContainer, RESET_A, Texture.class));
		assetList.add(new TempAsset("sprites/gui/buttons/right.png", textureContainer, RIGHT, Texture.class));
		assetList.add(new TempAsset("sprites/gui/buttons/right_a.png", textureContainer, RIGHT_A, Texture.class));
		assetList.add(new TempAsset("sprites/gui/buttons/Weiter.png", textureContainer, WEITER, Texture.class));
		assetList.add(new TempAsset("sprites/gui/buttons/Weiter_a.png", textureContainer, WEITER_A, Texture.class));
		assetList.add(new TempAsset("sprites/gui/buttons/Zurueck.png", textureContainer, ZURUECK, Texture.class));
		assetList.add(new TempAsset("sprites/gui/buttons/Zurueck_a.png", textureContainer, ZURUECK_A, Texture.class));

		assetList.add(new TempAsset("sprites/gui/overlay_black.png", textureContainer, OVERLAY_BLACK, Texture.class));
	}

	@Deprecated
	public synchronized void load() {
		manager = new AssetManager();

		for (int i = 0; i < assetList.size(); i++) {
			assetList.get(i).load(manager);
		}

		manager.finishLoading();

		for (int i = 0; i < assetList.size(); i++) {
			assetList.get(i).store(manager);
		}
		skin = manager.get("ui/uiskin.json");
		System.out.println("Loaded all assets. Clears now.");
		assetList.clear();
	}

	public void prepareLoading() {
		manager = new AssetManager();

		for (int i = 0; i < assetList.size(); i++) {
			assetList.get(i).load(manager);
		}
	}

	public void storeAssets() {
		for (int i = 0; i < assetList.size(); i++) {
			assetList.get(i).store(manager);
		}
		System.out.println("Loaded all assets. Clears now.");
		assetList.clear();
	}

	public boolean isFinished() {
		return manager.update();
	}

	public float getProgress() {
		return manager.getProgress();
	}

	public Skin getSkin() {
		if(skin==null){
			AssetManager m = new AssetManager();

			m.load("ui/uiskin.json", Skin.class);
			m.finishLoading();
			skin = m.get("ui/uiskin.json");
		}
		
		return skin;
	}
	
	public Texture getLoadingIcon(){
		AssetManager m = new AssetManager();

		m.load("sprites/gui/logoTiny.png", Texture.class);
		m.finishLoading();
		Texture t = m.get("sprites/gui/logoTiny.png");
		return t;
	}

	public AssetContainer<Texture> getTextureContainer() {
		return textureContainer;
	}

	public AssetContainer<Sound> getSoundContainer() {
		return soundContainer;
	}

	public AssetContainer<Music> getMusicContainer() {
		return musicContainer;
	}

	public Texture getTexture(String key) {
		return getTextureContainer().get(key);
	}

	public Sound getSound(String key) {
		return getSoundContainer().get(key);
	}

	public Music getMusic(String key) {
		return getMusicContainer().get(key);
	}

	public BitmapFont getFont(String key) {
		return fontContainer.get(key);
	}

	public class TempAsset {
		private String path;
		private AssetContainer<?> container;
		private String key;
		private Class<?> type;

		public TempAsset(String path, AssetContainer<?> container, String key, Class<?> type) {
			System.out.println("path: " + path + " key: " + key);
			this.path = path;
			this.container = container;
			this.key = key;
			this.type = type;
		}

		public void load(AssetManager manager) {
			manager.load(path, type);
		}

		public void store(AssetManager manager) {
			Object o = manager.get(path, type);
			container.tryToAdd(key, o);
		}
	}
}
