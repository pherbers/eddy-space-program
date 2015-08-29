package espgame.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import espgame.entity.Kanone;

/**
 * Created by Patrick on 28.08.2015.
 */
public class KanoneController implements InputProcessor {

    private Kanone kanone;
    public KanoneController(Kanone kanone) {
        this.kanone = kanone;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
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
            System.out.println("Right mouse button");
        // TODO: Change to eddy select
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
        return false;
    }
}
