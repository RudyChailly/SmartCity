package com.example.smartcity.notifications;

public class Sender {

    private Data data;
    public String to;

    public Sender() {}

    public Sender(Data data, String to) {
        this.data = data;
        this.to = to;
    }

}
