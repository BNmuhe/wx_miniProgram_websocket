package com.ConnectionToDB;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.websocket.Message;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class GetLastMessageFromDB {



    public static HashMap<Integer,String> getLastMessages(int user_id) throws SQLException {
        //获取所有chat(to_id是user_id)
        ArrayList<Chat> chats=ChatUtils.getChats(user_id);
        //根据所有chat来获取对应的最新消息

        HashMap<Integer, String> lastMessages=new HashMap<Integer, String>();

        for(Chat chat : chats){
            lastMessages.put(chat.getFrom_id(),getLastMessage(chat.getFrom_id(),chat.getTo_id()));
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


