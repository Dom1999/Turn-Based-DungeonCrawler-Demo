package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.GameClass;
import com.mygdx.game.GdxUtils;
import com.mygdx.game.objects.Entity;
import com.mygdx.game.objects.Knight;
import com.mygdx.game.objects.Skeleton;
import com.mygdx.game.util.Constants;
import com.mygdx.game.util.Dice;
import com.mygdx.game.util.GameManager;
import com.mygdx.game.util.assets.AssetDescriptors;
import com.mygdx.game.util.assets.RegionNames;

import static com.mygdx.game.util.GameManager.PLAYER;

public class GameScreen extends ScreenAdapter {

    private SpriteBatch batch;
    private GameClass game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private AssetManager assetManager;

    private Skin skin;
    private Table table;
    private TextureAtlas atlas;
    private TextureRegion dungeon;
    private TextureRegion menu;
    private Knight player;
    private Entity enemy;
    private Stage stage;
    private BitmapFont textFont;
    private Label playerHP;
    private Label enemyHP;
    private Label enemyAction;

    private int maxEnemyHP;

    private TextButton attackBtn;
    private TextButton blockBtn;
    private TextButton healBtn;
    private TextButton back;

    private MyDialog endDialog;
    private Array<Animation> playerAnimations;
    private Array<Animation> enemyAnimations;

    boolean playerTurn = false, enemyTurn = false;
    boolean playerDead = false, enemyDead = false;
    public static int animationID = 0, enemyAnimationID = 0;
    public static boolean animationsPlaying = false;


    public GameScreen(GameClass game) {
        this.game = game;

        batch = game.getBatch();
        assetManager = game.getAssetManager();
        atlas = assetManager.get(AssetDescriptors.GAMEPLAY_ATLAS);
        dungeon = atlas.findRegion(RegionNames.DUNGEONS);
        menu = atlas.findRegion(RegionNames.ACTION_MENU);

        if (PLAYER == null) {
            GameManager.loadSettings();
        }
        player = PLAYER;
        enemy = spawnEnemy();

        skin = assetManager.get(AssetDescriptors.DEFAULT_SKIN);
        camera = new OrthographicCamera(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        textFont = assetManager.get(AssetDescriptors.FONT);

        initHUD();
        initAnimations();

        Gdx.app.log("ENEMY ANIMATIONS", "" + enemyAnimations.size);

    }

    private void initAnimations() {
        playerAnimations = new Array<>();
        enemyAnimations = new Array<>();

        //player Animations
        Animation playerAttack, playerIdle, playerBlock, playerHeal;
        Array<TextureRegion> attackFrames = new Array<>();
        attackFrames.add(assetManager.get(AssetDescriptors.GAMEPLAY_ATLAS).findRegion(RegionNames.KNIGHT));
        attackFrames.add(assetManager.get(AssetDescriptors.GAMEPLAY_ATLAS).findRegion(RegionNames.KNIGHT_ATTACK));
        playerAttack = new Animation(0.5f, attackFrames);

        Array<TextureRegion> idleFrames = new Array<>();
        idleFrames.add(assetManager.get(AssetDescriptors.GAMEPLAY_ATLAS).findRegion(RegionNames.KNIGHT));
        playerIdle = new Animation(1f, idleFrames);

        Array<TextureRegion> blockFrames = new Array<>();
        blockFrames.add(assetManager.get(AssetDescriptors.GAMEPLAY_ATLAS).findRegion(RegionNames.KNIGHT));
        blockFrames.add(assetManager.get(AssetDescriptors.GAMEPLAY_ATLAS).findRegion(RegionNames.KNIGHT_DEFENCE));
        playerBlock = new Animation(0.5f, blockFrames);

        Array<TextureRegion> healFrames = new Array<>();
        healFrames.add(assetManager.get(AssetDescriptors.GAMEPLAY_ATLAS).findRegion(RegionNames.KNIGHT));
        healFrames.add(assetManager.get(AssetDescriptors.GAMEPLAY_ATLAS).findRegion(RegionNames.KNIGHT_HEAL));
        healFrames.add(assetManager.get(AssetDescriptors.GAMEPLAY_ATLAS).findRegion(RegionNames.KNIGHT_HEAL2));
        healFrames.add(assetManager.get(AssetDescriptors.GAMEPLAY_ATLAS).findRegion(RegionNames.KNIGHT_HEAL));
        healFrames.add(assetManager.get(AssetDescriptors.GAMEPLAY_ATLAS).findRegion(RegionNames.KNIGHT_HEAL2));
        playerHeal = new Animation(0.3f, healFrames);

        playerAnimations.add(playerIdle);
        playerAnimations.add(playerAttack);
        playerAnimations.add(playerBlock);
        playerAnimations.add(playerHeal);

        Animation enemyIdle, enemyMace, enemySword;
        Array<TextureRegion> idleEnemyFrames = new Array<>();
        idleEnemyFrames.add(assetManager.get(AssetDescriptors.GAMEPLAY_ATLAS).findRegion(RegionNames.SKELETON));
        enemyIdle = new Animation(1f, idleEnemyFrames);


        Array<TextureRegion> enemySwordFrames = new Array<>();
        enemySwordFrames.add(assetManager.get(AssetDescriptors.GAMEPLAY_ATLAS).findRegion(RegionNames.SKELETON));
        enemySwordFrames.add(assetManager.get(AssetDescriptors.GAMEPLAY_ATLAS).findRegion(RegionNames.SKELETON_SWORD));
        enemySword = new Animation(0.5f, enemySwordFrames);

        Array<TextureRegion> enemyMaceFrames = new Array<>();
        enemyMaceFrames.add(assetManager.get(AssetDescriptors.GAMEPLAY_ATLAS).findRegion(RegionNames.SKELETON));
        enemyMaceFrames.add(assetManager.get(AssetDescriptors.GAMEPLAY_ATLAS).findRegion(RegionNames.SKELETON_MACE));
        enemyMace = new Animation(0.5f, enemyMaceFrames);

        enemyAnimations.add(enemyIdle);
        enemyAnimations.add(enemySword);
        enemyAnimations.add(enemyMace);
        enemyAnimations.add(enemyIdle);
    }

    private void initHUD() {
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

        playerHP = new Label("Hit Points: " + player.hp + "/" + PLAYER.maxHP, new Label.LabelStyle(textFont, Color.GREEN));
        playerHP.setFontScale(0.7f);

        enemyHP = new Label("Hit Points: " + enemy.hp + "/" + enemy.maxHP, new Label.LabelStyle(textFont, Color.RED));
        Pixmap labelColor = new Pixmap((int) enemyHP.getWidth() - 50, (int) enemyHP.getHeight(), Pixmap.Format.RGB888);
        labelColor.setColor(Color.BLACK);
        labelColor.fill();
        enemyHP.getStyle().background = new Image(new Texture(labelColor)).getDrawable();
        enemyHP.setFontScale(0.7f);

        enemyAction = new Label("", new Label.LabelStyle(textFont, Color.RED));
        enemyAction.getStyle().background = new Image(new Texture(labelColor)).getDrawable();
        enemyAction.setFontScale(0.7f);

        attackBtn = new TextButton("Strike", skin);
        attackBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playerActionID = 1;
                playerTurn = true;
            }
        });
        blockBtn = new TextButton("Block", skin);
        blockBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ACTION", "blocking");
                playerActionID = 2;
                playerTurn = true;
            }
        });
        healBtn = new TextButton("Drink Potion", skin);
        healBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ACTION", "drinking potion");
                playerActionID = 3;
                playerTurn = true;
            }
        });
        back = new TextButton("Back", skin);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.gotoScreen(GameClass.Screens.MAIN_MENU);
            }
        });

        int btnHeight = 50, btnWidth = 125;

        table = new Table();
        table.add(playerHP).pad(1).row();
        table.add(attackBtn).height(btnHeight).width(btnWidth).pad(1).row();
        table.add(blockBtn).height(btnHeight).width(btnWidth).pad(1).row();
        table.add(healBtn).height(btnHeight).width(btnWidth).pad(1).row();
        table.add(enemyHP).pad(1).row();
        table.add(enemyAction).row();
        table.add(back).height(btnHeight).width(btnWidth).pad(1).padTop(40).row();


        table.setFillParent(true);
        table.right().pad(5).center();
        table.add(new Container());
        table.right();
        table.pack();

        stage.addActor(table);
    }

    private void openDialog(String text) {
        endDialog = new MyDialog(text, skin, "dialog") {
        };
        endDialog.setSize(400, 300);
        endDialog.addButton("Ok", MyDialog.DialogSelected.yes, skin.get("default", TextButton.TextButtonStyle.class)).fillX().height(30).expand().left();

        endDialog.getContentTable().clearChildren();
        endDialog.show(stage);
    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        updateGameState();
        batch.end();
        stage.act(delta);
        stage.draw();

        if (!playerDead && !enemyDead && animationID == 0)
            combat();
    }

    private int enemyActionID = 0, playerActionID = 0;

    private void combat() {

        //Enemy Turn
        if (!enemyTurn) {
            enemyActionID = Dice.d3();
            enemyTurn = true;
        }

        //Player Turn
        if (playerTurn && enemyTurn){
            player.takeAction(enemy, playerActionID);

            if (enemy.hp <= 0 && animationID == 0) {
                int reward = Dice.d4()*50;
                openDialog(enemy.name + " slain, looted " + reward + " Gold");
                PLAYER.setGold(PLAYER.getGold() + reward);
                Gdx.app.log("FIGHT ENDED", "GOLD AMOUNT " + PLAYER.getGold());
                PLAYER.hp = PLAYER.maxHP;
                GameManager.saveSettings();

                enemyDead = true;
                Gdx.app.log("FIGHT ENDED", "you win");
                animationID = enemyAnimationID = 0;
            }


            enemy.takeAction(player, enemyActionID);

            if (player.hp <= 0) {
                openDialog( player.name + " has Fallen");
                playerDead = true;
                Gdx.app.log("FIGHT ENDED", "you died");
                GameManager.createNewCharacter();
                animationID = enemyAnimationID = 0;
            }

            Gdx.app.log("TURN ENDED", "actions taken");
            playerTurn = enemyTurn = false;
            animationID = playerActionID;
            enemyAnimationID = enemyActionID;

        }


    }

    private void updateGameState() {
        GdxUtils.clearScreen();
        playerHP.setText("Hit Points: " + player.hp + "/" + PLAYER.maxHP);
        enemyHP.setText("Hit Points: " + enemy.hp + "/" + maxEnemyHP);
        enemyAction.setText(enemy.actionName(enemyActionID));


        batch.draw(dungeon, -20,0,viewport.getWorldWidth()-100, viewport.getWorldHeight());

        Animation currAnimation = playerAnimations.get(animationID);
        if (!playerDead) {
            player.draw(batch, currAnimation, Gdx.graphics.getDeltaTime());

            if(currAnimation.isAnimationFinished(Gdx.graphics.getDeltaTime())){
                animationID = 0;
            }
        }
        Animation currEnemyAnimation = enemyAnimations.get(enemyAnimationID);
        if (!enemyDead) {
            enemy.draw(batch, currEnemyAnimation, Gdx.graphics.getDeltaTime());

            if(currEnemyAnimation.isAnimationFinished(Gdx.graphics.getDeltaTime())){
                enemyAnimationID = 0;
            }
        }

        batch.draw(menu, viewport.getWorldWidth() - 165,0, 165, viewport.getWorldHeight());
        while (currAnimation.isAnimationFinished(Gdx.graphics.getDeltaTime()) && currEnemyAnimation.isAnimationFinished(Gdx.graphics.getDeltaTime())) {}
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public Entity spawnEnemy() {
        if (Dice.d6() > 0) {
            maxEnemyHP = Dice.d12(3);
            return new Skeleton(maxEnemyHP, 12, atlas.findRegion(RegionNames.SKELETON), "Skeleton");
        }
        else return new Skeleton(5,5, atlas.findRegion(RegionNames.SKELETON), "Skeleton boyo");
    }
}
