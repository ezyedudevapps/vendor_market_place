package com.ezyedu.vendor.model;

public class Chat_New
{
    int sender_id,receiver_id;
    String content;

    public Chat_New(int sender_id, int receiver_id, String content) {
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.content = content;
    }

    public int getSender_id() {
        return sender_id;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public String getContent() {
        return content;
    }
}
