package com.mygdx.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreeen;
import com.mygdx.game.SimplestGdxGameTanks;
import com.mygdx.game.Weapon;
import com.mygdx.game.utils.Direction;
import com.mygdx.game.utils.TankOwner;

public class PlayerTank extends Tank {
    int score;
    int lifes;

    public PlayerTank(GameScreeen game, TextureAtlas atlas) {
        super(game);
        this.ownerType = TankOwner.HUMAN;
        this.texture = atlas.findRegion("tank");
        this.textureHp = atlas.findRegion("bar");
        this.weapon = new Weapon(atlas);
        this.position = new Vector2(100.0f, 200.0f);
        this.speed = 100;
        this.width = texture.getRegionWidth();
        this.height = texture.getRegionHeight();
        this.hpMax = 10;
        this.hp = this.hpMax;
        this.circle = new Circle(position.x, position.y, (float) (width + height / 1.8f));
        this.lifes = 7;
        this.score = 0;
    }

    public void renderHUD(SpriteBatch batch, BitmapFont font24) {
        font24.draw(batch, "Score: " + score + "\nLives: " + this.lifes, 10, 700);
    }

    public void checkMovement(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            move(Direction.LEFT, dt);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            move(Direction.RIGHT, dt);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            move(Direction.UP, dt);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            move(Direction.DOWN, dt);
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

    public void addScore(int amount) {
        score += amount;
    }

    @Override
    public void destroy() {
        lifes--;
        hp = hpMax;
    }
}
