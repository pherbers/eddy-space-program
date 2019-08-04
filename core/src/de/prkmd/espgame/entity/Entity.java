package de.prkmd.espgame.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import de.prkmd.espgame.ESPGame;
import de.prkmd.espgame.util.MathUtils;

public abstract class Entity {

	protected float radius, rotation;
	protected int lifespan = -1;
	protected Vector2 position, velocity;
	protected boolean collidable = true, orbitable, explodeable = true;
	protected Sprite sprite;
	protected boolean visible = true;

	public abstract void render(SpriteBatch batch);

	public abstract void update();

	public Entity(float x, float y) {
		position = new Vector2(x, y);
		velocity = new Vector2();
	}

	public void remove() {
		ESPGame.getLevel().removeEntity(this);
	}

	public boolean checkCollision(Entity e) {
		if (collidable && e.collidable && position.dst2(e.getPosition()) < MathUtils.sqr(radius + e.getRadius()))
			return true;
		return false;
	}

	public void pushAway(Vector2 origin, float force) {
		Vector2 v = new Vector2(origin.x - position.x, origin.y - position.y);
		v.scl(force / v.len());
		velocity.add(v);
	}

	public void disableInterruptions() {
		setCollidable(false);
		setOrbitable(false);
		setExplodable(false);
	}

	public boolean isInScreen() {
		int y = (int) (Math.abs(ESPGame.getRenderHeight()) / ESPGame.getGamescale());
		int x = (int) (Math.abs(ESPGame.getRenderWidth()) / ESPGame.getGamescale());

		if (x > Math.abs(getX()) + getRadius() && y > Math.abs(getY()) + getRadius())
			return true;
		return false;
	}

	public int getlifespan() {
		return lifespan;
	}

	public void setlifespan(int lifespan) {
		this.lifespan = lifespan;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public void onRemove() {
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public float getX() {
		return position.x;
	}

	public void setX(float x) {
		position.x = x;
	}

	public boolean isCollidable() {
		return collidable;
	}

	public void setCollidable(boolean collidable) {
		this.collidable = collidable;
	}

	public boolean isOrbitable() {
		return orbitable;
	}

	public void setOrbitable(boolean orbitable) {
		this.orbitable = orbitable;
	}

	public float getY() {
		return position.y;
	}

	public void setY(float y) {
		position.y = y;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public void setPosition(float x, float y) {
		position.x = x;
		position.y = y;
	}

	public void setVelocity(float x, float y) {
		velocity.x = x;
		velocity.y = y;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public void ticklifespan() {
		if (lifespan > 0) {
			lifespan--;
		}
		if (lifespan == 0)
			remove();
	}

	/*
	 * public boolean isInScreen() { int y = (int)
	 * (Math.abs(Game.getRenderHeight()) / Game.getGamescale()); int x = (int)
	 * (Math.abs(Game.getRenderWidth()) / Game.getGamescale());
	 * 
	 * if (x > Math.abs(getX()) + getRadius() && y > Math.abs(getY()) +
	 * getRadius()) return true; return false; }
	 */

	public boolean isExplodeable() {
		return explodeable;
	}

	public void setExplodable(boolean explodeable) {
		this.explodeable = explodeable;
	}

}
