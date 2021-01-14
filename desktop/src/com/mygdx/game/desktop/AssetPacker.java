package com.mygdx.game.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class AssetPacker {
    private static final String RAW_ASSETS_PATH = "desktop/src/com/mygdx/game/desktop/assets-raw";
    private static final String ASSETS_PATH = "android/assets";

    public static void main(String[] args) {
        TexturePacker.Settings settings = new TexturePacker.Settings();
        TexturePacker.process(settings, "C:\\Users\\Dominik\\Desktop\\FERI\\3.letnik\\Razvoj Racunalniskih Iger (RRI)\\DungeonCrawl\\android\\assets\\assets-raw\\gameplay",
                ASSETS_PATH + "/gameplay", "gameplay");
        //TexturePacker.process(settings, RAW_ASSETS_PATH + "/ui", ASSETS_PATH + "/ui", "ui");

    }
}
