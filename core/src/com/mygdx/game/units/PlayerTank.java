package com.mygdx.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.SimplestGdxGameTanks;
import com.mygdx.game.Weapon;
import com.mygdx.game.utils.TankOwner;

public class PlayerTank extends Tank {
    int lifes;

    public PlayerTank(SimplestGdxGameTanks game, TextureAtlas atlas) {
        super(game);
        this.ownerType = TankOwner.HUMAN;
        this.texture = atlas.findRegion("tank");
        this.textureHp = atlas.findRegion("bar");
        this.weapon = new Weapon(atlas);
        this.position = new Vector2(100.0f, 100.0f);
        this.speed = 100;
        this.width = texture.getRegionWidth();
        this.height = texture.getRegionHeight();
        this.hpMax = 10;
        this.hp = this.hpMax;
        this.circle = new Circle(position.x, position.y, (float) (width + height / 1.8f));
        this.lifes = 7;
    }

    public void checkMovement(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            position.x -= speed * dt;
            angle = 180.0f;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            position.x += speed * dt;
            angle = 0.0f;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            position.y += speed * dt;
            angle = 90.0f;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            position.y -= speed * dt;
            angle = 270.0f;
        }
    }

    public void update(float dt) {
        checkMovement(dt);

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isTouched()) {
            this.fire();
        }

        float mx = Gdx.input.getX();
        float my = Gdx.graphics.getHeight() - Gdx.input.getY();

        rotateTurretToPoint(mx, my, dt);

        super.update(dt);
    }

    @Override
    public void destroy() {
        lifes--;
        hp = hpMax;
    }
}
