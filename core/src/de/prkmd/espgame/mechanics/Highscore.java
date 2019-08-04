package de.prkmd.espgame.mechanics;

import java.io.Serializable;
import java.util.Date;

import de.prkmd.espgame.ESPGame;

public class Highscore implements Serializable, Comparable<Highscore> {
	private static final long serialVersionUID = 6257523437558730335L;
	private int score, level, schwierigkeit;
	private long timestamp;

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	private String name;

	public Highscore(int level, int score, int schwierigkeit) {
		this(level, score, ESPGame.game.getEinstellungen().getPlayername(), schwierigkeit, new Date().getTime());
	}

	public Highscore(int level, int score, String name, int schwierigkeit, long timestamp) {
		this.level = level;
		this.score = score;
		this.name = name;
		this.setSchwierigkeit(schwierigkeit);
		this.timestamp = timestamp;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int compareTo(Highscore o) {
		if (score == o.getScore())
			return o.getLevel() - getLevel();
		return o.getScore() - getScore();
	}

	@Override
	public String toString() {
		return name + " LV: " + level + " Score: " + score;
	}

	public int getSchwierigkeit() {
		return schwierigkeit;
	}

	public void setSchwierigkeit(int schwierigkeit) {
		this.schwierigkeit = schwierigkeit;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public Date getTime() {
		return new Date(getTimestamp());
	}
}
