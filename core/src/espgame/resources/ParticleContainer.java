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
    public final ParticleEffect explosion;
    public final ParticleEffect heman;

    public ParticleContainer() {
        eddyRot = new ParticleEffect();
        eddyBlau = new ParticleEffect();
        eddyGruen = new ParticleEffect();
        eddyGelb = new ParticleEffect();
        eddyCyan = new ParticleEffect();
        eddyMagenta = new ParticleEffect();
        eddyWeiss = new ParticleEffect();
        eddySchwarz = new ParticleEffect();

        explosion = new ParticleEffect();
        heman = new ParticleEffect();
    }

    public void loadParticles() {
        FileHandle eddyParticleSystem = Gdx.files.internal("particles/eddyParticle.p");
        FileHandle particleSprites = Gdx.files.internal("sprites/particles");

        eddyRot.load(eddyParticleSystem, particleSprites);
        setEmitterColor(eddyRot, Color.RED);

        eddyBlau.load(eddyParticleSystem, particleSprites);
        setEmitterColor(eddyBlau, Color.BLUE);

        eddyGruen.load(eddyParticleSystem, particleSprites);
        setEmitterColor(eddyGruen, Color.GREEN);

        eddyGelb.load(eddyParticleSystem, particleSprites);
        setEmitterColor(eddyGelb, Color.YELLOW);

        eddyCyan.load(eddyParticleSystem, particleSprites);
        setEmitterColor(eddyCyan, Color.CYAN);

        eddyMagenta.load(eddyParticleSystem, particleSprites);
        setEmitterColor(eddyMagenta, Color.MAGENTA);

        eddyWeiss.load(eddyParticleSystem, particleSprites);
        setEmitterColor(eddyWeiss, Color.WHITE);

        eddySchwarz.load(eddyParticleSystem, particleSprites);
        setEmitterColor(eddySchwarz, Color.GRAY);

        explosion.load(Gdx.files.internal("particles/explosion.p"), particleSprites);
        heman.load(Gdx.files.internal("particles/heman.p"), particleSprites);
    }

    private static void setEmitterColor(ParticleEffect effect, Color color) {
        effect.getEmitters().get(0).getTint().setColors(new float[]{color.r, color.g, color.b});
    }
}
