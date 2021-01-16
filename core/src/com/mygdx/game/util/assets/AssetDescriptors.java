package com.mygdx.game.util.assets;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetDescriptors {
    public static AssetDescriptor<Skin> DEFAULT_SKIN =
            new AssetDescriptor<>(AssetPaths.SKIN, Skin.class);

    public static AssetDescriptor<TextureAtlas> UI_ATLAS =
            new AssetDescriptor<>(AssetPaths.MENU_ATLAS, TextureAtlas.class);

    public static AssetDescriptor<TextureAtlas> GAMEPLAY_ATLAS =
            new AssetDescriptor<>(AssetPaths.GAMEPLAY_ATLAS, TextureAtlas.class);

    public static AssetDescriptor<BitmapFont> TITLE_FONT =
            new AssetDescriptor<>(AssetPaths.TITLE_FONT, BitmapFont.class);

    public static AssetDescriptor<BitmapFont> FONT =
            new AssetDescriptor<>(AssetPaths.TEXT_FONT, BitmapFont.class);

    public static final AssetDescriptor<Sound> MISS_SOUND =
            new AssetDescriptor<Sound>(AssetPaths.SWING_SOUND, Sound.class);

    public static final AssetDescriptor<Sound> SWORD_SOUND =
            new AssetDescriptor<Sound>(AssetPaths.SWORD_SOUND, Sound.class);

    public static final AssetDescriptor<Sound> MACE_SOUND =
            new AssetDescriptor<Sound>(AssetPaths.MACE_SOUND, Sound.class);

    public static final AssetDescriptor<Sound> DRINK_SOUND =
            new AssetDescriptor<Sound>(AssetPaths.DRINK_SOUND, Sound.class);

    public static final AssetDescriptor<Sound> BLOCKED_SOUND =
            new AssetDescriptor<Sound>(AssetPaths.BLOCKED_SOUND, Sound.class);

    public AssetDescriptors() {}
}
