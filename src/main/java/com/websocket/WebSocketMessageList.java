package com.websocket;

import com.ConnectionToDB.ChatUtils;
import com.ConnectionToDB.MessageUtils;
import com.alibaba.fastjson.JSON;

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
        System.out.println(userID+"接入");
        WebSocketUtils.getWebsocketSingleLinks().put(this.userID,this);

        //获取所有历史消息中的最新消息，并发送

        HashMap<Integer,String> lastMessages= MessageUtils.getLastMessages(this.userID);

        String str=JSON.toJSONString(lastMessages);

        this.session.getAsyncRemote().sendText(str);

        //获取未读消息数目，并发送

        HashMap<Integer,Integer> unreadNumber=ChatUtils.getUnreadNumber(this.userID);

        String str1=JSON.toJSONString(unreadNumber);

        this.session.getAsyncRemote().sendText(str1);


    }



    /**
     * 连接关闭调用方法
     */
    @OnClose
    public void onClose(){
        WebSocketUtils.getWebsocketSingleLinks().remove(this.userID);
        System.out.println(this.userID+"离开界面");
    }




    /**
     * 接收到消息后调用方法
     * @Param message
     * @Param session
     */
    @OnMessage
    public void onMessage(String message, Session session) throws SQLException {
        System.out.println("接受内容:" + message);
        Couple couple=Couple.parseObject(message);
    //将未读消息数目清零
        ChatUtils.clearUnreadNumber(couple.from_id,couple.to_id);
    }



    /**
     * 发生错误时调用
     * @param error
     */
    @OnError
    public void onError(Throwable error){
        System.out.println("发生错误");
        error.printStackTrace();
    }



}
