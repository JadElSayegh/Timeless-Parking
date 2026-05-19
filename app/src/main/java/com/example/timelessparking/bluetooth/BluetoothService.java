package com.example.timelessparking.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class BluetoothService {
    public interface BluetoothListener {
        void onDataReceived(String data);
        void onConnectionStatus(String status);
    }

    private static BluetoothService instance;
    private BluetoothSocket socket;
    private InputStream inputStream;
    private boolean listening;
    private BluetoothListener listener;

    private final UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothService() {}

    public static BluetoothService getInstance() {
        if (instance == null) instance = new BluetoothService();
        return instance;
    }

    public void setListener(BluetoothListener listener) {
        this.listener = listener;
    }

    public void connect(BluetoothDevice device) {
        new Thread(() -> {
            try {
                socket = device.createRfcommSocketToServiceRecord(SERIAL_UUID);
                socket.connect();
                inputStream = socket.getInputStream();
                startListening();
                if (listener != null) listener.onConnectionStatus("Connected to " + device.getName());
            } catch (IOException e) {
                e.printStackTrace();
                if (listener != null) listener.onConnectionStatus("Connection failed");
            }
        }).start();
    }

    private void startListening() {
        listening = true;
        new Thread(() -> {
            byte[] buffer = new byte[1024];
            StringBuilder stringBuilder = new StringBuilder();
            int bytes;
            try {
                while (listening && (bytes = inputStream.read(buffer)) != -1) {
                    String chunk = new String(buffer, 0, bytes);
                    stringBuilder.append(chunk);
                    int endOfLineIndex;
                    while ((endOfLineIndex = stringBuilder.indexOf("\n")) != -1) {
                        String line = stringBuilder.substring(0, endOfLineIndex).trim();
                        stringBuilder.delete(0, endOfLineIndex + 1);
                        if (listener != null) listener.onDataReceived(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (listener != null) listener.onConnectionStatus("Disconnected");
            }
        }).start();
    }

    public void disconnect() {
        listening = false;
        try {
            if (inputStream != null) inputStream.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
