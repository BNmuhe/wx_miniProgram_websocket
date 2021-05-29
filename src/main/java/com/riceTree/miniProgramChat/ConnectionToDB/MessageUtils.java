package com.riceTree.miniProgramChat.ConnectionToDB;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.riceTree.miniProgramChat.websocket.Message;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class MessageUtils {


    private static ArrayList<DBMessage> DBHistoryMessages=new ArrayList<DBMessage>();


    public static boolean getFromDB(int from_id,int to_id) throws SQLException {

        DruidPooledConnection connection=ConnectionToDB.getConn();
        QueryRunner runner=new QueryRunner();
        String sql="select from_id, to_id, message, date, time"
                +" from history_message where (from_id = ? and to_id = ?) or (to_id = ? and from_id = ?) order by date,time";

        BeanListHandler<DBMessage> handler=new BeanListHandler<>(DBMessage.class);

        DBHistoryMessages= (ArrayList<DBMessage>) runner.query(connection,sql,handler,from_id,to_id,from_id,to_id);

        connection.close();

        return true;
    }

    public static Vector<Message> getHistoryMessages() {
        Vector<Message> historyMessages=new Vector<Message>();

        for(DBMessage dbMessage :DBHistoryMessages){
            historyMessages.add(dbMessage.DBMessageToMessage());
        }

        return historyMessages;
    }



    public static boolean sendToDB(DBMessage message) throws SQLException {
        DruidPooledConnection connection=ConnectionToDB.getConn();
        QueryRunner runner=new QueryRunner();
        String sql="insert into history_message(from_id,to_id,message,date,time) value(?,?,?,?,?)";
        runner.update(connection,sql,message.getFrom_id(),message.getTo_id(),message.getMessage(),message.getDate(),message.getTime());

        connection.close();
        return true;
    }






    public static HashMap<String,String> getLastMessages(int user_id) throws SQLException {
        //获取所有chat(to_id是user_id)
        ArrayList<Chat> chats=ChatUtils.getChats(user_id);
        //根据所有chat来获取对应的最新消息

        HashMap<String, String> lastMessages=new HashMap<String, String>();

        for(Chat chat : chats){
            lastMessages.put(chat.getFrom_id()+"",getLastMessage(chat.getFrom_id(),chat.getTo_id()));
        }


        return lastMessages;
    }


    public static String getLastMessage(int from_id,int to_id) throws SQLException {
        DruidPooledConnection connection=ConnectionToDB.getConn();
        QueryRunner runner=new QueryRunner();
        String sql="select from_id, to_id, message, date, time"
                +" from history_message where (from_id = ? and to_id = ?) or (to_id = ? and from_id = ?) order by id DESC LIMIT 1";

        BeanListHandler<DBMessage> handler=new BeanListHandler<>(DBMessage.class);

        //


        ArrayList<DBMessage> DBHistoryMessages= (ArrayList<DBMessage>) runner.query(connection,sql,handler,from_id,to_id,from_id,to_id);

        connection.close();
        return  DBHistoryMessages.get(0).getMessage();
    }



}
