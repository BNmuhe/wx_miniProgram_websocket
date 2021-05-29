package com.ConnectionToDB;

public class Chat {
    private int from_id;
    private int to_id;
    private int unread_number;
    private int thing_id;

    public int getTo_id() {
        return to_id;
    }

    public int getFrom_id() {
        return from_id;
    }

    public int getUnread_number() {
        return unread_number;
    }

    public int getThing_id() {
        return thing_id;
    }

    public void setThing_id(int thing_id) {
        this.thing_id = thing_id;
    }

    public void setTo_id(int to_id) {
        this.to_id = to_id;
    }

    public void setFrom_id(int from_id) {
        this.from_id = from_id;
    }

    public void setUnread_number(int unread_number) {
        this.unread_number = unread_number;
    }
}
