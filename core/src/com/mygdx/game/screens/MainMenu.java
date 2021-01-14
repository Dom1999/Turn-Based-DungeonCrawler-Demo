package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.GameClass;
import com.mygdx.game.GdxUtils;
import com.mygdx.game.util.Constants;
import com.mygdx.game.util.GameManager;
import com.mygdx.game.util.assets.AssetDescriptors;
import com.mygdx.game.util.assets.RegionNames;

public class MainMenu extends ScreenAdapter {

    private SpriteBatch batch;
    private GameClass game;
    private OrthographicCamera camera;

    private Skin skin;
    private Table table;
    private TextureRegion background;
    private Label titleLabel;
    private BitmapFont titleFont;
    private BitmapFont textFont;
    private TextButton playButton;
    private TextButton upgradeButton;
    private TextButton exitButton;

    private FitViewport viewport;
    private AssetManager assetManager;
    private TextureAtlas atlas;
    private Stage stage;


    public MainMenu(GameClass game) {
        this.game = game;

        batch = game.getBatch();
        assetManager = game.getAssetManager();
        atlas = assetManager.get(AssetDescriptors.UI_ATLAS);
        background = atlas.findRegion(RegionNames.BACKGROUND);
        titleFont =  assetManager.get(AssetDescriptors.TITLE_FONT);
        textFont = assetManager.get(AssetDescriptors.FONT);

        skin = assetManager.get(AssetDescriptors.DEFAULT_SKIN);
        camera = new OrthographicCamera(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);

        initGUI();
    }

    private void initGUI() {
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

        titleLabel = new Label(Constants.GAME_TITLE,  new Label.LabelStyle(titleFont, Color.WHITE));
        playButton = new TextButton("Play", skin);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.gotoScreen(GameClass.Screens.GAME);
            }
        });
        upgradeButton = new TextButton("Upgrade", skin);
        upgradeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.gotoScreen(GameClass.Screens.UPGRADE);
            }
        });
        exitButton = new TextButton("Exit", skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Gdx.app.exit();
            }
        });

        int btnHeight = 75, btnWidth = 500;


        table = new Table(skin);
        table.add(titleLabel).expandX().pad(5).row();
        table.add(playButton).height(btnHeight).width(btnWidth).pad(5).row();
        table.add(upgradeButton).height(btnHeight).width(btnWidth).pad(5).row();
        table.add(exitButton).height(btnHeight).width(btnWidth).pad(5).row();

        table.setFillParent(true);
        table.add(new Container()).expandY();
        table.pack();

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        GdxUtils.clearScreen();
        batch.draw(background, 0,0,viewport.getWorldWidth(), viewport.getWorldHeight());

        batch.end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}
