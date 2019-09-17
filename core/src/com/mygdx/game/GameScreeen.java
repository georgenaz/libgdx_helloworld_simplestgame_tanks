package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.units.BotTank;
import com.mygdx.game.units.PlayerTank;
import com.mygdx.game.units.Tank;
import com.mygdx.game.utils.KeysControl;

import java.util.ArrayList;
import java.util.List;

public class GameScreeen implements Screen {
    private SpriteBatch batch;
    private BitmapFont font24;
    private Map map;

    private List<PlayerTank> playerTanks;

    private BulletEmitter bulletEmitter;
    private BotEmitter botEmitter;
    private float gameTimer;
    private float worldTimer;
    private Stage stage;
    private boolean paused;
    private Vector2 mousePosition;
    private TextureRegion cursor;

    private static final boolean FRIENDLY_FIRE = false;

    public GameScreeen(SpriteBatch batch) {
        this.batch = batch;
    }

    public Vector2 getMousePosition() {
        return mousePosition;
    }

    public Map getMap() {
        return map;
    }

    public BulletEmitter getBulletEmitter() {
        return bulletEmitter;
    }

    public List<PlayerTank> getPlayerTanks() {
        return playerTanks;
    }

    @Override
    public void show() {
        TextureAtlas atlas = new TextureAtlas("game.pack.atlas");
        font24 = new BitmapFont(Gdx.files.internal("font24.fnt"));
        cursor = new TextureRegion(atlas.findRegion("cursor"));
        map = new Map(atlas);
        playerTanks = new ArrayList<>();
        playerTanks.add(new PlayerTank(1, this, KeysControl.createStandardControl1(), atlas));
        playerTanks.add(new PlayerTank(2, this, KeysControl.createStandardControl2(), atlas));
        bulletEmitter = new BulletEmitter(atlas);
        botEmitter = new BotEmitter(this, atlas);
        gameTimer = 5.0f;
        stage = new Stage();
        mousePosition = new Vector2();

        Skin skin = new Skin();
        skin.add("simpleButton", new TextureRegion(atlas.findRegion("simplest_button")));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;

        Group group = new Group();
        final TextButton pauseButton = new TextButton("Pause", textButtonStyle);
        final TextButton exitButton = new TextButton("Exit", textButtonStyle);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                paused = !paused;
            }
        });
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        pauseButton.setPosition(0, 32);
        exitButton.setPosition(0, 0);
        group.addActor(pauseButton);
        group.addActor(exitButton);
        group.setPosition(1140, 0);
        stage.addActor(group);
        Gdx.input.setInputProcessor(stage);
//        Gdx.input.setCursorCatched(true);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        ScreenManager.getInstance().getCamera().position.set(playerTank.getPosition().x, playerTank.getPosition().y, 0);
//        ScreenManager.getInstance().getCamera().update();

        batch.setProjectionMatrix(ScreenManager.getInstance().getCamera().combined);
        batch.begin();
        map.render(batch);

        for (int i = 0; i < playerTanks.size(); i++) {
            playerTanks.get(i).render(batch);
        }
        botEmitter.render(batch);
        bulletEmitter.render(batch);

        for (int i = 0; i < playerTanks.size(); i++) {
            playerTanks.get(i).renderHUD(batch, font24);
        }

        batch.draw(cursor,
            mousePosition.x - cursor.getRegionWidth() / 2, mousePosition.y - cursor.getRegionHeight() / 2,
            cursor.getRegionWidth()/2, cursor.getRegionHeight()/2,
            cursor.getRegionWidth(), cursor.getRegionHeight(),
            1, 1, -worldTimer * 50);

        batch.end();
        stage.draw();
    }

    public void update(float dt) {
        mousePosition.set(Gdx.input.getX(), Gdx.input.getY());
        ScreenManager.getInstance().getViewport().unproject(mousePosition);
        worldTimer += dt;
        if (!paused) {
            gameTimer += dt;
            if (gameTimer > 5.0f) {
                gameTimer = 0.0f;

                float coordX, coordY;
                do {
                    coordX = MathUtils.random(0, Gdx.graphics.getWidth());
                    coordY = MathUtils.random(0, Gdx.graphics.getHeight());
                } while (!map.isAreaCleared(coordX, coordY, 16));
                botEmitter.activate(coordX, coordY);
            }
            for (int i = 0; i < playerTanks.size(); i++) {
                playerTanks.get(i).update(dt);
            }
            botEmitter.update(dt);
            bulletEmitter.update(dt);
            checkCollisions();
        }
        stage.act(dt);
        Gdx.input.setInputProcessor(stage);
    }


    public void checkCollisions() {
        for (int i = 0; i < bulletEmitter.getBullets().length; i++) {
            Bullet bullet = bulletEmitter.getBullets()[i];
            if (bullet.isActive()) {
                for (int j = 0; j < botEmitter.getBots().length; j++) {
                    BotTank bot = botEmitter.getBots()[j];
                    if (bot.isActive()) {
                        if (checkBulletOwner(bot, bullet) && bot.getCircle().contains(bullet.getPosition())) {
                            bullet.deactivate();
                            bot.takeDamage(bullet.getDamage());
                            break;
                        }
                    }
                }
                for (int j = 0; j < playerTanks.size(); j++) {
                    PlayerTank player = playerTanks.get(j);
                    if (checkBulletOwner(player, bullet) && player.getCircle().contains(bullet.getPosition())) {
                        bullet.deactivate();
                        player.takeDamage(bullet.getDamage());
                    }
                }

                map.checkWallAndBulletCollision(bullet);
            }
        }
    }

    public boolean checkBulletOwner(Tank tank, Bullet bullet) {
        if (!FRIENDLY_FIRE) {
            return tank.getOwnerType() != bullet.getOwner().getOwnerType();
        }
        else {
            return tank != bullet.getOwner();
        }
    }

    @Override
    public void resize(int width, int height) {
        ScreenManager.getInstance().resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
