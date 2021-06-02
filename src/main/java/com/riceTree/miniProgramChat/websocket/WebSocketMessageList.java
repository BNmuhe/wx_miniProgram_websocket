package com.riceTree.miniProgramChat.websocket;

import com.riceTree.miniProgramChat.ConnectionToDB.ChatUtils;
import com.riceTree.miniProgramChat.ConnectionToDB.MessageUtils;
import com.alibaba.fastjson.JSON;
import com.riceTree.miniProgramChat.Log.Logger;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.sql.SQLException;
import java.util.HashMap;

@ServerEndpoint("/websocketMessageList/{userID}")
public class WebSocketMessageList {

    private int userID;
    private Session session;



    /**
     * 连接建立成功调用方法
     * @param userID
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("userID") String userID, Session session) throws SQLException {
        this.session=session;
        this.userID=Integer.parseInt(userID);
        Logger.SystemLog(this.getClass(),userID+"进入消息列表");
        WebSocketUtils.getWebsocketSingleLinks().put(this.userID,this);
        sendMessageListInfo(this.userID);
    }



    /**
     * 连接关闭调用方法
     */
    @OnClose
    public void onClose(){
        WebSocketUtils.getWebsocketSingleLinks().remove(this.userID);
        Logger.SystemLog(this.getClass(),userID+"离开消息列表");
    }




    /**
     * 接收到消息后调用方法
     * @Param message
     * @Param session
     */
    @OnMessage
    public void onMessage(String message, Session session) throws SQLException {

        Logger.SystemLog(this.getClass(),"接收消息,来自 "+userID+":"+message);
        if(message.equals("firstMessage received")){//获取未读消息数目，并发送
            HashMap<String,Integer> unreadNumber=ChatUtils.getUnreadNumber(userID);
            String str1=JSON.toJSONString(unreadNumber);
            WebSocketUtils.getWebsocketSingleLinks().get(userID).session.getAsyncRemote().sendText(str1);
        }else if(message.equals("unreadNum received")){//获取物品id并发送
            HashMap<String,Integer> thingId=ChatUtils.getThingID(userID);
            String str2=JSON.toJSONString(thingId);
            this.session.getAsyncRemote().sendText(str2);
        }else if(message.indexOf("delete")==0) {
            String[] strings=message.split(" ");
            ChatUtils.deleteChat(Integer.parseInt(strings[1]),Integer.parseInt(strings[2]),Integer.parseInt(strings[3]));
        }
        else{
            Logger.SystemLog(this.getClass(),"为正常发送消息至 "+userID);
        }

    }



    /**
     * 发生错误时调用
     * @param error
     */
    @OnError
    public void onError(Throwable error){
        Logger.errorLog(this.getClass(),"发生错误");
        error.printStackTrace();
    }


    public static void  sendMessageListInfo(int userID) throws SQLException {
        //获取所有历史消息中的最新消息，并发送

        HashMap<String,String> lastMessages= MessageUtils.getLastMessages(userID);

        String str=JSON.toJSONString(lastMessages);

        WebSocketUtils.getWebsocketSingleLinks().get(userID).session.getAsyncRemote().sendText(str);

    }

}
