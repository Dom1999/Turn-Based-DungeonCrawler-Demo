package com.mygdx.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.GameClass;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.util.Constants;
import com.mygdx.game.util.Dice;
import com.mygdx.game.util.assets.AssetDescriptors;

import static com.mygdx.game.util.Dice.d20;

public class Skeleton extends Entity {

    private Sound missSFX;
    private Sound maceSFX;
    private Sound swordSFX;
    private Sound blockedSFX;


    public Skeleton(int hp, int armor, TextureRegion sprite, String name) {
        super(hp, armor, sprite, name);
        dodging = true;
        missSFX = GameClass.assetManager.get(AssetDescriptors.MISS_SOUND);
        maceSFX = GameClass.assetManager.get(AssetDescriptors.MACE_SOUND);
        swordSFX = GameClass.assetManager.get(AssetDescriptors.SWORD_SOUND);
        blockedSFX = GameClass.assetManager.get(AssetDescriptors.BLOCKED_SOUND);
    }

    @Override
    public void draw(SpriteBatch batch, Animation animation, float deltaTime) {
        playAnimation(batch, animation, deltaTime, Constants.ENEMY_POSITION_X,Constants.ENEMY_POSITION_Y,
                Constants.ENEMY_WIDTH, Constants.ENEMY_HEIGHT);
    }

    private float timePassed = 0;
    private boolean animationReset = true;
    @Override
    public void playAnimation(SpriteBatch batch, Animation animation, float deltaTime, float x, float y, float width, float height) {
        timePassed += deltaTime;
        if (animation.getKeyFrames().length > 1)
            GameScreen.animationsPlaying = true;

        batch.draw((TextureRegion) animation.getKeyFrame(timePassed, true), x, y, width, height);

        //Gdx.app.log("ANIMATION", "" + animation.isAnimationFinished(timePassed) + " " + timePassed + " " + animation.getKeyFrames().length);

        if (animation.isAnimationFinished(timePassed)) {
            //GameScreen.animationID = 0;
            GameScreen.enemyAnimationID = 0;
            timePassed = 0;
        }

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
        return "Nothing";
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
            swordSFX.play();
            int dmg = Dice.d8(2);
            Gdx.app.log(this.name + " action", "HIT dmg taken " + dmg);
            target.loseHP(dmg);
        }
        else {
            if (target.dodging)
                blockedSFX.play();
            else
                missSFX.play();

            Gdx.app.log(this.name + " action", "MISS");
        }
    }

    @Override
    public void action2(Entity target) {
        Gdx.app.log(this.name + " action", "mace strike");
        int roll = d20(2);

        if (target.dodging){
            int dmg = Dice.d12();
            target.loseHP(dmg);
            Gdx.app.log(this.name + " action", "rolled " + dmg);
            maceSFX.play();
        }

        else
        {
            if(roll >= target.armor){
                int dmg = Dice.d4();
                Gdx.app.log(this.name + " action", "HIT dmg taken " + dmg);
                target.loseHP(dmg);
                maceSFX.play();
            }
            else {
                missSFX.play();
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
