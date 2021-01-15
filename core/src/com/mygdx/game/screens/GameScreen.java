package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
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

    boolean playerTurn = false, enemyTurn = false;
    boolean playerDead = false, enemyDead = false;



    public GameScreen(GameClass game) {
        this.game = game;

        batch = game.getBatch();
        assetManager = game.getAssetManager();
        atlas = assetManager.get(AssetDescriptors.GAMEPLAY_ATLAS);
        dungeon = atlas.findRegion(RegionNames.DUNGEONS);
        menu = atlas.findRegion(RegionNames.ACTION_MENU);

        if (GameManager.PLAYER == null) {
            GameManager.loadSettings();
        }
        player = GameManager.PLAYER;
        enemy = spawnEnemy();

        skin = assetManager.get(AssetDescriptors.DEFAULT_SKIN);
        camera = new OrthographicCamera(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        textFont = assetManager.get(AssetDescriptors.FONT);

        initHUD();
    }

    private void initHUD() {
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

        playerHP = new Label("Hit Points: " + player.hp + "/" + GameManager.PLAYER.maxHP, new Label.LabelStyle(textFont, Color.GREEN));
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

        if (!playerDead && !enemyDead)
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

            if (enemy.hp <= 0) {
                int reward = Dice.d4()*50;
                openDialog(enemy.name + " slain, looted " + reward + " Gold");
                GameManager.PLAYER.setGold(GameManager.PLAYER.getGold() + reward);
                Gdx.app.log("FIGHT ENDED", "GOLD AMOUNT " + GameManager.PLAYER.getGold());
                GameManager.saveSettings();

                enemyDead = true;
                Gdx.app.log("FIGHT ENDED", "you win");
            }


            enemy.takeAction(player, enemyActionID);//enemyActionID

            if (player.hp <= 0) {
                openDialog("YOU DIED");
                playerDead = true;
                Gdx.app.log("FIGHT ENDED", "you died");
            }

            Gdx.app.log("TURN ENDED", "actions taken");
            playerTurn = enemyTurn = false;
        }


    }

    private void updateGameState() {
        GdxUtils.clearScreen();
        playerHP.setText("Hit Points: " + player.hp + "/" + GameManager.PLAYER.maxHP);
        enemyHP.setText("Hit Points: " + enemy.hp + "/" + maxEnemyHP);
        enemyAction.setText(enemy.actionName(enemyActionID));


        batch.draw(dungeon, -20,0,viewport.getWorldWidth()-100, viewport.getWorldHeight());

        if (!playerDead) {
            batch.draw(player.sprite, Constants.PLAYER_POSITION_X,Constants.PLAYER_POSITION_Y,
                    Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT);
        }
        if (!enemyDead) {
            batch.draw(enemy.sprite, Constants.ENEMY_POSITION_X,Constants.ENEMY_POSITION_Y,
                    Constants.ENEMY_WIDTH, Constants.ENEMY_HEIGHT);
        }

        batch.draw(menu, viewport.getWorldWidth() - 165,0, 165, viewport.getWorldHeight());
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
