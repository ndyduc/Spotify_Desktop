package org.music.models;

public class Queue_Item {
    private String mongoID;
    private String ImgCover;
    private String title;
    private String artist;
    private String album;
    private String fileName;
    private String link;
    private String where;
    private int artist_id;
    private int duration;
    private String genre;

    public Queue_Item(){}

    public Queue_Item(String imgCover, String title, String artist, String album, String fileName, String link, String mongoID, String where, int artist_id, int duration, String genre) {
        ImgCover = imgCover;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.fileName = fileName;
        this.link = link;
        this.mongoID = mongoID;
        this.where = where;
        this.artist_id = artist_id;
        this.duration = duration;
        this.genre = genre;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(int artist_id) {
        this.artist_id = artist_id;
    }

    public String getMongoID() {
        return mongoID;
    }

    public void setMongoID(String mongoID) {
        this.mongoID = mongoID;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getImgCover() {
        return ImgCover;
    }

    public void setImgCover(String imgCover) {
        ImgCover = imgCover;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//
//    public Queue_Item(String title, String artist, String fileName) {
//        this.title = title;
//        this.artist = artist;
//        this.fileName = fileName;
//    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getFileName() {
        return fileName;
    }

    // Hiển thị thông tin bài hát
    @Override
    public String toString() {
        return "Bài hát: " + title + " | Ca sĩ: " + artist + " | Tên file: " + fileName;
    }
}