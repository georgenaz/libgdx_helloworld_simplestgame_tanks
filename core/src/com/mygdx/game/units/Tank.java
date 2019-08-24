package com.mygdx.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.SimplestGdxGameTanks;
import com.mygdx.game.utils.TankOwner;
import com.mygdx.game.utils.Utils;
import com.mygdx.game.Weapon;

public abstract class Tank {
    SimplestGdxGameTanks game;
    TankOwner ownerType;
    Weapon weapon;
    TextureRegion texture;
    TextureRegion textureHp;
    Vector2 position;
    Circle circle;

    int hp;
    int hpMax;

    float speed;
    float angle;

    float angleTurret;
    float fireTimer;

    int width;
    int height;

    public Tank(SimplestGdxGameTanks game) {
        this.game = game;
    }

    public Circle getCircle() {
        return circle;
    }

    public TankOwner getOwnerType() {
        return ownerType;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - width / 2 , position.y - height / 2, width / 2, height / 2, width, height, 1, 1, angle);
        batch.draw(weapon.getTexture(), position.x - width / 2 , position.y - height / 2, width / 2, height / 2, width, height, 1, 1, angleTurret);
        batch.setColor(0, 0, 0, 0.3f);
        batch.draw(textureHp, position.x - width / 2 - 1, position.y + height / 2 - 1, textureHp.getRegionWidth() + 2, 8 + 2);
        batch.setColor(0, 1, 0, 0.6f);
        batch.draw(textureHp, position.x - width / 2, position.y + height / 2, textureHp.getRegionWidth() * ((float) hp / hpMax) , 8);
        batch.setColor(1, 1, 1, 1);
    }

    public void rotateTurretToPoint(float pointX, float pointY, float dt) {
        float angleTo = Utils.getAngle(position.x, position.y, pointX, pointY);
        angleTurret = Utils.makeRotation(angleTurret, angleTo, 180.0f, dt);
        angleTurret = Utils.angleToFromNegPiTpPosPi(angleTurret);
    }

    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp < 0) {
            this.destroy();
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public abstract void destroy();

    public void fire() {
        if (fireTimer >= weapon.getFirePeriod()) {
            fireTimer = 0.0f;
            float angleRad = (float) Math.toRadians(angleTurret);
            game.getBulletEmitter().activate(this, position.x, position.y, 320.0f * (float) Math.cos(angleRad), 320.0f * (float) Math.sin(angleRad), weapon.getDamage());
        }
    }

    public void update(float dt) {
        if (fireTimer <= weapon.getFirePeriod()) {
            fireTimer += dt;
        }

        if (position.x < 0.0f + this.width / 2) {
            position.x = 0.0f + this.width / 2;
        }
        if (position.x > Gdx.graphics.getWidth() - this.width / 2) {
            position.x = Gdx.graphics.getWidth() - this.width / 2;
        }
        if (position.y < 0.0f + this.height / 2) {
            position.y = 0.0f + this.height / 2;
        }
        if (position.y > Gdx.graphics.getHeight() - this.height / 2) {
            position.y = Gdx.graphics.getHeight() - this.height / 2;
        }

        circle.setPosition(position);
    }
}
