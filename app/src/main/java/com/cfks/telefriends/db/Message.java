package com.cfks.telefriends.db;

public class Message {
    public @interface MessageType {
        int SENT = 0;
        int RECEIVED = 1;
        int DRAFT = 2;
        int EMPTY = 3;
    }
}