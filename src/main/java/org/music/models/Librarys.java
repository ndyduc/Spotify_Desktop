package org.music.models;

public class Librarys {
    int id;
    String name;
    String owner;
    String description;
    String cover;
    boolean download;
    boolean pin;
    boolean play;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public String getDescription() {
        return description;
    }

    public String getCover() {
        return cover;
    }

    public boolean isDownload() {
        return download;
    }

    public boolean isPin() {
        return pin;
    }

    public boolean isPlay() {
        return play;
    }
}
