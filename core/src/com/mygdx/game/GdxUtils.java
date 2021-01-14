package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.graphics.Color;


public class GdxUtils {
    public static void clearScreen() {
        clearScreen(Color.DARK_GRAY);
    }

    public static void clearScreen(Color color) {
        // clear screen
        // DRY - Don't repeat yourself
        // WET - Waste everyone's time
        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}
