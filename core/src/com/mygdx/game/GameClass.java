package com.mygdx.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.screens.MainMenu;
import com.mygdx.game.screens.UpgradeScreen;
import com.mygdx.game.util.GameManager;
import com.mygdx.game.util.assets.AssetDescriptors;
import com.mygdx.game.util.net.ConnectionManager;

public class GameClass extends Game {
	SpriteBatch batch;
	Texture img;
	GameManager gameManager;

	public static AssetManager assetManager = new AssetManager();
	private ConnectionManager connectionManager = null;

	public static enum Screens {
		MAIN_MENU, GAME, UPGRADE;
	}
	@Override
	public void create () {
		Gdx.app.setLogLevel(Logger.DEBUG);


		batch = new SpriteBatch();

		assetManager = new AssetManager();
		assetManager.getLogger().setLevel(Logger.DEBUG);
		assetManager.load(AssetDescriptors.DEFAULT_SKIN);
		assetManager.load(AssetDescriptors.UI_ATLAS);
		assetManager.load(AssetDescriptors.TITLE_FONT);
		assetManager.load(AssetDescriptors.FONT);
		assetManager.load(AssetDescriptors.GAMEPLAY_ATLAS);
		assetManager.load(AssetDescriptors.SWORD_SOUND);
		assetManager.load(AssetDescriptors.MACE_SOUND);
		assetManager.load(AssetDescriptors.MISS_SOUND);
		assetManager.load(AssetDescriptors.DRINK_SOUND);
		assetManager.load(AssetDescriptors.BLOCKED_SOUND);
		assetManager.finishLoading();
		gameManager = new GameManager(this);
		goToFirstScreen();

	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		batch.dispose();
		assetManager.dispose();

	}

	private void goToFirstScreen() {
		gotoScreen(Screens.MAIN_MENU);
	}


	public void gotoScreen(Screens screenType) {
		switch (screenType) {
			case MAIN_MENU:
				super.setScreen(new MainMenu(this));
				break;
			case UPGRADE:
				super.setScreen(new UpgradeScreen(this));
				break;
			case GAME:
				super.setScreen(new GameScreen(this));
				break;
			default:
		}
		System.gc();


	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public ConnectionManager getConnectionManager() {
		return connectionManager;
	}

	public void setConnectionManager(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}
}
