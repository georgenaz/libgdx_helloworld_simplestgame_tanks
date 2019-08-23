package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import org.w3c.dom.Text;

public class Weapon {
    private Texture texture;
    private float firePeriod;
    private int damage;

    public Weapon() {
        this.texture = new Texture("tank_gun.png");
        this.firePeriod = 0.4f;
        this.damage = 1;
    }

    public float getFirePeriod() {
        return firePeriod;
    }

    public Texture getTexture() {
        return texture;
    }

    public int getDamage() {
        return damage;
    }
}
