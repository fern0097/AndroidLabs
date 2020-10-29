package com.example.androidlabs;

class Message {

    private long id;
    private String chatMessage;
    private boolean isReceived;

    public Message(String chatMessage, boolean isReceived) {
        this.chatMessage = chatMessage;
        this.isReceived = isReceived;
    }

    public Message(long id, String chatMessage, boolean isReceived) {
        this.id = id;
        this.chatMessage = chatMessage;
        this.isReceived = isReceived;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }

    public boolean isReceived() {
        return isReceived;
    }

    public void setReceived(boolean received) {
        isReceived = received;
    }
}