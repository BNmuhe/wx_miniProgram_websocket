package com.riceTree.miniProgramChat.ConnectionToDB;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;

import java.sql.SQLException;

public class ConnectionToDB {
    private static final DruidDataSource source = new DruidDataSource();
    static {
        source.setDriverClassName("com.mysql.jdbc.Driver");
        source.setUrl("jdbc:mysql://www.crackme.cloud:3306/miniprogram?useSSL=false");//192.168.3.24
        source.setUsername("root");
        source.setPassword("Wx301301301B.");
        source.setMaxActive(1000);
    }

    public static DruidPooledConnection getConn() throws SQLException {
        return source.getConnection();
    }
}
