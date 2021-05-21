package com.ConnectionToDB;

import com.websocket.Message;

public class DBMessage {
    private int from_id;
    private int to_id;
    private String message;



    private String date;
    private String time;



    public int getFrom_id() {
        return from_id;
    }

    public int getTo_id() {
        return to_id;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }


    public void setFrom_id(int from_id) {
        this.from_id = from_id;
    }

    public void setTo_id(int to_id) {
        this.to_id = to_id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void MessageToDBMessage(Message message){
        setFrom_id(message.getFrom_id());
        setTo_id(message.getTo_id());
        setMessage(message.getMessage());
        setDate(message.getDate());
        setTime(message.getTime());
    }
    public Message DBMessageToMessage(){
        Message message=new Message();
        message.setTime(this.getTime());
        message.setDate(this.getDate());
        message.setMessage(this.getMessage());
        message.setFrom_id(this.getFrom_id());
        message.setTo_id(this.getTo_id());


        String[] monthAndDay=message.getDate().split("月");
        String[] Day=monthAndDay[1].split("日");

        message.setMonth(Integer.parseInt(monthAndDay[0]));
        message.setDay((Integer.parseInt(Day[0])));

        String[] hourAndMinute=message.getTime().split(":");

        message.setHour(Integer.parseInt(hourAndMinute[0]));
        message.setMinute(Integer.parseInt(hourAndMinute[1]));

        return message;
    }

}
