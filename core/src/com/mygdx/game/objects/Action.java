package com.mygdx.game.objects;

public interface Action {
    String action1Name();
    String action2Name();
    String action3Name();


    void action1(Entity player);
    void action2(Entity player);
    void action3(Entity player);

    void takeAction(Entity player, int actionID);
}
