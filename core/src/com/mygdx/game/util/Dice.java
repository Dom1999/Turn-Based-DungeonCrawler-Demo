package com.mygdx.game.util;

import com.badlogic.gdx.Gdx;

import java.util.Random;
import java.util.concurrent.Callable;

public class Dice {
    static Random rand = new Random();

    public static int d3() {
        return rand.nextInt(3)+1;
    }

    public static int d20() {
         return rand.nextInt(20)+1;
    }

    public static int d20(int mod) {
        return rand.nextInt(20) + mod+1;
    }

    public static int d12() {
        return rand.nextInt(12)+1;
    }

    public static int d12(int mod) {
        return rand.nextInt(12) + mod +1;
    }

    public static int d8() {
        return rand.nextInt(8)+1;
    }

    public static int d8(int mod) {
        return rand.nextInt(8) + mod+1;
    }

    public static int d6() {
        return rand.nextInt(6)+1;
    }

    public static int d6(int mod) {
        return rand.nextInt(6) + mod+1;
    }

    public static int d4() {
        return rand.nextInt(4)+1;
    }

    public static int d4(int mod) {
        return rand.nextInt(4) + mod+1;
    }


}
