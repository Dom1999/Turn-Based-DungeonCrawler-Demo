package com.mygdx.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.gson.Gson;
import com.mygdx.game.GameClass;
import com.mygdx.game.objects.Knight;
import com.mygdx.game.util.assets.AssetDescriptors;
import com.mygdx.game.util.assets.RegionNames;


public class GameManager {



    public static String PLAYER_NAME = "Player 1";
    public static int PLAYER_LEVEL = 1;
    public static int PLAYER_HP = 15;
    public static int PLAYER_AC = 10;
    public static int PLAYER_GOLD = 0;
    public static int ATTACK_MOD = 2;

    public static int UPGRADE_COST = 100;

    public static Knight PLAYER;
    public static AssetManager assetManager;

    private static Gson gson;

    public GameManager(GameClass game) {
        assetManager = game.getAssetManager();
    }

    public static void saveSettings() {
        FileHandle file = Gdx.files.local("settings.json");
        gson = new Gson();
        KnightJSON jsonKnight;

        if (PLAYER == null){
            createNewCharacter();
            Gdx.app.log("ERROR", "shouldnt be here");
        }


        updatePlayer();
        jsonKnight = new KnightJSON(PLAYER);


        file.writeString(gson.toJson(jsonKnight), false);

        Gdx.app.log("SAVE SETTINGS", file.readString() + " SAVED");
    }

    public static void loadSettings() {
        gson = new Gson();
        FileHandle file = Gdx.files.local("settings.json");
        if (!file.exists()) {
            createNewCharacter();
        } else {

            KnightJSON jsonKnight = gson.fromJson(file.readString(), KnightJSON.class);
            Gdx.app.log("READ FILE", file.readString());

            PLAYER = new Knight(jsonKnight.hp, jsonKnight.armor, assetManager.get(AssetDescriptors.GAMEPLAY_ATLAS).findRegion(RegionNames.KNIGHT), jsonKnight.name);
            PLAYER.setGold(jsonKnight.gold);
            PLAYER.setAttackMod(jsonKnight.attackMod);

            updateCharacter();
        }

    }

    private static void updateCharacter() {
        PLAYER_NAME = PLAYER.name;
        PLAYER_HP = PLAYER.hp;
        PLAYER_AC = PLAYER.armor;
        PLAYER_GOLD = PLAYER.getGold();
        ATTACK_MOD = PLAYER.getAttackMod();
        PLAYER_LEVEL = PLAYER.getLevel();
    }

    private static void updatePlayer() {
        PLAYER.name = PLAYER_NAME;
        PLAYER.hp = PLAYER_HP;
        PLAYER.armor = PLAYER_AC;
        PLAYER.setGold(PLAYER_GOLD);
        PLAYER.setAttackMod(ATTACK_MOD);
        PLAYER.setLevel(PLAYER_LEVEL);
    }

    public static void createNewCharacter() {
        PLAYER = new Knight(15, 10, assetManager.get(AssetDescriptors.GAMEPLAY_ATLAS).findRegion(RegionNames.KNIGHT), "Dave");
        PLAYER.setGold(200);
        updateCharacter();
        updatePlayer();
        saveSettings();
    }

}

 class KnightJSON {
     public int hp;
     public int armor;
     public String name;
     public int attackMod = 2;
     public int gold = 0;
     public int level = 1;

     public KnightJSON(Knight knight) {
         this.hp = knight.hp;
         this.armor = knight.armor;
         this.name = knight.name;
         this.attackMod = knight.getAttackMod();
         this.gold = knight.getGold();
         this.level = knight.getLevel();
     }
}