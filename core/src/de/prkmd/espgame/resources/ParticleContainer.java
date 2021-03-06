package de.prkmd.espgame.resources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

public class ParticleContainer {
    public final ParticleEffect eddyRot, eddyBlau, eddyGruen, eddyGelb, eddyCyan, eddyMagenta, eddyWeiss, eddySchwarz;
    public final ParticleEffect explosion;
    public final ParticleEffect heman, hemanGet, sternschnuppe;
    public final ParticleEffect ship;

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
        hemanGet = new ParticleEffect();
        sternschnuppe = new ParticleEffect();
        ship = new ParticleEffect();
    }

    public void loadParticles() {
        FileHandle eddyParticleSystem = Gdx.files.internal("res/particles/eddyParticle.p");
        FileHandle particleSprites = Gdx.files.internal("res/sprites/particles");

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

        explosion.load(Gdx.files.internal("res/particles/explosion.p"), particleSprites);
        heman.load(Gdx.files.internal("res/particles/heman.p"), particleSprites);
        hemanGet.load(Gdx.files.internal("res/particles/hemanGet.p"), particleSprites);
        sternschnuppe.load(Gdx.files.internal("res/particles/sternschnuppe.p"), particleSprites);
        ship.load(Gdx.files.internal("res/particles/ship.p"), particleSprites);
    }

    private static void setEmitterColor(ParticleEffect effect, Color color) {
        effect.getEmitters().get(0).getTint().setColors(new float[]{color.r, color.g, color.b});
    }
}
