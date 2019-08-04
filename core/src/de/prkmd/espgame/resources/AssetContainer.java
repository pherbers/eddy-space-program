package de.prkmd.espgame.resources;

import java.util.HashMap;
import java.util.Iterator;

public abstract class AssetContainer<T> implements Iterable<T> {
	public static final String SHIP_IDLE = "ship_idle";
	public static final String SHIP_ACTIVE = "sip_active";
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
	public static final String UI_LOGO_SM = "ui_logo_sm";
	public static final String UI_LOGO_SM_ALT = "ui_smalt";
	public static final String UI_LOGO_TINY = "ui_logo_tiny";
	public static final String UI_SELECTION = "ui_selection";
	
	public static final String UI_OBJECTIVE_TOP = "ui_objectivetop";
	public static final String UI_OBJECTIVE_BOT = "ui_objectivebot";
	public static final String UI_OBJECTIVE_LEFT = "ui_objectiveleft";
	public static final String UI_OBJECTIVE_BACKGROUND = "ui_objectivebg";

	public static final String UI_FULLSCREEN_OFF = "UI_FULLSCREEN_OFF";
	public static final String UI_FULLSCREEN_OFF_A = "UI_FULLSCREEN_OFF_A";
	public static final String UI_FULLSCREEN_ON = "UI_FULLSCREEN_ON";
	public static final String UI_FULLSCREEN_ON_A = "UI_FULLSCREEN_ON_A";


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

	public static final String ANLEITUNG = "Anleitung";
	public static final String ANLEITUNG_A = "Anleitung_a";
	public static final String BEENDEN = "Beenden";
	public static final String BEENDEN_A = "Beenden_a";
	public static final String BUTTONMINUS = "ButtonMinus";
	public static final String BUTTONMINUS_A = "ButtonMinus_a";
	public static final String BUTTONMUSIK = "ButtonMusik";
	public static final String BUTTONMUSIK_A = "ButtonMusik_a";
	public static final String BUTTONMUSIK_D = "ButtonMusik_d";
	public static final String BUTTONPLUS = "ButtonPlus";
	public static final String BUTTONPLUS_A = "ButtonPlus_a";
	public static final String BUTTONTON = "ButtonTon";
	public static final String BUTTONTON_A = "ButtonTon_a";
	public static final String BUTTONTON_D = "ButtonTon_d";
	public static final String DIFF_EASY = "diff_easy";
	public static final String DIFF_EASY_D = "diff_easy_d";
	public static final String DIFF_HARD = "diff_hard";
	public static final String DIFF_HARD_D = "diff_hard_d";
	public static final String DIFF_NORMAL = "diff_normal";
	public static final String DIFF_NORMAL_D = "diff_normal_d";
	public static final String HIGHSCORES = "Highscores";
	public static final String HIGHSCORES_A = "Highscores_a";
	public static final String LEFT = "left";
	public static final String LEFT_A = "left_a";
	public static final String MENU = "Menu";
	public static final String MENU_A = "Menu_a";
	public static final String NEUESSPIEL = "NeuesSpiel";
	public static final String NEUESSPIEL_A = "NeuesSpiel_a";
	public static final String OPTIONEN = "Optionen";
	public static final String OPTIONEN_A = "Optionen_a";
	public static final String RESET = "Reset";
	public static final String RESET_A = "Reset_a";
	public static final String RIGHT = "right";
	public static final String RIGHT_A = "right_a";
	public static final String WEITER = "Weiter";
	public static final String WEITER_A = "Weiter_a";
	public static final String ZURUECK = "Zurueck";
	public static final String ZURUECK_A = "Zurueck_a";

	public static final String OVERLAY_BLACK = "Overlay_black";
	public static final String OVERLAY_RED = "Overlay_black";
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
