package com.easychat;

public class Paket {
    private String from;
    private String to;
    private String type;
    private String data;

    public Paket(String from, String to, String type, String data) {
        this.from = from;
        this.to = to;
        this.type = type;
        this.data = data;
    }

    public Paket() {
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Paket{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", type='" + type + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
