package com.mygdx.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.util.Dice;

import static com.mygdx.game.util.Dice.d20;

public class Knight extends Entity {
    private int attackMod = 2;
    private int healTimer = 0;
    private int gold = 0;
    private int level = 1;

    public void levelUp() {
        level++;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getGold() {
        return gold;
    }

    public Knight(int hp, int armor, TextureRegion sprite, String name) {
        super(hp, armor, sprite, name);
    }

    public int getAttackMod() {
        return attackMod;
    }

    public void setAttackMod(int attackMod) {
        this.attackMod = attackMod;
    }

    @Override
    public String actionName(int id) {
        return null;
    }

    @Override
    public void action1(Entity target) {
        int roll1 = d20(attackMod), roll2 = d20(attackMod);
        int roll = roll1;

        if (target.dodging && roll2 < roll1)
            roll = roll2;

        Gdx.app.log(this.name + " action", "rolls: " + roll1 + " " + roll2 + " roll taken: " + roll);

        int dmg = Dice.d6(attackMod);
        if(roll >= target.armor){
            Gdx.app.log(this.name + " action", "HIT");
            target.loseHP(dmg);
        }
        else {
            Gdx.app.log(this.name + " action", "MISS");
        }
    }

    @Override
    public void action2(Entity target) {
        this.dodging = true;
        Gdx.app.log(this.name + " action", "dodging");
    }

    @Override
    public void action3(Entity target) {
        Gdx.app.log(this.name + " action", "heal Timer: " + healTimer);
        healTimer += 2;
    }

    @Override
    public void takeAction(Entity target, int actionID) {
        this.dodging = false;
        if (healTimer > 0) {
            healTimer--;
            int heal = Dice.d4();
            Gdx.app.log("potion", "healed " + heal + " hp " + healTimer + " turns active");
            this.gainHP(heal);
        }

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



    @Override
    public String action1Name() {
        return null;
    }

    @Override
    public String action2Name() {
        return null;
    }

    @Override
    public String action3Name() {
        return null;
    }
}
