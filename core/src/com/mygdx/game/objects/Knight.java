package com.mygdx.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.GameClass;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.util.Constants;
import com.mygdx.game.util.Dice;
import com.mygdx.game.util.assets.AssetDescriptors;
import com.mygdx.game.util.assets.RegionNames;

import static com.mygdx.game.util.Dice.d20;

public class Knight extends Entity {
    private int attackMod = 2;
    private int healTimer = 0;
    private int gold = 0;
    private int level = 1;

    private Sound missSFX;
    private Sound maceSFX;
    private Sound drinkSFX;

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
        missSFX = GameClass.assetManager.get(AssetDescriptors.MISS_SOUND);
        maceSFX = GameClass.assetManager.get(AssetDescriptors.MACE_SOUND);
        drinkSFX = GameClass.assetManager.get(AssetDescriptors.DRINK_SOUND);
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
            maceSFX.play();
            Gdx.app.log(this.name + " action", "HIT");

            target.loseHP(dmg);
        }
        else {
            missSFX.play();
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
        drinkSFX.play();
    }

    @Override
    public void takeAction(Entity target, int actionID) {
        this.dodging = false;

        if (healTimer > 0) {
            healTimer--;
            int heal = Dice.d4(2);
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

    @Override
    public void draw(SpriteBatch batch, Animation animation, float deltaTime) {
            playAnimation(batch, animation, deltaTime, Constants.PLAYER_POSITION_X,Constants.PLAYER_POSITION_Y, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT);
    }

    private float timePassed = 0;
    private boolean animationReset = true;
    @Override
    public void playAnimation(SpriteBatch batch, Animation animation, float deltaTime, float x, float y, float width, float height) {
        this.timePassed += deltaTime;


        batch.draw((TextureRegion) animation.getKeyFrame(timePassed, true), x, y, width, height);

        //Gdx.app.log("ANIMATION", "" + animation.isAnimationFinished(timePassed) + " " + timePassed + " " + animation.getKeyFrames().length);

        if (animation.isAnimationFinished(timePassed)) {
            GameScreen.animationID = 0;
            //GameScreen.enemyAnimationID = 0;
            this.timePassed = 0;
        }
    }
}
