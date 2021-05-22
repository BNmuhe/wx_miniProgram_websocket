package com.websocket;

import java.util.concurrent.ConcurrentHashMap;

public class WebSocketUtils {
    private static final ConcurrentHashMap<Integer,WebSocketServer> websocketClients=new ConcurrentHashMap<Integer, WebSocketServer>();
    private static final ConcurrentHashMap<Integer, WebSocketMessageList> websocketSingleLinks=new ConcurrentHashMap<Integer, WebSocketMessageList>();


    public static void sendMessageByUser(int userID,String message){


        websocketClients.get(userID).getSession().getAsyncRemote().sendText(message);



    }

    public static ConcurrentHashMap<Integer, WebSocketServer> getWebsocketClients() {
        return websocketClients;
    }

    public static ConcurrentHashMap<Integer, WebSocketMessageList> getWebsocketSingleLinks() {
        return websocketSingleLinks;
    }
}
