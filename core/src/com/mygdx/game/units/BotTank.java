package com.mygdx.game.units;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.GameScreeen;
import com.mygdx.game.SimplestGdxGameTanks;
import com.mygdx.game.Weapon;
import com.mygdx.game.utils.Direction;
import com.mygdx.game.utils.TankOwner;

public class BotTank extends Tank {
    Direction preferredDirection;
    float aiTimer;
    float aiTimerTo;

    float pursuitRadius;
    boolean active;
    Vector3 lastPosition;

    public boolean isActive() {
        return active;
    }

    public BotTank(GameScreeen game, TextureAtlas atlas) {
        super(game);
        this.ownerType = TankOwner.AI;
        this.texture = atlas.findRegion("tank_bot");
        this.textureHp = atlas.findRegion("bar");
        this.weapon = new Weapon(atlas);
        this.position = new Vector2(500.0f, 500.0f);
        this.lastPosition = new Vector3(0.0f, 0.0f, 0.0f);
        this.speed = 80;
        this.width = texture.getRegionWidth();
        this.height = texture.getRegionHeight();
        this.hpMax = 4;
        this.hp = this.hpMax;
        this.aiTimerTo = 5.0f;
        this.pursuitRadius = 300.0f;
        this.preferredDirection = Direction.UP;
        this.active = false;
        this.circle = new Circle(position.x, position.y, (float) (width + height / 2) / 1.8f);
    }

    public void activate(float x, float y) {
        this.hp = this.hpMax;
        this.position.set(x, y);
        this.preferredDirection = Direction.values()[MathUtils.random(0, Direction.values().length - 1)];
        this.angle = preferredDirection.getAngle();
        this.aiTimer = 0.0f;
        this.active = true;
    }

    public void update(float dt) {
        aiTimer += dt;
        if (aiTimer >= aiTimerTo) {
            aiTimer = 0.0f;
            aiTimerTo = MathUtils.random(3.5f, 6.0f);
            preferredDirection = Direction.values()[MathUtils.random(0, Direction.values().length - 1)];
            angle = preferredDirection.getAngle();
        }

        if (Math.abs(position.x - lastPosition.x) < 0.5f && Math.abs(position.y - lastPosition.y) < 0.5f) {
            lastPosition.z += dt;
            if (lastPosition.z > 0.25f) {
                aiTimer += 10.0f;
            }
        }
        else {
            lastPosition.x = position.x;
            lastPosition.y = position.y;
            lastPosition.z = 0.0f;
        }
        move(preferredDirection, dt);

        float dst = this.position.dst(gameScreen.getPlayerTank().getPosition());
        if (dst < pursuitRadius) {
            rotateTurretToPoint(gameScreen.getPlayerTank().getPosition().x, gameScreen.getPlayerTank().getPosition().y, dt);
            fire();
        }

        super.update(dt);
    }

    @Override
    public void destroy() {
        active = false;
    }
}
