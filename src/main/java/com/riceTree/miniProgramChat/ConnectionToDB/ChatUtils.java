package com.riceTree.miniProgramChat.ConnectionToDB;

import com.alibaba.druid.pool.DruidPooledConnection;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatUtils {

    private static ArrayList<Chat> chats=new ArrayList<Chat>();
    public static ArrayList<Chat> getChats(int user_id) throws SQLException {

        DruidPooledConnection connection=ConnectionToDB.getConn();
        QueryRunner runner=new QueryRunner();


        String sql="select from_id, to_id, unread_number,thing_id"
                +" from chat where to_id = ?";

        BeanListHandler<Chat> handler=new BeanListHandler<>(Chat.class);

        chats= (ArrayList<Chat>) runner.query(connection,sql,handler,user_id);

        connection.close();
        return chats;
    }

    public static void addNewChat(int from_id,int to_id,int thing_id) throws SQLException {
        DruidPooledConnection connection=ConnectionToDB.getConn();
        QueryRunner runner=new QueryRunner();
        String sql="insert chat(from_id,to_id,unread_number,thing_id) value(?,?,?,?)";
        runner.update(connection,sql,from_id,to_id,0,thing_id);
        connection.close();

    }

    public static void deleteChat(int from_id,int to_id, int thing_id) throws SQLException {
        DruidPooledConnection connection=ConnectionToDB.getConn();
        QueryRunner runner=new QueryRunner();
        String sql="delete from chat where from_id=? and to_id=? and thing_id=?";
        runner.update(connection,sql,from_id,to_id,thing_id);
        connection.close();
    }

    public static void clearUnreadNumber(int from_id,int to_id) throws SQLException {
        DruidPooledConnection connection=ConnectionToDB.getConn();
        QueryRunner runner=new QueryRunner();
        String sql="update chat set unread_number = 0 where from_id=? and to_id=?";
        runner.update(connection,sql,from_id,to_id);
        connection.close();
    }

    public static Chat getChat(int from_id, int to_id) throws SQLException {
        Chat chat=null;
        DruidPooledConnection connection=ConnectionToDB.getConn();
        QueryRunner runner=new QueryRunner();
        String sql="select from_id, to_id, unread_number, thing_id"
                +" from chat where from_id = ? and to_id = ?";
        BeanHandler handler=new BeanHandler<>(Chat.class);
        chat=(Chat) runner.query(connection,sql,handler,from_id,to_id);
        return chat;

    }

    public static void addUnreadNumber(int from_id,int to_id) throws SQLException {
        Chat chats=null;

        DruidPooledConnection connection=ConnectionToDB.getConn();
        QueryRunner runner=new QueryRunner();



        int cnt;




        String sql="select from_id, to_id, unread_number"
                +" from chat where from_id = ? and to_id = ?";

        BeanHandler<Chat> handler=new BeanHandler<>(Chat.class);

        chats=runner.query(connection,sql,handler,from_id,to_id);


        cnt= chats.getUnread_number();




        cnt++;
        String sql1="update chat set unread_number = ? where from_id=? and to_id=?";
        runner.update(connection,sql1,cnt,from_id,to_id);


        connection.close();
    }

    public static HashMap<String,Integer> getUnreadNumber(int user_id) throws SQLException {
        ArrayList<Chat> chats=null;
        chats=getChats(user_id);

        HashMap<String,Integer> unreadNumber=new HashMap<String, Integer>();


        for(Chat chat : chats){
            unreadNumber.put(chat.getFrom_id()+"",chat.getUnread_number());
        }

        return unreadNumber;
    }

    public static HashMap<String,Integer> getThingID(int user_id) throws SQLException {
        ArrayList<Chat> chats=null;
        chats=getChats(user_id);
        HashMap<String,Integer> thingID=new HashMap<String, Integer>();

        for(Chat chat : chats){
            thingID.put(chat.getFrom_id()+"",chat.getThing_id());
        }

        return thingID;
    }

}


