package org.music.models;

public class Track {
    private int id;
    private String title;
    private String artist;
    private String dsct_artist; // Description of the artist
    private String img_artist; // Artist's avatar URL
    private int fler_artist; // Follower count of the artist
    private int id_artist; // Artist's ID
    private String imgvcover; // Track's artwork URL
    private String genre;
    private String album;
    private Boolean isdl = false;
    private String File_name;


    public String getFile_name() {
        return File_name;
    }
    public void setFile_name(String file_name) {
        File_name = file_name;
    }
    public Boolean getIsdl() {
        return isdl;
    }
    public Boolean isIsdl() {
        return isdl;
    }
    public void setIsdl(Boolean isdl) {
        this.isdl = isdl;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getArtist() {
        return artist;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }
    public String getDsct_artist() {
        return dsct_artist;
    }
    public void setDsct_artist(String dsct_artist) {
        this.dsct_artist = dsct_artist;
    }
    public String getImg_artist() {
        return img_artist;
    }
    public void setImg_artist(String img_artist) {
        this.img_artist = img_artist;
    }
    public int getFler_artist() {
        return fler_artist;
    }
    public void setFler_artist(int fler_artist) {
        this.fler_artist = fler_artist;
    }
    public int getId_artist() {
        return id_artist;
    }
    public void setId_artist(int id_artist) {
        this.id_artist = id_artist;
    }
    public String getImgvcover() {
        return imgvcover;
    }
    public void setImgvcover(String imgvcover) {
        this.imgvcover = imgvcover;
    }
    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public String getAlbum() {
        return album;
    }
    public void setAlbum(String album) {
        this.album = album;
    }

}