package espgame.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import espgame.ESPGame;
import espgame.entity.Kanone;

public class KanoneController implements InputProcessor {

    private Kanone kanone;
    public KanoneController(Kanone kanone) {
        this.kanone = kanone;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.NUM_1)
            ESPGame.getLevel().setSelectedEddy(0);
        if (keycode == Input.Keys.NUM_2)
            ESPGame.getLevel().setSelectedEddy(1);
        if (keycode == Input.Keys.NUM_3)
            ESPGame.getLevel().setSelectedEddy(2);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        kanone.updateMousePosition(screenX, screenY);
        if(button == Input.Buttons.LEFT)
            kanone.startChargeup();
        if(button == Input.Buttons.RIGHT)
            ESPGame.getLevel().setSelectedEddy((ESPGame.getLevel().getSelectedEddy() + 1) % 3);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        kanone.updateMousePosition(screenX, screenY);
        if(button == Input.Buttons.LEFT)
            kanone.shoot();
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        kanone.updateMousePosition(screenX, screenY);
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        kanone.updateMousePosition(screenX, screenY);
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        ESPGame.getLevel().setSelectedEddy(Math.floorMod(ESPGame.getLevel().getSelectedEddy() + amount, 3));
        return true;
    }
}
