package com.websocket;

import com.alibaba.fastjson.JSON;

public class Couple {
    int from_id;
    int to_id;

    public int getFrom_id() {
        return from_id;
    }

    public int getTo_id() {
        return to_id;
    }




    public static Couple parseObject(String jsonString){
        return JSON.parseObject(jsonString,Couple.class);
    }

}
