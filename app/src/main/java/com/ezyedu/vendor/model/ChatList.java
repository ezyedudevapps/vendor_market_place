package com.ezyedu.vendor.model;

public class ChatList
{
    String Name;
    String message;
    String image;
    String Time;
    Integer Unread_Count;
    Integer receiver_id;
    Integer Sender_id;

    public ChatList(String name, String message, String image, String time, Integer unread_Count, Integer receiver_id, Integer sender_id) {
        Name = name;
        this.message = message;
        this.image = image;
        Time = time;
        Unread_Count = unread_Count;
        this.receiver_id = receiver_id;
        Sender_id = sender_id;
    }

    public String getName() {
        return Name;
    }

    public String getMessage() {
        return message;
    }

    public String getImage() {
        return image;
    }

    public String getTime() {
        return Time;
    }

    public Integer getUnread_Count() {
        return Unread_Count;
    }

    public Integer getReceiver_id() {
        return receiver_id;
    }

    public Integer getSender_id() {
        return Sender_id;
    }
}
