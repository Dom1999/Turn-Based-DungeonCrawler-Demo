package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.GameClass;
import com.mygdx.game.GdxUtils;
import com.mygdx.game.util.Constants;
import com.mygdx.game.util.GameManager;
import com.mygdx.game.util.assets.AssetDescriptors;
import com.mygdx.game.util.assets.RegionNames;

import static com.mygdx.game.util.GameManager.ATTACK_MOD;
import static com.mygdx.game.util.GameManager.PLAYER;
import static com.mygdx.game.util.GameManager.PLAYER_GOLD;
import static com.mygdx.game.util.GameManager.PLAYER_LEVEL;
import static com.mygdx.game.util.GameManager.UPGRADE_COST;

public class UpgradeScreen extends ScreenAdapter {

    private GameClass game;
    private SpriteBatch batch;
    private TextureRegion background;


    private Skin skin;
    private Table tableMain;

    private Label currentGoldLabel;
    private Label currentACLabel;
    private Label currentHPLabel;
    private Label currentATKLabel;
    private Label currentLevelLabel;
    private Label costLable;
    private Label nameLabel;

    private TextField nameTextField;

    private TextButton upgradeAttack;
    private TextButton upgradeDefence;
    private TextButton upgradeHealth;
    private TextButton resetProgress;

    private TextButton applyButton;
    private TextButton backButton;

    private FitViewport viewport;
    private AssetManager assetManager;
    private TextureAtlas atlas;
    private Stage stage;



    public UpgradeScreen(GameClass game) {
        GameManager.loadSettings();
        this.game = game;

        batch = game.getBatch();
        assetManager = game.getAssetManager();

        skin = assetManager.get(AssetDescriptors.DEFAULT_SKIN);
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        atlas = assetManager.get(AssetDescriptors.UI_ATLAS);
        background = atlas.findRegion(RegionNames.BACKGROUND);

        initGUI();
    }

    private void initGUI() {
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

        currentGoldLabel = new Label("" + GameManager.PLAYER_GOLD + " Gold",  new Label.LabelStyle(assetManager.get(AssetDescriptors.FONT), Color.GOLD));
        currentHPLabel = new Label("" + GameManager.PLAYER_HP + " Max HP",  new Label.LabelStyle(assetManager.get(AssetDescriptors.FONT), Color.WHITE));
        currentACLabel = new Label("" + GameManager.PLAYER_AC + " Armor",  new Label.LabelStyle(assetManager.get(AssetDescriptors.FONT), Color.WHITE));
        currentATKLabel = new Label(GameManager.ATTACK_MOD + " bonus",  new Label.LabelStyle(assetManager.get(AssetDescriptors.FONT), Color.WHITE));
        currentLevelLabel = new Label("" + GameManager.PLAYER_LEVEL,  new Label.LabelStyle(assetManager.get(AssetDescriptors.FONT), Color.GOLD));
        costLable = new Label("" + GameManager.UPGRADE_COST,  new Label.LabelStyle(assetManager.get(AssetDescriptors.FONT), Color.WHITE));

        float fontScale = 0.4f;
        currentGoldLabel.setFontScale(1);
        currentHPLabel.setFontScale(fontScale);
        currentACLabel.setFontScale(fontScale);
        currentATKLabel.setFontScale(fontScale);
        currentATKLabel.setFontScale(fontScale);

        int btnHeight = 30, btnWidth = 200;

        Table enterText = new Table();
        nameLabel = new Label("Name: ", new Label.LabelStyle(assetManager.get(AssetDescriptors.FONT), Color.WHITE));
        nameTextField = new TextField(GameManager.PLAYER_NAME, skin);
        Gdx.app.log("OPTIONS MENU", GameManager.PLAYER_NAME);
        nameTextField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameManager.PLAYER_NAME = nameTextField.getText();
            }
        });
        enterText.add(nameLabel).pad(5);
        enterText.add(nameTextField).width(btnWidth*2);
        enterText.add(currentLevelLabel);
        enterText.row();


        upgradeAttack = new TextButton("Upgrade Weapon", skin);
        upgradeAttack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (checkPrice(PLAYER.getGold())) {
                    PLAYER.setGold(PLAYER.getGold() - UPGRADE_COST);
                    PLAYER.levelUp();
                    PLAYER.setAttackMod(PLAYER.getAttackMod() + 1);
                    UPGRADE_COST += 50 * PLAYER.getLevel();
                    GameManager.saveSettings();
                }
            }
        });


        Table upgradeAttackBlock = new Table();
        upgradeAttackBlock.add(upgradeAttack).width(btnWidth).height(btnHeight);
        upgradeAttackBlock.add(currentATKLabel).pad(5);



        upgradeDefence = new TextButton("Upgrade Armor", skin);
        upgradeDefence.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

            }
        });

        Table upgradeDefenceBlock = new Table();
        upgradeDefenceBlock.add(upgradeDefence).width(btnWidth).height(btnHeight);
        upgradeDefenceBlock.add(currentACLabel).pad(5);

        upgradeHealth = new TextButton("Upgrade Health", skin);
        upgradeHealth.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

            }
        });

        Table upgradeHPBlock = new Table();
        upgradeHPBlock.add(upgradeHealth).width(btnWidth).height(btnHeight);
        upgradeHPBlock.add(currentHPLabel).pad(5);

        resetProgress = new TextButton("Retire Character", skin);
        resetProgress.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                GameManager.createNewCharacter();
                Gdx.app.log("UPGRADE SCREEN", "character reset");
            }
        });

        backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                GameManager.loadSettings();
                game.gotoScreen(GameClass.Screens.MAIN_MENU);
            }
        });

        applyButton = new TextButton("Apply", skin);
        applyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                applyUpgrades();
                GameManager.saveSettings();
                game.gotoScreen(GameClass.Screens.MAIN_MENU);
            }
        });


        tableMain = new Table(skin);
        tableMain.add(enterText).fillX().pad(5).row();

        Table outTable = new Table();
        outTable.add(backButton).width(btnWidth).height(btnHeight).pad(3);
        outTable.add(applyButton).width(btnWidth).height(btnHeight).pad(3);

        tableMain.add(currentGoldLabel).pad(5).row();
        tableMain.add(costLable).pad(5).row();
        tableMain.add(upgradeAttackBlock).fillX().pad(2).row();
        tableMain.add(upgradeDefenceBlock).fillX().pad(2).row();
        tableMain.add(upgradeHPBlock).fillX().pad(2).row();
        tableMain.add(resetProgress).fillX().pad(2).row();

        tableMain.add(outTable).width(btnWidth).height(btnHeight).pad(2).row();

        tableMain.setFillParent(true);
        tableMain.add(new Container()).expandY();
        tableMain.center();
        tableMain.pack();

        stage.addActor(tableMain);
    }

    private boolean checkPrice(int playerGold) {
        return playerGold >= UPGRADE_COST;
    }

    private void applyUpgrades() {
        GameManager.PLAYER_NAME = nameTextField.getText();
    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(viewport.getCamera().combined);
        updateLabels();

        batch.begin();
        GdxUtils.clearScreen();
        batch.draw(background, 0,0,viewport.getWorldWidth(), viewport.getWorldHeight());

        batch.end();
        stage.act(delta);
        stage.draw();
    }

    private void updateLabels() {
        currentGoldLabel.setText(PLAYER.getGold() + " Gold");
        currentHPLabel.setText(PLAYER.hp + " Max HP");
        currentACLabel.setText(PLAYER.armor + " Armor");
        currentATKLabel.setText("+" + PLAYER.getAttackMod() + " to attack rolls");
        currentLevelLabel.setText(" level " + PLAYER.getLevel());
        nameTextField.setText(PLAYER.name);
        costLable.setText("Cost: " + GameManager.UPGRADE_COST);

    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

}
