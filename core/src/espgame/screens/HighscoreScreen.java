package espgame.screens;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import espgame.ESPGame;
import espgame.mechanics.Highscore;
import espgame.resources.AssetLoader;
import javafx.scene.control.Tab;

public class HighscoreScreen implements Screen {

	public static final int MAX_VIEWMODE = 3;
	public static final int NONEWPOS = -1;

	public static ArrayList<Highscore> liste;

	private boolean nameModus;
	private int viewmode;
	private int newPos = NONEWPOS;
	
	private Label pageLB;
	private Button nextBT, prevBT, backBT;

	private TextField nameTF;
	private Highscore newScore;
	private Stage stage;
	private Table table;
	private Skin skin;

	public HighscoreScreen() {
		viewmode = MAX_VIEWMODE;
		init();
	}

	public HighscoreScreen(Highscore score) {
		init(score);
	}

	private void init() {
		stage = new Stage();
		table = new Table();
		stage.addActor(table);
		stage.setDebugAll(true);
		
		skin = AssetLoader.get().getSkin();
	}

	private void init(Highscore score) {
		liste.add(score);
		Collections.sort(liste);
		newScore = score;
		viewmode = score.getSchwierigkeit();
		newPos = setNewPos();
		init();
	}

	@Override
	public void show() {
		Table scoreTB = new Table(skin);
		
		
		Table buttons = new Table(skin);
		buttons.add(pageLB).left();
		
		Table t = new Table(skin);
		t.add(prevBT);
		t.add(backBT);
		t.add(nextBT);
		
		buttons.add(t).expand().center();
		table.add(buttons).expand();
		
		System.out.println("Highscore show");
	}

	public static void initHighscores(boolean first) {
		liste = new ArrayList<Highscore>();
		if (first) {
			// TODO init mit Default Werte hier

			saveHighscores();
		} else {
			loadHighscores();
		}
		Collections.sort(liste);
	}

	public static void saveHighscores() {
		File outputFile = ESPGame.game.getFileManager().getHighscoresFile();
		try {
			if (!outputFile.exists())
				outputFile.createNewFile();

			FileOutputStream fo = new FileOutputStream(outputFile);
			ObjectOutputStream oo = new ObjectOutputStream(fo);
			oo.writeObject(liste);
			oo.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			// Game.showErrorMessage("Die Highscores konnten nicht gespeichert
			// werden! Die Datei konnte nicht gefunden werden!");
		} catch (IOException e) {
			// Game.showErrorMessage("Die Highscores konnten nicht gespeichert
			// werden! Auf die Datei konnte nicht zugegriffen werden!");
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static boolean loadHighscores() {
		File inputFile = ESPGame.game.getFileManager().getHighscoresFile();
		FileInputStream fi;
		boolean f = false;
		String s = "";
		try {
			if (!inputFile.exists())
				return false;

			fi = new FileInputStream(inputFile);
			ObjectInputStream oi = new ObjectInputStream(fi);
			liste = (ArrayList<Highscore>) oi.readObject();
			oi.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			s = "Die Datei um Highscores zu laden konnte nicht gefunden werden!";
			f = true;
		} catch (IOException e) {
			e.printStackTrace();
			s = "Die Highscores konnten nicht gelesen werden!";
			f = true;
		} catch (ClassNotFoundException e) {
			// TODO diese exception tritt auf wenn jemand Daten aus Version 1.0
			// hat! was nun?
			s = "Die Datei um Highscores zu lesen enthällt fehlerhafte Informationen!";
			f = true;
			e.printStackTrace();
		}
		if (f) {
			// Game.showErrorMessage(s);
			System.out.println("Error: " + s);
			// TODO display error in UI
			return false;
		}
		return true;
	}

	private void save() {
		if (nameModus) {
			newScore.setName(nameTF.getText());
			// EPSGame.setPlayerName(nameTF.getText());
			// ESPGame.game.getEinstellungen().setPlayername(nameTF.getText());
			// TODO do this
		}
		nameModus = false;
		saveHighscores();
	}

	private int setNewPos() {
		// Game.print(newScore);
		if (newScore == null) {
			return NONEWPOS;
		}
		int i = liste.indexOf(newScore);
		System.out.println("Der Spieler hat eienen neuen Highscore! Er ist auf Platz " + i);
		return i;
	}

	@Override
	public void render(float delta) {
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {

	}
}
