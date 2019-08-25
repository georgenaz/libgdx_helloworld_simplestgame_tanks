package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScreenManager {
    public enum ScreenType {
        MENU, GAME
    }

    private static ScreenManager ourInstance = new ScreenManager();

    public static ScreenManager getInstance() {
        return ourInstance;
    }

    private ScreenManager(){}

    private Game game;
    private GameScreeen gameScreen;

    public void init(Game game, SpriteBatch batch) {
        this.game = game;
        this.gameScreen = new GameScreeen(batch);
    }

    public void setScreen(ScreenType screenType) {
        Screen currentScreen = game.getScreen();
        switch (screenType) {
            case GAME:
                game.setScreen(gameScreen);
                break;
        }
        if (currentScreen != null) {
            currentScreen.dispose();
        }
    }
}
