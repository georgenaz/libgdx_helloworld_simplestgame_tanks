package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    private Vector2 position;
    private Vector2 velocity;
    private int damage = 0;
    private boolean active;

    public Vector2 getPosition() {
        return position;
    }

    public int getDamage() {
        return damage;
    }

    public Bullet() {
        this.position = new Vector2();
        this.velocity = new Vector2();
        this.active = false;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        if (position.x < 0.0f || position.x > 1280.0f || position.y < 0.0f || position.y > 720.0f) {
            active = false;
        }
    }

    public void deactivate() {
        active = false;
    }

    public void activate(float x, float y, float vx, float vy, int damage) {
        active = true;
        this.position.set(x, y);
        this.velocity.set(vx, vy);
        this.damage = damage;
    }

    public boolean isActive() {
        return active;
    }
}
