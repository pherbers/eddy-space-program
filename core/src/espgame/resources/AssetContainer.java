package espgame.resources;

import java.util.HashMap;
import java.util.Iterator;

public abstract class AssetContainer<T> implements Iterable<T> {
	public static final String SHIP_IDLE = "ship_idle";
	public static final String SHIP_SAMMELN_1 = "ship_collect_1";
	public static final String SHIP_SAMMELN_2 = "ship_collect_2";
	public static final String KANONE_BASE = "cacon_base";
	public static final String KANONE_TOP = "canon_top";
	public static final String ORBIT = "orbit";

	public static final String EDDY_BLAU = "eddy_blue";
	public static final String EDDY_CYAN = "eddy_cyan";
	public static final String EDDY_GELB = "eddy_yellow";
	public static final String EDDY_GRUEN = "eddy_green";
	public static final String EDDY_HIGHLIGHT = "eddy_highlight";
	public static final String EDDY_MAGENTA = "eddy_magenta";
	public static final String EDDY_ROT = "eddy_red";
	public static final String EDDY_WEISS = "eddy_white";
	public static final String HEMAN = "eddy_heman";
	public static final String MENU_EDDY = "menu_eddy";
	public static final String MENU_NILS = "menu_heman";

	public static final String PLANET_MAIN = "planet_earth";
	public static final String PLANET_VARIANT = "planet_moon";
	public static final String PLANET_SECRET_1 = "planet_secret_1";
	public static final String PLANET_SECRET_2 = "planet_secret_2";
	public static final String PLANET_SECRET_3 = "planet_secret_3";

	public static final String UI_ANLEITUNG = "ui_anleitung";
	public static final String UI_COLORS = "ui_color";
	public static final String UI_CREDITS = "ui_credits";
	public static final String UI_EDDY_SELECTOR = "ui_seclector";
	public static final String UI_ESP_TITLE = "ui_title";
	public static final String UI_GAMEOVER = "ui_gameover";
	public static final String UI_TICK = "ui_tick";
	public static final String UI_LOGO = "ui_logo";
	public static final String UI_LOGO_ALT = "ui_logo_alt";
	public static final String UI_LOGO_TINY = "ui_logo_tiny";
	public static final String UI_SELECTION = "ui_selection";

	public static final String SOUND_POP = "sound_pop";
	public static final String SOUND_HEMAN_ENTER = "sound_hemanenter";
	public static final String SOUND_EXPLOSION = "sound_explosion";
	public static final String SOUND_HEMAN_GET = "sound_hemanratschlag";
	public static final String SOUND_TWINKLE = "sound_twinkle";
	public static final String SOUND_BUTTON_PRESS = "sound_button";
	public static final String SOUND_KANON_EMPTY = "sound_canon_empty";

	public static final String MUSIC = "main_music";

	public static final String FONT_SMALL = "airstrike_s";
	public static final String FONT_MEDIUM = "airstrike_m";
	public static final String FONT_BIG = "airstrike_b";

	private HashMap<String, T> textureMap;

	public AssetContainer() {
		textureMap = new HashMap<String, T>();
	}

	public T get(String texture) {
		T t = textureMap.get(texture);
		if (t == null) {
			t = handleMissing();
		}

		return t;
	}

	public void add(String key, T t) {
		textureMap.put(key, t);
	}

	@SuppressWarnings("unchecked")
	public void tryToAdd(String key, Object o) {
		try {
			add(key, (T) o);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Iterator<T> iterator() {
		return textureMap.values().iterator();
	}

	protected abstract T handleMissing();
}
