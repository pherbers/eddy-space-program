package espgame.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;

/**
 * Created by Patrick on 04.09.2015.
 */
public class ParticleContainer {
    public final ParticleEffect eddyRot, eddyBlau, eddyGruen, eddyGelb, eddyCyan, eddyMagenta, eddyWeiss, eddySchwarz;

    public ParticleContainer() {
        eddyRot = new ParticleEffect();
        eddyBlau = new ParticleEffect();
        eddyGruen = new ParticleEffect();
        eddyGelb = new ParticleEffect();
        eddyCyan = new ParticleEffect();
        eddyMagenta = new ParticleEffect();
        eddyWeiss = new ParticleEffect();
        eddySchwarz = new ParticleEffect();
    }

    public void loadParticles() {
        FileHandle eddyParticleSystem = Gdx.files.internal("particles/eddyParticle.p");
        FileHandle eddyParticleSprite = Gdx.files.internal("sprites/particles");

        eddyRot.load(eddyParticleSystem, eddyParticleSprite);
        setEmitterColor(eddyRot, Color.RED);

        eddyBlau.load(eddyParticleSystem, eddyParticleSprite);
        setEmitterColor(eddyBlau, Color.BLUE);

        eddyGruen.load(eddyParticleSystem, eddyParticleSprite);
        setEmitterColor(eddyGruen, Color.GREEN);

        eddyGelb.load(eddyParticleSystem, eddyParticleSprite);
        setEmitterColor(eddyGelb, Color.YELLOW);

        eddyCyan.load(eddyParticleSystem, eddyParticleSprite);
        setEmitterColor(eddyCyan, Color.CYAN);

        eddyMagenta.load(eddyParticleSystem, eddyParticleSprite);
        setEmitterColor(eddyMagenta, Color.MAGENTA);

        eddyWeiss.load(eddyParticleSystem, eddyParticleSprite);
        setEmitterColor(eddyWeiss, Color.WHITE);

        eddySchwarz.load(eddyParticleSystem, eddyParticleSprite);
        setEmitterColor(eddySchwarz, Color.GRAY);

    }

    private static void setEmitterColor(ParticleEffect effect, Color color) {
        effect.getEmitters().get(0).getTint().setColors(new float[]{color.r, color.g, color.b});
    }
}
