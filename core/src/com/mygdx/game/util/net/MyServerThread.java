

package com.mygdx.game.util.net;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.util.GameManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServerThread implements Runnable {
    Socket socket;
    ServerSocket serverSocket;
    InputStreamReader inputStreamReader;
    BufferedReader bufferedReader;
    String message;

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(7801);
            while (true) {
                socket = serverSocket.accept();
                inputStreamReader = new InputStreamReader(socket.getInputStream());
                bufferedReader = new BufferedReader(inputStreamReader);
                message = bufferedReader.readLine();

                Gdx.app.log("HOLY SHIT", "The message is: " + message);
                GameManager.loadSettings(message);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
