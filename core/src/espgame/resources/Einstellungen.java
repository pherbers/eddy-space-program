package espgame.resources;

import java.io.Serializable;

public class Einstellungen implements Serializable {

	private static final long serialVersionUID = -213319216710987639L;
	private double LSmusic, LSsound;
	private boolean MUmusic, MUsound;
	private int schwierigkeit;
	private String playername;

	public Einstellungen() {
		setToDefault();
	}

	public Einstellungen(double LSmusik, double LSsound, boolean MUmusic, boolean MUsound, int schwierigkeit,
			String playername) {
		this.LSmusic = LSmusik;
		this.LSsound = LSsound;
		this.MUmusic = MUmusic;
		this.MUsound = MUsound;
		this.schwierigkeit = schwierigkeit;
		this.playername = playername;
	}

	public static Einstellungen getCurrentEinstellungen() {
		// SoundStore s = SoundStore.get();
		// return new Einstellungen(s.getMusicVolume(), s.getSoundVolume(),
		// s.musicOn(), s.soundsOn(),
		// Game.game.getSchwierigkeit(), Game.getPlayerName());
		return null;
	}

	public static Einstellungen getDefaultEinstellungen() {
		return new Einstellungen();
	}

	public void setToDefault() {
		LSmusic = 0.5;
		LSsound = 0.5;
		MUmusic = true;
		MUsound = true;
		schwierigkeit = 1;
		playername = "Tippe hier deinen Namen ein!";
	}

	public double getLSmusic() {
		return LSmusic;
	}

	public void setLSmusic(double lSmusic) {
		LSmusic = lSmusic;
	}

	public double getLSsound() {
		return LSsound;
	}

	public void setLSsound(double lSsound) {
		LSsound = lSsound;
	}

	public boolean isMUmusic() {
		return MUmusic;
	}

	public void setMUmusic(boolean mUmusic) {
		MUmusic = mUmusic;
	}

	public boolean isMUsound() {
		return MUsound;
	}

	public void setMUsound(boolean mUsound) {
		MUsound = mUsound;
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
}
