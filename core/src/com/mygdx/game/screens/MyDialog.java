package com.mygdx.game.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class MyDialog extends Dialog {
    public enum DialogSelected {
        back, retry, yes, no, cancel
    }

    public MyDialog(String title, Skin skin) {
        super(title, skin);
    }
    public MyDialog(String title, Skin skin, String windowStyleName) {
        super(title, skin, windowStyleName);
    }
    public MyDialog(String title, WindowStyle windowStyle) {
        super(title, windowStyle);
    }

    public Cell addButton(String text, Object object, TextButton.TextButtonStyle buttonStyle) {
        TextButton tmp = new TextButton(text, buttonStyle);
        setObject(tmp, object);
        return getButtonTable().add(tmp); //Return table cell
    }
}
