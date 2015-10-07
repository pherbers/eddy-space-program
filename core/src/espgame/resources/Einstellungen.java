package espgame.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Einstellungen {
	
	public static final String DEFAULT_PLAYERNAME = "Tippe hier deinen Namen ein!";

	public static final String VOLUME_MUSIC = "volume_music";
	public static final String VOLUME_SOUND = "volume_sound";
	public static final String MUTE_SOUND = "mute_sound";
	public static final String MUTE_MUSIC = "mute_music";
	public static final String SCHWIERIGKEIT = "difficulty";
	public static final String PLAYERNAME = "playername";

	private Properties properties;

	private float musicVolume, soundVolume;
	private boolean musicMute, soundMute;
	private int schwierigkeit;
	private String playername;

	private Einstellungen() {
		properties = getDefaults();
		properties.list(System.out);
		readProperties();
	}

	public Einstellungen(File inputFile) throws IOException, NumberFormatException {
		properties = new Properties();
		FileInputStream fis = new FileInputStream(inputFile);
		properties.loadFromXML(fis);
		readProperties();
	}

	public static Einstellungen getDefaultEinstellungen() {
		System.out.println("requesting default einstellungen");
		return new Einstellungen();
	}

	public static Properties getDefaults() {
		Properties p = new Properties();
		p.setProperty(VOLUME_MUSIC, "0.5");
		p.setProperty(VOLUME_SOUND, "0.5");
		p.setProperty(MUTE_SOUND, "false");
		p.setProperty(MUTE_MUSIC, "false");
		p.setProperty(SCHWIERIGKEIT, "1");
		p.setProperty(PLAYERNAME, DEFAULT_PLAYERNAME);

		return p;
	}

	private void readProperties() {
		setMusicVolume(Float.parseFloat(properties.getProperty(VOLUME_MUSIC)));
		setSoundVolume(Float.parseFloat(properties.getProperty(VOLUME_SOUND)));

		setMusicMute(Boolean.parseBoolean(properties.getProperty(MUTE_MUSIC)));
		setSoundMute(Boolean.parseBoolean(properties.getProperty(MUTE_SOUND)));

		setSchwierigkeit(Integer.parseInt(properties.getProperty(SCHWIERIGKEIT)));
		setPlayername(properties.getProperty(PLAYERNAME));
	}

	private void storeProperties() {
		properties.setProperty(VOLUME_MUSIC, String.valueOf(getMusicVolume()));
		properties.setProperty(VOLUME_SOUND, String.valueOf(getSoundVolume()));

		properties.setProperty(MUTE_MUSIC, String.valueOf(isMusicMute()));
		properties.setProperty(MUTE_SOUND, String.valueOf(isSoundMute()));

		properties.setProperty(SCHWIERIGKEIT, String.valueOf(getSchwierigkeit()));
		properties.setProperty(PLAYERNAME, getPlayername());
	}

	public void save(File file) throws IOException {
		storeProperties();
		System.out.println(
				"Speichern der Einstellungen steht unmittelbar bevor! Einträge: " + properties.keySet().size());
		FileOutputStream fos = new FileOutputStream(file);
		properties.storeToXML(fos, "Eddy Space Program");
	}

	public int getSchwierigkeit() {
		return schwierigkeit;
	}

	public void setSchwierigkeit(int schwierigkeit) {
		this.schwierigkeit = schwierigkeit;
	}

	public String getPlayername() {
		return playername;
	}

	public void setPlayername(String playername) {
		this.playername = playername;
	}

	public float getMusicVolume() {
		return musicVolume;
	}

	public void setMusicVolume(float musicVolume) {
		this.musicVolume = musicVolume;
	}

	public float getSoundVolume() {
		return soundVolume;
	}

	public void setSoundVolume(float soundVolume) {
		this.soundVolume = soundVolume;
	}

	public boolean isMusicMute() {
		return musicMute;
	}

	public void setMusicMute(boolean musicMute) {
		this.musicMute = musicMute;
	}

	public boolean isSoundMute() {
		return soundMute;
	}

	public void setSoundMute(boolean soundMute) {
		this.soundMute = soundMute;
	}
}
