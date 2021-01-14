package com.mygdx.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.util.Dice;

import static com.mygdx.game.util.Dice.d20;

public class Skeleton extends Entity {




    public Skeleton(int hp, int armor, TextureRegion sprite, String name) {
        super(hp, armor, sprite, name);
        dodging = true;
    }

    public int swordStrike() {
        return Dice.d8();
    }

    public int maceStrike() {
        return Dice.d4(2);
    }

    @Override
    public String action1Name() {
        return "Sword Strike ";
    }

    @Override
    public String action2Name() {
        return "Mace Strike";
    }

    @Override
    public String action3Name() {
        return "Dodge";
    }

    @Override
    public void action1(Entity target) {
        Gdx.app.log(this.name + " action", "sword strike");
        int roll1 = d20(2), roll2 = d20(2);

        int roll = roll1;
        if (target.dodging && roll2 < roll1)
            roll = roll2;

        Gdx.app.log(this.name + " action", "rolls: " + roll1 + " " + roll2 + " roll taken: " + roll);

        if(roll >= target.armor){
            int dmg = Dice.d8(2);
            Gdx.app.log(this.name + " action", "HIT dmg taken " + dmg);
            target.loseHP(dmg);
        }
        else {
            Gdx.app.log(this.name + " action", "MISS");
        }
    }

    @Override
    public void action2(Entity target) {
        Gdx.app.log(this.name + " action", "mace strike");
        int roll = d20(2);

        if (target.dodging){
            roll+=15;
            int dmg = Dice.d12();
            target.loseHP(dmg);
            Gdx.app.log(this.name + " action", "rolled " + dmg);
        }

        else
        {
            if(roll >= target.armor){
                int dmg = Dice.d4();
                Gdx.app.log(this.name + " action", "HIT dmg taken " + dmg);
                target.loseHP(dmg);
            }
            else {
                Gdx.app.log(this.name + " action", "MISS");
            }
        }


    }

    @Override
    public void action3(Entity target) {
        Gdx.app.log(this.name + " action", "dodge");
        this.dodging = true;
    }

}
