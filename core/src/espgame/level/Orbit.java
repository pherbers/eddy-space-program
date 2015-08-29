package espgame.level;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Orbit {

    float radius,force;
    Sprite sprite;
    private Vector2 position;

    public Orbit(Vector2 position, float radius, float force){
        this.setPosition(position);
        this.radius = radius;
        this.force = force;
        init();
    }
    public Orbit(float x, float y, float radius, float force){
        this.setPosition(new Vector2(x, y));
        this.radius = radius;
        this.force = force;
        init();
    }
    private void init(){
        // sprite = Sprites.ORBIT.getScaledCopy((float) ((radius*2)/1024));

    }
    public void setForce(float force){
        this.force = force;
    }

    public double getForce(){
        return force;
    }

    public void render(){
        // Game.drawImage(sprite, -radius, -radius);
    }
    public Vector2 getPosition() {
        return position;
    }
    public void setPosition(Vector2 position) {
        this.position = position;
    }
    public float getRadius() {
        return radius;
    }
    public void setRadius(float radius) {
        this.radius = radius;
    }

}
