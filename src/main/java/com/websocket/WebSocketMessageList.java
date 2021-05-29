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
        System.out.println(this.getClass().toString()+" log_info:"+userID+"接入");
        WebSocketUtils.getWebsocketSingleLinks().put(this.userID,this);

        //检测是否存在链接
//        if(WebSocketUtils.getWebsocketSingleLinks().getOrDefault(this.userID,null)!=null){
//
//        }
//        //获取所有历史消息中的最新消息，并发送
//
//        HashMap<Integer,String> lastMessages= MessageUtils.getLastMessages(this.userID);
//
//        String str=JSON.toJSONString(lastMessages);
//
//        this.session.getAsyncRemote().sendText(str);
//
//        //获取未读消息数目，并发送
//
//        HashMap<Integer,Integer> unreadNumber=ChatUtils.getUnreadNumber(this.userID);
//
//        String str1=JSON.toJSONString(unreadNumber);
//
//        this.session.getAsyncRemote().sendText(str1);

        sendMessageListInfo(this.userID);
//开启一个线程，用于监听数据库是否接收到新消息
    }



    /**
     * 连接关闭调用方法
     */
    @OnClose
    public void onClose(){
        WebSocketUtils.getWebsocketSingleLinks().remove(this.userID);
        System.out.println(this.getClass().toString()+" log_info:"+this.userID+"离开界面");
    }




    /**
     * 接收到消息后调用方法
     * @Param message
     * @Param session
     */
    @OnMessage
    public void onMessage(String message, Session session) throws SQLException {
        System.out.println(this.getClass().toString()+" log_info:"+"接受内容:" + message);

        //获取未读消息数目，并发送

        if(message.equals("firstMessage received")){
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
            System.out.println(this.getClass().toString()+" log_info:"+"未正常发送最新消息");
        }



    }



    /**
     * 发生错误时调用
     * @param error
     */
    @OnError
    public void onError(Throwable error){
        System.out.println(this.getClass().toString()+" log_info:"+"发生错误");
        error.printStackTrace();
    }


    public static void  sendMessageListInfo(int userID) throws SQLException {
        //获取所有历史消息中的最新消息，并发送

        HashMap<String,String> lastMessages= MessageUtils.getLastMessages(userID);

        String str=JSON.toJSONString(lastMessages);

        WebSocketUtils.getWebsocketSingleLinks().get(userID).session.getAsyncRemote().sendText(str);





    }


}
