package com.ConnectionToDB;

import com.alibaba.druid.pool.DruidPooledConnection;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatUtils {

    private static ArrayList<Chat> chats=new ArrayList<Chat>();
    public static ArrayList<Chat> getChats(int user_id) throws SQLException {


        DruidPooledConnection connection=ConnectionToDB.getConn();
        QueryRunner runner=new QueryRunner();

        String sql="select from_id, to_id, unread_number"
                +" from chat where to_id = ?";

        BeanListHandler<Chat> handler=new BeanListHandler<>(Chat.class);

        chats= (ArrayList<Chat>) runner.query(connection,sql,handler,user_id);

        connection.close();
        return chats;
    }

    public static void addNewChat(int from_id,int to_id) throws SQLException {
        DruidPooledConnection connection=ConnectionToDB.getConn();
        QueryRunner runner=new QueryRunner();
        String sql="insert chat(from_id,to_id,unread_number) value(?,?,?)";
        runner.update(connection,sql,from_id,to_id,0);
        connection.close();

    }

    public static void deleteChat(int from_id,int to_id) throws SQLException {
        DruidPooledConnection connection=ConnectionToDB.getConn();
        QueryRunner runner=new QueryRunner();
        String sql="delete from chat where from_id=? and to_id=?";
        runner.update(connection,sql,from_id,to_id);
        connection.close();
    }

    public static void clearUnreadNumber(int from_id,int to_id) throws SQLException {
        DruidPooledConnection connection=ConnectionToDB.getConn();
        QueryRunner runner=new QueryRunner();
        String sql="update chat set unread_number = 0 where from_id=? and to_id=?";
        runner.update(connection,sql,from_id,to_id);
        connection.close();
    }


    public static void addUnreadNumber(int from_id,int to_id) throws SQLException {
        ArrayList<Chat> chats=null;

        DruidPooledConnection connection=ConnectionToDB.getConn();
        QueryRunner runner=new QueryRunner();



        int cnt;




        String sql="select from_id, to_id, unread_number"
                +" from chat where from_id = ? and to_id = ?";

        BeanListHandler<Chat> handler=new BeanListHandler<>(Chat.class);

        chats= (ArrayList<Chat>) runner.query(connection,sql,handler,from_id,to_id);


        cnt= chats.get(0).getUnread_number();




        cnt++;
        String sql1="update chat set unread_number = ? where from_id=? and to_id=?";
        runner.update(connection,sql1,cnt,from_id,to_id);


        connection.close();
    }

    public static HashMap<Integer,Integer> getUnreadNumber(int user_id) throws SQLException {
        ArrayList<Chat> chats=null;
        chats=getChats(user_id);

        HashMap<Integer,Integer> unreadNumber=new HashMap<Integer, Integer>();


        for(Chat chat : chats){
            unreadNumber.put(chat.getFrom_id(),chat.getUnread_number());
        }

        return unreadNumber;
    }



}


