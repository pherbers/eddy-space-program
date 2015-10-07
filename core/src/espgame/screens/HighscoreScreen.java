package espgame.screens;

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

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import espgame.ESPGame;
import espgame.level.Level;
import espgame.mechanics.Highscore;
import espgame.ui.menus.ESPMenu;
import espgame.ui.menus.MainMenu;

public class HighscoreScreen extends ESPMenu {

	public static final int HEADERPADDING = 15;
	public static final int MAX_VIEWMODE = 3;
	public static final int NONEWPOS = -1;
	public static final String SKILLNAME[] = { "Leicht", "Mittel", "Schwer" };
	public static final String DEFAULT_ENTRY = "Hier klicken um Namen einzugeben.";

	public static ArrayList<Highscore> liste;

	private boolean nameModus;
	private int viewmode;
	private int newPos = NONEWPOS;

	private Label pageLB, schwierigkeitLB;
	private TextButton nextBT, prevBT, bacBT;
	private Table highscoreTable;
	private TextField newEntryTF;

	private Highscore newScore;

	public HighscoreScreen() {
		this(null);
	}

	public HighscoreScreen(Highscore score) {
		super(Level.STAR_PERCENTAGE / 3f);
		viewmode = MAX_VIEWMODE;

		if (score != null) {
			init(score);
		}
		// pageSwitch(0);
	}

	@Override
	public void init() {
		Table scoreTB = new Table(skin);
		stage.setDebugAll(true);

		pageLB = new Label("Seite: 1", skin);
		prevBT = new TextButton("<", skin);
		nextBT = new TextButton(">", skin);
		bacBT = new TextButton("Zur�ck zum Men�", skin);
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

		highscoreTable = new Table(skin);
		updateHighscoreTable();

		ScrollPaneStyle style = new ScrollPaneStyle(skin.get(ScrollPaneStyle.class));
		style.background = null;
		ScrollPane pane = new ScrollPane(highscoreTable, style);
		pane.setFadeScrollBars(false);
		table.add(pane).expand().top().fillX().padTop(15);

		Table t = new Table(skin);
		t.add(prevBT);
		t.add(bacBT).padLeft(10).padRight(10);
		t.add(nextBT);

		Table buttons = new Table(skin);
		buttons.add(pageLB).left().padLeft(10);
		buttons.add(t).expand().center();

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
		newEntryTF.setSelection(0, 0);
		System.out.println("focus: " + stage.getKeyboardFocus());
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
		nextBT.setVisible(false);
		prevBT.setVisible(false);
		pageLB.setVisible(false);
		bacBT.setText("Highscore speichern");

		updateHighscoreTable();
	}

	private void disableNameMode() {
		nameModus = false;
		pageLB.setVisible(true);
		nextBT.setVisible(true);
		prevBT.setVisible(true);

		updateHighscoreTable();
		bacBT.setText("Zum Hauptmenue");
	}

	private void onBackKlick() {
		boolean b = nameModus;
		save();

		if (nameModus) {
			disableNameMode();
		}
		if (!b)
			ESPGame.game.setScreen(new MainMenu());
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
			s = "Die Datei um Highscores zu lesen enth�llt fehlerhafte Informationen!";
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
		highscoreTable.add(new Label("Platz", skin)).expand().padBottom(HEADERPADDING);
		highscoreTable.add(new Label("Punkte", skin)).expand().padBottom(HEADERPADDING);
		highscoreTable.add(new Label("Name", skin)).expand().padBottom(HEADERPADDING);
		highscoreTable.add(new Label("Aufgestellt am", skin)).expand().padBottom(HEADERPADDING);
		highscoreTable.add(schwierigkeitLB).expand().padBottom(HEADERPADDING);

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
					System.out.println("changed? " + key + " (" + Character.getNumericValue(key) + ") text? "
							+ newEntryTF.getText());
					if (key == 13) {
						onBackKlick();
					}
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
				String playername = ESPGame.game.getBufferedPlayerName();
				if (playername == null || playername.trim().equals("")) {
					newEntryTF.setText(DEFAULT_ENTRY);
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

	private void save() {
		if (newScore != null) {
			String name = newEntryTF.getText().trim();
			newScore.setName(name);
			System.out.println("New Name: " + name);
			if (name != null && !name.equals("")) {
				ESPGame.game.setBufferedPlayerName(name);
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