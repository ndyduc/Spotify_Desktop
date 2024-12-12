package org.music.models.DB;

public class Love_Artists {
    private String id;
    private int artist_id;
    private String Owner;
    private String artist_name;
    private String artist_img;

    public Love_Artists( String id, String owner, int artist_id, String artist_name, String artist_img) {
        this.artist_id = artist_id;
        this.artist_img = artist_img;
        this.Owner = owner;
        this.artist_name = artist_name;
        this.id = id;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }

    public String getArtist_img() {
        return artist_img;
    }

    public void setArtist_img(String artist_img) {
        this.artist_img = artist_img;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public int getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(int artist_id) {
        this.artist_id = artist_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
