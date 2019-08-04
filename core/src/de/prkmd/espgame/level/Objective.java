package de.prkmd.espgame.level;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import de.prkmd.espgame.entity.Eddy.Color;

public class Objective implements Iterable<Color> {

	ArrayList<Color> farben;
	int[] anzahl;

	public Objective() {
		farben = new ArrayList<Color>();
	}

	public void newObjective(int level, int[] anzahl) {
		farben.clear();
		int cap = getCap(level);
		Random r = new Random();
		double tier1chance, tier2chance;
		if (level == 1) {
			tier1chance = 1.0;
			tier2chance = 0.0;
			// tier3chance = 0.0;
		} else if (level == 2) {
			tier1chance = 1.0;
			tier2chance = 0.0;
			// tier3chance = 0.0;
		} else if (level >= 3 && level < 5) {
			tier1chance = 0.6;
			tier2chance = 0.4;
			// tier3chance = 0.0;
		} else if (level >= 5 && level < 10) {
			tier1chance = 0.3;
			tier2chance = 0.5;
			// tier3chance = 0.2;
		} else {
			tier1chance = 0.1;
			tier2chance = 0.5;
			// tier3chance = 0.4;
		}

		int[] anzahl2 = new int[7]; // Für mögliches Bilden von eddys
		double[] chances = new double[7];

		int i = 0;
		while (i < cap) {
			double chancegesamt = 0;

			anzahl2[3] = Math.min(anzahl[0], anzahl[1]);
			anzahl2[4] = Math.min(anzahl[1], anzahl[2]);
			anzahl2[5] = Math.min(anzahl[0], anzahl[2]);
			anzahl2[6] = Math.min(Math.min(anzahl[0], anzahl[2]), anzahl[1]);

			chances[0] = tier1chance / 3 * anzahl[0];
			chances[1] = tier1chance / 3 * anzahl[1];
			chances[2] = tier1chance / 3 * anzahl[2];
			chances[3] = tier2chance / 3 * (anzahl[3] + anzahl2[3]);
			chances[4] = tier2chance / 3 * (anzahl[4] + anzahl2[4]);
			chances[5] = tier2chance / 3 * (anzahl[5] + anzahl2[5]);
			chances[6] = (1 - tier1chance - tier2chance) * (anzahl[6] + anzahl2[6]);
			for (double j : chances) {
				chancegesamt += j;
			}
			for (int j = 0; j < chances.length; j++) {
				chances[j] /= chancegesamt;
				if (j > 0)
					chances[j] += chances[j - 1];
			}
			double c = r.nextDouble();
			Color farbe;
			if (c <= chances[0]) {
				farbe = Color.ROT;
				anzahl[0]--;
				i++;
			} else if (c <= chances[1]) {
				farbe = Color.BLAU;
				anzahl[1]--;
				i++;
			} else if (c <= chances[2]) {
				farbe = Color.GRUEN;
				anzahl[2]--;
				i++;
			} else if (c <= chances[3]) {
				farbe = Color.MAGENTA;
				if (anzahl[3] > 0)
					anzahl[3]--;
				else {
					anzahl[0]--;
					anzahl[1]--;
				}
				i += 2;
			} else if (c <= chances[4]) {
				farbe = Color.CYAN;
				if (anzahl[4] > 0)
					anzahl[4]--;
				else {
					anzahl[2]--;
					anzahl[1]--;
				}
				i += 2;
			} else if (c <= chances[5]) {
				farbe = Color.GELB;
				if (anzahl[5] > 0)
					anzahl[5]--;
				else {
					anzahl[0]--;
					anzahl[2]--;
				}
				i += 2;
			} else if (c <= chances[6]) {
				farbe = Color.WEISS;
				if (anzahl[6] > 0)
					anzahl[6]--;
				else {
					anzahl[0]--;
					anzahl[1]--;
					anzahl[2]--;
				}
				i += 3;
			} else {
				switch (r.nextInt(3)) {
				case 1:
					farbe = Color.BLAU;
					break;
				case 2:
					farbe = Color.GRUEN;
					break;
				default:
					farbe = Color.ROT;
				}
				i++;
			}
			farben.add(farbe);

		}
		this.anzahl = new int[7];
		for (Color c : this.farben)
			switch (c) {
			case ROT:
				this.anzahl[0]++;
				break;
			case BLAU:
				this.anzahl[1]++;
				break;
			case GRUEN:
				this.anzahl[2]++;
				break;
			case MAGENTA:
				this.anzahl[3]++;
				break;
			case CYAN:
				this.anzahl[4]++;
				break;
			case GELB:
				this.anzahl[5]++;
				break;
			case WEISS:
				this.anzahl[6]++;
				break;
			default:
				break;
			}
	}

	public boolean checkObjective(int[] reserve) {
		if (reserve[0] < anzahl[0])
			return false;
		if (reserve[1] < anzahl[1])
			return false;
		if (reserve[2] < anzahl[2])
			return false;
		if (reserve[3] + Math.min(reserve[0] - anzahl[0], reserve[1] - anzahl[1]) < anzahl[3])
			return false;
		if (reserve[4] + Math.min(reserve[1] - anzahl[1], reserve[2] - anzahl[2]) < anzahl[4])
			return false;
		if (reserve[5] + Math.min(reserve[0] - anzahl[0], reserve[2] - anzahl[2]) < anzahl[5])
			return false;
		int reserveweiss = reserve[6]
				+ Math.min(Math.min(reserve[0] - anzahl[0], reserve[2] - anzahl[2]), reserve[1] - anzahl[1]);
		int reserveweiss1 = Math.min(reserve[3] - anzahl[3], reserve[2] - anzahl[2]);
		int reserveweiss2 = Math.min(reserve[4] - anzahl[4], reserve[0] - anzahl[0]);
		int reserveweiss3 = Math.min(reserve[5] - anzahl[5], reserve[1] - anzahl[1]);
		if (reserveweiss1 < 0)
			reserveweiss1 = 0;
		if (reserveweiss2 < 0)
			reserveweiss2 = 0;
		if (reserveweiss3 < 0)
			reserveweiss3 = 0;
		if (reserveweiss + reserveweiss1 + reserveweiss2 + reserveweiss3 < anzahl[6])
			return false;
		return true;
	}

	public static int getCap(int level) {
		return (int) (Math.log(level) * 2.7) + 3;
	}

	public Color get(int i) {
		return farben.get(i);
	}

	public int size() {
		return farben.size();
	}

	@Override
	public Iterator<Color> iterator() {
		return farben.iterator();
	}

	public int getColorCount(int color) {
		return anzahl[color];
	}

	public int[] getColorCount() {
		return anzahl;
	}
}
