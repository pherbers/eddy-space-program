package espgame.ui.uielements;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import espgame.resources.AssetContainer;
import espgame.screens.ESPMenu;

public abstract class ESPSlider extends Table {

	private Slider mySlider;
	private float buttonStepsize;
	private float min, max, stepsize;

	public ESPSlider(float min, float max, float stepsize, float buttonStepsize, float padding, Skin skin) {
		super(skin);
		this.buttonStepsize = buttonStepsize;
		this.max = max;
		this.min = min;
		this.stepsize = stepsize;

		Button plusBT = ESPMenu.getImageButton(AssetContainer.BUTTONPLUS, AssetContainer.BUTTONPLUS_A);
		Button minusBT = ESPMenu.getImageButton(AssetContainer.BUTTONMINUS, AssetContainer.BUTTONMINUS_A);
		mySlider = new Slider(min, max, stepsize, false, skin);

		plusBT.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				mySlider.setValue(mySlider.getValue() + getButtonStepSize());
				return super.touchDown(event, x, y, pointer, button);
			}
		});
		minusBT.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				mySlider.setValue(mySlider.getValue() - getButtonStepSize());
				return super.touchDown(event, x, y, pointer, button);
			}
		});
		mySlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				valueChanged();
			}
		});

		add(minusBT).padRight(padding);
		add(mySlider).expand().fill();
		add(plusBT).padLeft(padding);
	}

	public abstract void valueChanged();

	public Slider getMySlider() {
		return mySlider;
	}

	public float getButtonStepsize() {
		return buttonStepsize;
	}

	public float getMin() {
		return min;
	}

	public float getMax() {
		return max;
	}

	public float getStepsize() {
		return stepsize;
	}

	public float getValue() {
		return mySlider.getValue();
	}

	public float getButtonStepSize() {
		return buttonStepsize;
	}

	public boolean setValue(float value) {
		return mySlider.setValue(value);
	}

}
