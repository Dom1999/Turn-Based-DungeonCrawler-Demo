package com.mygdx.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.util.GameManager;

public abstract class Entity implements Action {
    public int hp;
    public int maxHP;
    public int armor;
    public TextureRegion sprite;
    public String name;
    public boolean dodging = false;

    public Entity(int hp, int armor, TextureRegion sprite, String name) {
        this.hp = hp;
        maxHP = hp;
        this.armor = armor;
        this.sprite = sprite;
        this.name = name;
    }

    public String actionName(int id) {
        switch(id) {
            case 1:
                return this.action1Name();
            case 2:
                return this.action2Name();
            case 3:
                return this.action3Name();
        }
        return null;
    }

    @Override
    public void takeAction(Entity target, int actionID) {
        this.dodging = false;
        switch (actionID) {
            case 1:
                action1(target);
                break;
            case 2:
                action2(target);
                break;
            case 3:
                action3(target);
                break;
            default:
                Gdx.app.log("ERROR", "NO ACTION HAPPEND");
                break;
        }
    }


    public void loseHP(int num) {
        hp -= num;
    }
    public void gainHP(int num) {
        hp = (hp + num) % maxHP;
    }
}
