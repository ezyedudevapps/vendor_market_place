package com.ezyedu.vendor.model;

public class GetChat
{
    Integer sender_Id;
    String message;

    public GetChat(Integer sender_Id, String message) {
        this.sender_Id = sender_Id;
        this.message = message;
    }

    public Integer getSender_Id() {
        return sender_Id;
    }

    public String getMessage() {
        return message;
    }
}
