package com.mygdx.game;

import android.os.AsyncTask;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.util.GameManager;
import com.mygdx.game.util.net.ConnectionManager;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class AndroidConnectionManager extends AsyncTask<String, Void, Void>  implements ConnectionManager {
    Socket socket;
    DataInputStream dataInputStream;
    PrintWriter printWriter;

    @Override
    protected Void doInBackground(String... messages) {
        Gdx.app.log("Android: Connection manager", "You called this function");

        String req = messages[0];
        String data = messages[1];

        try
        {
            socket = new Socket("192.168.1.11", 7800);
            printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.write (req+"\n");
            printWriter.write(data);
            printWriter.flush();
            printWriter.close();
            socket.close();

        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            GameManager.connected = false;

        }
        return null;
    }

    @Override
    public void sendMessage(String... voids) {
        if (voids.length > 1)
            doInBackground(voids[0], voids[1]);
    }
}
