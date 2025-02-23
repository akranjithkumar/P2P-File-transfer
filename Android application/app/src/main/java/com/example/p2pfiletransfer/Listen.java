package com.example.p2pfiletransfer;

import okhttp3.*;
import okio.ByteString;

public class Listen {
    private OkHttpClient client;
    private WebSocket webSocket;

    public Listen() {
        client = new OkHttpClient();
        Request request = new Request.Builder().url("https://192.168.1.100:5000").build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                System.out.println("WebSocket Connected");
                webSocket.send("{\"event\": \"get_update\"}");  // Request updates
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                System.out.println("Received Message: " + text);
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                System.out.println("Received Bytes: " + bytes.hex());
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                webSocket.close(1000, null);
                System.out.println("WebSocket Closing: " + reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                System.err.println("WebSocket Error: " + t.getMessage());
            }
        });
    }
}