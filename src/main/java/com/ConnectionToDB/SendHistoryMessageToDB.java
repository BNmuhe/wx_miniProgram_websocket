package com.ConnectionToDB;

import com.alibaba.druid.pool.DruidPooledConnection;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.SQLException;

public class SendHistoryMessageToDB {

    public static boolean sendToDB(DBMessage message) throws SQLException {
        DruidPooledConnection connection=ConnectionToDB.getConn();
        QueryRunner runner=new QueryRunner();
        String sql="insert into history_message(from_id,to_id,message,date,time) value(?,?,?,?,?)";
        runner.update(connection,sql,message.getFrom_id(),message.getTo_id(),message.getMessage(),message.getDate(),message.getTime());

        connection.close();
        return true;
    }
}
