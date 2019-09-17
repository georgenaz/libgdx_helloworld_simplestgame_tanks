package com.mygdx.game.utils;

import com.badlogic.gdx.Input;
import com.mygdx.game.units.Tank;

import java.security.Key;

public class KeysControl {
    enum Targeting {
        MOUSE, KEYBOARD
    }
    private int up;
    private int down;
    private int left;
    private int right;

    private Targeting tagreting;

    private int fire;
    private int rotateTurretLeft;
    private int rotateTurretRight;

    public KeysControl(int up, int down, int left, int right, Targeting tagreting, int fire, int rotateTurretLeft, int rotateTurretRight) {
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.tagreting = tagreting;
        this.fire = fire;
        this.rotateTurretLeft = rotateTurretLeft;
        this.rotateTurretRight = rotateTurretRight;
    }

    public static KeysControl createStandardControl1() {
        KeysControl keysControl = new KeysControl(
            Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT,
            Targeting.MOUSE,
            0, 0, 0
        );
        return  keysControl;
    }

    public static KeysControl createStandardControl2() {
        KeysControl keysControl = new KeysControl(
            Input.Keys.W, Input.Keys.S, Input.Keys.A, Input.Keys.D,
            Targeting.KEYBOARD,
            Input.Keys.SPACE, Input.Keys.LEFT_BRACKET, Input.Keys.RIGHT_BRACKET
        );
        return  keysControl;
    }

    public int getUp() {
        return up;
    }

    public int getDown() {
        return down;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public Targeting getTagreting() {
        return tagreting;
    }

    public int getFire() {
        return fire;
    }

    public int getRotateTurretLeft() {
        return rotateTurretLeft;
    }

    public int getRotateTurretRight() {
        return rotateTurretRight;
    }
}
