package com.mygdx.game.util.net;


import com.badlogic.gdx.utils.async.AsyncTask;

import java.io.DataInputStream;
import java.io.PrintWriter;
import java.net.Socket;

public interface ConnectionManager {

    void sendMessage(String... voids);



}
