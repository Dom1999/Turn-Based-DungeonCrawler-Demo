package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.screens.MainMenu;
import com.mygdx.game.util.GameManager;
import com.mygdx.game.util.assets.AssetDescriptors;

public class GameClass extends Game {
	SpriteBatch batch;
	Texture img;
	GameManager gameManager;

	private AssetManager assetManager = new AssetManager();

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
		assetManager.finishLoading();
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
		img.dispose();
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
				//super.setScreen(new OptionsMenu(this));
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
}
