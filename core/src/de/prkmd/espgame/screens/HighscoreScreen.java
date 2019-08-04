package de.prkmd.espgame.screens;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.prkmd.espgame.ESPGame;
import de.prkmd.espgame.level.Level;
import de.prkmd.espgame.mechanics.Highscore;
import de.prkmd.espgame.resources.AssetContainer;
import de.prkmd.espgame.resources.Einstellungen;

public class HighscoreScreen extends ESPMenu {

	public static final int HEADERPADDING = 15;
	public static final int MAX_VIEWMODE = 3;
	public static final int NONEWPOS = -1;
	public static final String SKILLNAME[] = { "Leicht", "Mittel", "Schwer" };

	public static ArrayList<Highscore> liste;

	private boolean cleartext;
	private boolean nameModus;
	private boolean playAgainEnabled;
	private int viewmode;
	private int newPos = NONEWPOS;

	private Label pageLB, schwierigkeitLB;
	private Button nextBT, prevBT, bacBT;
	private Table highscoreTable;
	private TextField newEntryTF;
	private ScrollPane pane;
	private Button playAgainBT;

	private Highscore newScore;

	public HighscoreScreen() {
		this(null);
	}

	public HighscoreScreen(Highscore score) {
		super(Level.STAR_PERCENTAGE / 3f);
		viewmode = MAX_VIEWMODE;

		playAgainEnabled = false;
		if (score != null) {
			init(score);
			playAgainEnabled = true;
		}
		// pageSwitch(0);
	}

	@Override
	public void init() {
		stage.setDebugAll(false);

		pageLB = new Label("Seite: 1", skin);
		prevBT = getImageButton(AssetContainer.LEFT, AssetContainer.LEFT_A);
		nextBT = getImageButton(AssetContainer.RIGHT, AssetContainer.RIGHT_A);
		bacBT = getImageButton(AssetContainer.MENU, AssetContainer.MENU_A);
		bacBT.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				onBackKlick();
			}
		});
		nextBT.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				pageSwitch(1);
			}
		});
		prevBT.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				pageSwitch(-1);
			}
		});
		playAgainBT = getImageButton(AssetContainer.ANLEITUNG, AssetContainer.ANLEITUNG_A);
		playAgainBT.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				ESPGame.game.newGame();
			}
		});

		highscoreTable = new Table(skin);
		updateHighscoreTable();

		ScrollPaneStyle style = new ScrollPaneStyle(skin.get(ScrollPaneStyle.class));
		style.background = null;
		pane = new ScrollPane(highscoreTable, style);
		pane.setFadeScrollBars(false);
		table.add(pane).expand().top().fillX().padTop(15).uniform();

		Table t = new Table(skin);
		t.add(prevBT);
		t.add(bacBT).padLeft(10).padRight(10);
		t.add(nextBT);

		Table buttons = new Table(skin);
		buttons.add(pageLB).left().padLeft(10).uniform();
		buttons.add(t).expand().center();
		if (playAgainEnabled) {
			buttons.add(playAgainBT).right().padRight(10).uniform();
		}

		table.row();
		table.add(buttons).padBottom(20).padTop(15).expandX().fill();

		if (newScore != null) {
			enableNameMode();
		}
		pageSwitch(0);
	}

	@Override
	public void show() {
		super.show();

		stage.setKeyboardFocus(newEntryTF);
		if (newEntryTF != null) {
			newEntryTF.setSelection(0, newEntryTF.getText().length());

			if (nameModus) {
				float x = newEntryTF.getX();
				float y = newEntryTF.getY();
				System.out.println("Scrolling to: " + x + ", " + y);
				pane.scrollTo(x, y, newEntryTF.getWidth(), newEntryTF.getHeight(), false, true);
			}
		}
	}

	private void init(Highscore score) {
		liste.add(score);
		Collections.sort(liste);
		newScore = score;
		viewmode = score.getSchwierigkeit();
		newPos = setNewPos();
	}

	public static void initHighscores(boolean first) {
		liste = new ArrayList<Highscore>();
		if (first) { // TODO init mit Default Werte hier
			saveHighscores();
		} else {
			loadHighscores();
		}
		Collections.sort(liste);
	}

	private void enableNameMode() {
		nameModus = true;
		cleartext = true;
		nextBT.setVisible(false);
		prevBT.setVisible(false);
		pageLB.setVisible(false);
		playAgainBT.setVisible(false);

		updateHighscoreTable();
	}

	private void disableNameMode() {
		nameModus = false;
		pageLB.setVisible(true);
		nextBT.setVisible(true);
		prevBT.setVisible(true);
		playAgainBT.setVisible(true);

		updateHighscoreTable();
		pageSwitch(0);
	}

	private void onBackKlick() {
		save();
		//
		// if (nameModus) {
		// disableNameMode();
		// }
		ESPGame.game.changeScreen(new MainMenu());
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

	private void updateHighscoreTable() {
		highscoreTable.clear();

		schwierigkeitLB = new Label("", skin);
		highscoreTable.add(new Label("Platz", skin)).expand().padBottom(HEADERPADDING).uniform();
		highscoreTable.add(new Label("Punkte", skin)).expand().padBottom(HEADERPADDING).uniform();
		highscoreTable.add(new Label("Name", skin)).expand().padBottom(HEADERPADDING).uniform();
		highscoreTable.add(new Label("Aufgestellt am", skin)).expand().padBottom(HEADERPADDING).uniform();
		highscoreTable.add(schwierigkeitLB).expand().padBottom(HEADERPADDING).uniform();

		Locale l = Locale.getDefault();
		DateFormat day = DateFormat.getDateInstance(DateFormat.FULL, l);
		DateFormat time = DateFormat.getTimeInstance(DateFormat.SHORT, l);

		ArrayList<Highscore> filderedList = new ArrayList<Highscore>();
		for (int i = 0; i < liste.size(); i++) {
			Highscore s = liste.get(i);
			if (viewmode == MAX_VIEWMODE || s.getSchwierigkeit() == viewmode) {
				filderedList.add(s);
			}
		}

		for (int i = 0; i < filderedList.size(); i++) {
			highscoreTable.row();
			Highscore s = filderedList.get(i);
			Color color = Color.WHITE;
			if (i % 2 == 1) {
				color = Color.LIGHT_GRAY;
			}
			if (s == newScore) {
				color = Color.ORANGE;
			}

			Label nummer = new Label("" + (i + 1), skin);
			Label score = new Label("" + s.getScore(), skin);
			Label name = new Label(s.getName(), skin);
			Label date = new Label(
					day.format(new Date(s.getTimestamp())) + " - " + time.format(new Date(s.getTimestamp())), skin);
			Label diff = new Label(SKILLNAME[s.getSchwierigkeit()], skin);

			newEntryTF = new TextField("", skin); // TODO getPlayerName
			newEntryTF.setTextFieldListener(new TextFieldListener() {
				@Override
				public void keyTyped(TextField textField, char key) {
					onKeyTyped(key);
				}
			});

			nummer.setColor(color);
			score.setColor(color);
			name.setColor(color);
			date.setColor(color);
			diff.setColor(color);

			highscoreTable.add(nummer);
			highscoreTable.add(score);
			if (nameModus && s == newScore) {
				highscoreTable.add(newEntryTF).fill();
				stage.setKeyboardFocus(highscoreTable);
				newEntryTF.getOnscreenKeyboard().show(true);
				String playername = ESPGame.game.getEinstellungen().getPlayername();
				if (playername == null || playername.trim().equals("")) {
					newEntryTF.setText(Einstellungen.DEFAULT_PLAYERNAME);
				} else {
					newEntryTF.setText(playername);
				}
			} else {
				highscoreTable.add(name);
			}
			highscoreTable.add(date);
			highscoreTable.add(diff);
		}
	}

	private void pageSwitch(int mod) {
		viewmode += mod;
		if (viewmode > MAX_VIEWMODE)
			viewmode = 0;
		if (viewmode < 0) {
			viewmode = MAX_VIEWMODE;
		}
		if (highscoreTable != null)
			updateHighscoreTable();

		String s = "Schwierigkeit";
		if (viewmode < MAX_VIEWMODE)
			s = "Nur: " + SKILLNAME[viewmode];

		if (schwierigkeitLB != null && pageLB != null) {
			schwierigkeitLB.setText(s);
			pageLB.setText("Seite " + (viewmode + 1) + " / " + (MAX_VIEWMODE + 1));
		}
	}

	private void onKeyTyped(char key) {
		// System.out
		// .println("changed? " + key + " (" + Character.getNumericValue(key) +
		// ") text? " + newEntryTF.getText());
		if (key == 13) {
			save();
			disableNameMode();
		}
		// if (cleartext) {
		// newEntryTF.setText(""+key);
		// cleartext = false;
		// }
	}

	private void save() {
		if (newScore != null) {
			String name = newEntryTF.getText().trim();
			if (name != null && !name.equals("")) {
				System.out.println("New Name: " + name);
				newScore.setName(name);
				ESPGame.game.getEinstellungen().setPlayername(name);
			}

			// EPSGame.setPlayerName(nameTF.getText());
			// ESPGame.game.getEinstellungen().setPlayername(nameTF.getText());
			// TODO do this
		}
		disableNameMode();
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
}
