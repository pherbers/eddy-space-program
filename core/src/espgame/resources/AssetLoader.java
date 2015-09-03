package espgame.resources;

import java.util.ArrayList;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import static espgame.resources.AssetContainer.*;

public class AssetLoader {

	private static AssetLoader loader;
	private ArrayList<TempAsset> assetList;

	private AssetContainer<Texture> textureContainer;
	private AssetContainer<Sound> soundContainer;
	private AssetContainer<Music> musicContainer;
	private AssetContainer<BitmapFont> fontContainer;

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
	}

	public synchronized void load() {
		AssetManager manager = new AssetManager();
		manager.load("ui/uiskin.json", Skin.class);

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

	public Skin getSkin() {
		return skin;
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
