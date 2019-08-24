package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.w3c.dom.Text;

public class Weapon {
    private TextureRegion texture;
    private float firePeriod;
    private int damage;

    public Weapon(TextureAtlas atlas) {
        this.texture = atlas.findRegion("tank_gun");
        this.firePeriod = 0.4f;
        this.damage = 1;
    }

    public float getFirePeriod() {
        return firePeriod;
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public int getDamage() {
        return damage;
    }
}
