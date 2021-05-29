package com.riceTree.miniProgramChat.websocket;


import com.alibaba.fastjson.JSON;

public class Message {

    private int from_id;
    private int to_id;
    private String date;
    private String message;
    private String time;
    private int month;
    private int day;
    private int hour;
    private int minute;


    public void setTime(String time) {
        this.time = time;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setFrom_id(int from_id) {
        this.from_id = from_id;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    public int getDay() {
        return day;
    }

    public int getFrom_id() {
        return from_id;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getMonth() {
        return month;
    }

    public int getTo_id() {
        return to_id;
    }

    public String getDate() {
        return date;
    }

    public void setTo_id(int to_id) {
        this.to_id = to_id;
    }


    public static Message parseObject(String jsonString){
        return JSON.parseObject(jsonString,Message.class);
    }


}
