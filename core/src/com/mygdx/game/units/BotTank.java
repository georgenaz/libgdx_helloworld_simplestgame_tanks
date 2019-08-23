package com.mygdx.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Weapon;
import com.mygdx.game.utils.Direction;

public class BotTank extends Tank {
    Direction preferredDirection;
    float aiTimer;
    float aiTimerTo;

    boolean active;

    public boolean isActive() {
        return active;
    }

    public void activate(float x, float y) {
        this.hp = this.hpMax;
        this.position.set(x, y);
        this.preferredDirection = Direction.values()[MathUtils.random(0, Direction.values().length - 1)];
        this.angle = preferredDirection.getAngle();
        this.aiTimer = 0.0f;
        this.active = true;
    }

    public BotTank(MyGdxGame game) {
        super(game);
        this.texture = new Texture("tank_bot.png");
        this.weapon = new Weapon();
        this.position = new Vector2(500.0f, 500.0f);
        this.speed = 100;
        this.width = texture.getWidth();
        this.height = texture.getHeight();
        this.hpMax = 5;
        this.hp = this.hpMax;
        this.aiTimerTo = 3.0f;
        this.preferredDirection = Direction.UP;
        this.active = false;
    }

    public void update(float dt) {
        aiTimer += dt;
        if (aiTimer >= aiTimerTo) {
            aiTimer = 0.0f;
            aiTimerTo = MathUtils.random(2.5f, 4.0f);
            preferredDirection = Direction.values()[MathUtils.random(0, Direction.values().length - 1)];
            angle = preferredDirection.getAngle();
        }
        position.add(speed * preferredDirection.getVx() * dt, speed * preferredDirection.getVy() * dt);
    }
}
