package com.riceTree.miniProgramChat.websocket;

import com.riceTree.miniProgramChat.ConnectionToDB.*;
import com.alibaba.fastjson.JSON;
import com.riceTree.miniProgramChat.Log.Logger;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.sql.SQLException;
import java.util.Vector;


@ServerEndpoint("/websocket/{userID}")
public class WebSocketServer {
    private int userID;
    private Session session;
    private int targetID;
    private Chat chat=new Chat();

    /**
     * 连接建立成功调用方法
     * @param userID
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("userID") String userID, Session session) throws SQLException {
        this.session=session;
        this.userID=Integer.parseInt(userID);
        int targetID = Integer.parseInt(session.getQueryString());
        Vector<Message> historyMessages = null;
        this.targetID=targetID;

        chat.setFrom_id(this.userID);
        chat.setTo_id(targetID);


        Logger.SystemLog(this.getClass(),userID+" 建立聊天连接");


        //清除未读消息

        ChatUtils.clearUnreadNumber(this.userID,targetID);

        //从数据库获取历史消息
        try{
            MessageUtils.getFromDB(this.userID, targetID);
            historyMessages= MessageUtils.getHistoryMessages();
        }catch (Exception e){
            e.printStackTrace();
        }



        //将历史消息发送到连接方
        if((!historyMessages.isEmpty())&&historyMessages!=null){
            //将历史消息的vector转换为json数组
            String str= JSON.toJSONString(historyMessages);
            this.session.getAsyncRemote().sendText(str);

            MessageUtils.getHistoryMessages().removeAllElements();
        }



        WebSocketUtils.getWebsocketClients().put(this.userID,this);
        
    }

    /**
     * 连接关闭调用方法
     */
    @OnClose
    public void onClose(){
        WebSocketUtils.getWebsocketClients().remove(this.userID);
        Logger.SystemLog(this.getClass(),userID+" 断开聊天连接");
    }


    /**
     * 接收到消息后调用方法
     * @Param message
     * @Param session
     */
    @OnMessage
    public void onMessage(String message, Session session) throws SQLException {
        Logger.SystemLog(this.getClass(),"接收消息,来自 "+userID+":"+message);

        if(message.indexOf("thingId:")==0){
            String[] strings=message.split(":");
            chat.setThing_id(Integer.parseInt(strings[1]));
        }






        Message message1;
        try {
            message1 =Message.parseObject(message);
        }catch (Exception e){
            Logger.SystemLog(this.getClass(),"接收非聊天消息消息,来自: "+userID);
            return;
        }
        //发送消息确认为聊天消息后
        //添加chat记录

        if(ChatUtils.getChat(this.userID,targetID)==null){
            ChatUtils.addNewChat(this.userID,targetID,chat.getThing_id());
        }
        if(ChatUtils.getChat(targetID,this.userID)==null){
            ChatUtils.addNewChat(targetID,this.userID,chat.getThing_id());
        }
        //消息存入数据库
        DBMessage dbMessage=new DBMessage();
        dbMessage.MessageToDBMessage(message1);
        MessageUtils.sendToDB(dbMessage);
        //对方不在线时，对方未读消息记录加1

        if(WebSocketUtils.getWebsocketClients().getOrDefault(message1.getTo_id(),null)==null){
            //将未读消息记录增加
            ChatUtils.addUnreadNumber(message1.getFrom_id(),message1.getTo_id());

            //判断对方是否登陆程序
            if(WebSocketUtils.getWebsocketSingleLinks().getOrDefault(message1.getTo_id(),null)!=null){//在线
                WebSocketMessageList.sendMessageListInfo(message1.getTo_id());
            }
        }else {
            //消息转发,
            Logger.SystemLog(this.getClass(),"转发消息,来自 "+userID+":"+message);

            WebSocketUtils.sendMessageByUser(message1.getTo_id(),message);
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



    public Session getSession() {
        return session;
    }
}
