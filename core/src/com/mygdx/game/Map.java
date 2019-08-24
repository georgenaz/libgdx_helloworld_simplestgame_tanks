package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sun.prism.shader.Texture_LinearGradient_REFLECT_AlphaTest_Loader;

public class Map {
    public static final int SIZE_X = 40;
    public static final int SIZE_Y = 23;
    public static final int CELL_SIZE = 32;

    private TextureRegion grassTexture;
    private TextureRegion wallTexture;
    private int obstacles[][];

    public Map(TextureAtlas atlas) {
        this.grassTexture = atlas.findRegion("grass");
        this.wallTexture = atlas.findRegion("wall_block");
        this.obstacles = new int[SIZE_X][SIZE_Y];
        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < 3; j++) {
                obstacles[i][SIZE_Y - 1 - j] = 5;
            }
        }
    }

    public void checkWallAndBulletCollision(Bullet bullet) {
        int cx = (int) bullet.getPosition().x / CELL_SIZE;
        int cy = (int) bullet.getPosition().y / CELL_SIZE;

        if (cx >= 0 && cy >= 0 && cx <= SIZE_X && cy <= SIZE_Y) {
            if (obstacles[cx][cy] > 0) {
                obstacles[cx][cy] -= bullet.getDamage();
                bullet.deactivate();
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < SIZE_Y; j++) {
                batch.draw(grassTexture, i * CELL_SIZE, j * CELL_SIZE);
                if (obstacles[i][j] > 0) {
                    batch.draw(wallTexture, i * CELL_SIZE, j * CELL_SIZE);
                }
            }
        }
    }
}
