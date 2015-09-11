package espgame.resources;

import java.io.File;
import java.io.IOException;

import espgame.ESPGame;
import espgame.screens.HighscoreScreen;

public class FileManager {

	private ESPGame game;
	private File highscoresFile, optionsFile, fileDir;

	public FileManager(ESPGame game) {
		this.game = game;
	}

	public void initHighScores() {
		String dir = getSystemEddyFolder();
		File d = new File(dir + "/Eddy Space Program");
		File f = new File(d + "/highscores.eddy");
		File o = new File(d + "/options.eddy");

		if (f.exists() && d.exists()) {
			// Hat schonmal gespielt
			System.out.println("Folder & File exist -> Played before");
			game.setFirstTimePlaying(false);
		} else {
			// Spielt zum ersten Mal
			Boolean b = d.mkdirs();
			System.out.println("Folder or File does not exist" + b);
			try {
				f.createNewFile();
			} catch (IOException e) {
				// game.showErrorMessage("Konnte die Datei für Highscores nicht
				// anlegen. Du wirst keine Highscores anlegen können.\nEin
				// Neustart des Spiel könnte helfen.");
				// TODO show error message
				e.printStackTrace();
			}
			game.setFirstTimePlaying(true);
		}

		setFileDir(d);
		setOptionsFile(o);
		setHighscoresFile(f);
		HighscoreScreen.initHighscores(game.isFirstTimePlaying());

		// if (!loadOptions())
		// einstellungen = Einstellungen.getDefaultEinstellungen();
		// applyOptions();
		//TODO settings
	}
	
	public String getSystemEddyFolder(){
		String dir = System.getProperty("user.home");
		if(System.getProperty("os.name").toLowerCase().contains("windows")){
			dir = System.getenv("APPDATA");
		}
		
		return dir;
	}

	public File getHighscoresFile() {
		return highscoresFile;
	}

	public void setHighscoresFile(File highscoresFile) {
		this.highscoresFile = highscoresFile;
	}

	public File getOptionsFile() {
		return optionsFile;
	}

	public void setOptionsFile(File optionsFile) {
		this.optionsFile = optionsFile;
	}

	public File getFileDir() {
		return fileDir;
	}

	public void setFileDir(File fileDir) {
		this.fileDir = fileDir;
	}

}
