package com.ConnectionToDB;

import com.websocket.Message;

public class UnreadMessage {
    int cnt;
    Message message;


    public void setMessage(Message message) {
        this.message = message;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }


}
