package com.ConnectionToDB;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.websocket.Message;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

public class GetHistoryMessageFromDB {
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






}
