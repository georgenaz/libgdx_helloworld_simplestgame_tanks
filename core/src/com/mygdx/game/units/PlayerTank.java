package com.mygdx.game.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameScreeen;
import com.mygdx.game.ScreenManager;
import com.mygdx.game.SimplestGdxGameTanks;
import com.mygdx.game.Weapon;
import com.mygdx.game.utils.Direction;
import com.mygdx.game.utils.KeysControl;
import com.mygdx.game.utils.TankOwner;

public class PlayerTank extends Tank {
    KeysControl keysControl;
    int index;
    int score;
    int lifes;

    public PlayerTank(int index, GameScreeen game, KeysControl keysControl, TextureAtlas atlas) {
        super(game);
        this.index = index;
        this.keysControl = keysControl;
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
        font24.draw(batch, "Score: " + score + "\nLives: " + this.lifes, 10 + (index - 1) * 200, 700);
    }

    public void checkMovement(float dt) {
        if (Gdx.input.isKeyPressed(keysControl.getLeft())) {
            move(Direction.LEFT, dt);
        }
        else if (Gdx.input.isKeyPressed(keysControl.getRight())) {
            move(Direction.RIGHT, dt);
        }
        else if (Gdx.input.isKeyPressed(keysControl.getUp())) {
            move(Direction.UP, dt);
        }
        else if (Gdx.input.isKeyPressed(keysControl.getDown())) {
            move(Direction.DOWN, dt);
        }
    }

    public void update(float dt) {
        checkMovement(dt);
        rotateTurretToPoint(gameScreen.getMousePosition().x, gameScreen.getMousePosition().y, dt);
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isTouched()) {
            this.fire();
        }

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
