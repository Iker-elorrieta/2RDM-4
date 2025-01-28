package com.example.androidreto2grupo4;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerConection {
    private static ServerConection instance;
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private static final String SERVER_IP = "192.168.56.1";  // Change to your server's IP address.
    private static final int SERVER_PORT = 2000;

    private ServerConection() throws IOException {
        socket = new Socket(SERVER_IP, SERVER_PORT);

        // Initialize Object Streams
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());

        // Initialize Data Streams
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataInputStream = new DataInputStream(socket.getInputStream());
    }

    public static synchronized ServerConection getInstance() throws IOException {
        if (instance == null) {
            instance = new ServerConection();
        }
        return instance;
    }

    // Methods for Object Streams
    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    public ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }

    // Methods for Data Streams
    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    // Close the streams and socket
    public void close() throws IOException {
        if (objectInputStream != null) objectInputStream.close();
        if (objectOutputStream != null) objectOutputStream.close();
        if (dataInputStream != null) dataInputStream.close();
        if (dataOutputStream != null) dataOutputStream.close();
        if (socket != null) socket.close();
        instance = null;
    }
}
