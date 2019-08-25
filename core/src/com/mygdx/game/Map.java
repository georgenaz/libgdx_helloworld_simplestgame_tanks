package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class Map {
    private enum WallType {
        HARD(0, 5, true),
            SOFT(1, 3, true),
            INDESTRUCTABLE(2,1, false),
            NONE(0,0, false);

        int index;
        int maxHp;
        boolean destructible;

        WallType(int index, int maxHp, boolean destructible) {
            this.index = index;
            this.maxHp = maxHp;
            this.destructible = destructible;
        }
    }

    private class CELL {
        WallType type;
        int hp;

        public CELL(WallType type) {
            this.type = type;
            this.hp = type.maxHp;
        }

        public void damage() {
            if (this.type == WallType.INDESTRUCTABLE) {
                return;
            }
            hp--;
            if (hp <= 0) {
                type = WallType.NONE;
            }
        }

        public void changeType(WallType type) {
            this.type = type;
            this.hp = type.maxHp;
        }
    }

    public static final int SIZE_X = 40;
    public static final int SIZE_Y = 23;
    public static final int CELL_SIZE = 32;

    private TextureRegion grassTexture;
    private TextureRegion[][] wallsTexture;
    private CELL cells[][];

    public Map(TextureAtlas atlas) {
        this.wallsTexture = new TextureRegion(atlas.findRegion("walls")).split(CELL_SIZE, CELL_SIZE);
        this.grassTexture = atlas.findRegion("grass");
        this.cells = new CELL[SIZE_X][SIZE_Y];
        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < SIZE_Y; j++) {
                cells[i][j] = new CELL(WallType.NONE);
                if (j == 0 || j > 0 && i%j == 0) {
                    if (MathUtils.random() < 0.8f) {
                        cells[i][j].changeType(WallType.HARD);
                    }
                    else {
                        cells[i][j].changeType(WallType.SOFT);
                    }
                }
            }
        }
        for (int i = 0; i < SIZE_X; i++) {
            cells[i][0].changeType(WallType.INDESTRUCTABLE);
            cells[i][SIZE_Y - 1].changeType(WallType.INDESTRUCTABLE);
        }
        for (int i = 0; i < SIZE_Y; i++) {
            cells[0][i].changeType(WallType.INDESTRUCTABLE);
            cells[SIZE_X - 1][i].changeType(WallType.INDESTRUCTABLE);
        }
    }

    public void checkWallAndBulletCollision(Bullet bullet) {
        int cx = (int) bullet.getPosition().x / CELL_SIZE;
        int cy = (int) bullet.getPosition().y / CELL_SIZE;

        if (cx >= 0 && cy >= 0 && cx <= SIZE_X && cy <= SIZE_Y) {
            if (cells[cx][cy].type != WallType.NONE) {
                cells[cx][cy].damage();
                bullet.deactivate();
            }
        }
    }

    public boolean isAreaCleared(float x, float y, float halfSize) {
        int leftX = (int) (x - halfSize) / CELL_SIZE;
        int rightX = (int) (x + halfSize) / CELL_SIZE;

        int bottomY = (int) (y - halfSize) / CELL_SIZE;
        int topY = (int) (y + halfSize) / CELL_SIZE;

        if (leftX < 0) {
            leftX = 0;
        }
        if (rightX >= SIZE_X) {
            rightX = SIZE_X - 1;
        }

        if (bottomY < 0) {
            bottomY = 0;
        }
        if (topY >= SIZE_Y) {
            topY = SIZE_Y - 1;
        }

        for (int i = leftX; i <= rightX; i++) {
            for (int j = bottomY; j <= topY; j++) {
                if (cells[i][j].type != WallType.NONE) {
                    return false;
                }
            }
        }

        return true;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < SIZE_X; i++) {
            for (int j = 0; j < SIZE_Y; j++) {
                batch.draw(grassTexture, i * CELL_SIZE, j * CELL_SIZE);
                if (cells[i][j].type != WallType.NONE) {
                    batch.draw(wallsTexture[cells[i][j].type.index][cells[i][j].hp - 1], i * CELL_SIZE, j * CELL_SIZE);
                }
            }
        }
    }
}
