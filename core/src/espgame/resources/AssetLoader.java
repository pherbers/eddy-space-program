package espgame.resources;

import java.util.ArrayList;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import static espgame.resources.AssetContainer.*;

public class AssetLoader {

	private static AssetLoader loader;
	private ArrayList<TempAsset> assetList;

	private AssetContainer<Texture> textureContainer;
	private AssetContainer<Sound> soundContainer;
	private AssetContainer<Music> musicContainer;

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
		assetList.add(new TempAsset("sprites/eddys/EddyHighlight.png", textureContainer, EDDY_HIGHLIGHT, Texture.class));
		assetList.add(new TempAsset("sprites/eddys/HeMan.png", textureContainer, HEMAN, Texture.class));
		assetList.add(new TempAsset("sprites/eddys/pointyeddy.png", textureContainer, MENU_EDDY, Texture.class));
		assetList.add(new TempAsset("sprites/eddys/pointynils.png", textureContainer, MENU_NILS, Texture.class));

		assetList.add(new TempAsset("sprites/planeten/earth.png", textureContainer, PLANET_MAIN, Texture.class));
		assetList.add(new TempAsset("sprites/planeten/moon.png", textureContainer, PLANET_VARIANT, Texture.class));
		assetList.add(new TempAsset("sprites/planeten/majora.png", textureContainer, PLANET_SECRET_1, Texture.class));
		assetList.add(new TempAsset("sprites/planeten/death_star.png", textureContainer, PLANET_SECRET_2, Texture.class));
		assetList.add(new TempAsset("sprites/planeten/cheese.png", textureContainer, PLANET_SECRET_3, Texture.class));
	}

	public synchronized void load() {
		AssetManager manager = new AssetManager();

		for (int i = 0; i < assetList.size(); i++) {
			assetList.get(i).load(manager);
		}

		manager.finishLoading();

		for (int i = 0; i < assetList.size(); i++) {
			assetList.get(i).store(manager);
		}
		System.out.println("Loaded all assets. Clears now.");
		assetList.clear();
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
	public Sound getSound(String key) { return getSoundContainer().get(key); }
	public Music getMusic(String key) {	return getMusicContainer().get(key); }

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
