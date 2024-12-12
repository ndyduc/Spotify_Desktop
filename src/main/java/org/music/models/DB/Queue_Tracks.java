package org.music.models.DB;

public class Queue_Tracks {
    private String  id;
    private String owner;
    private String Title;
    private String Artist;
    private String Filename;

    public Queue_Tracks(String id, String owner, String title, String artist, String filename) {
        this.id = id;
        this.owner = owner;
        Title = title;
        Artist = artist;
        Filename = filename;
    }

    public String getArtist() {
        return Artist;
    }

    public void setArtist(String Artist) {
        this.Artist = Artist;
    }

    public String getFilename() {
        return Filename;
    }

    public void setFilename(String Filename) {
        this.Filename = Filename;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String  getId() {
        return id;
    }
    public void setId(String  id) {
        this.id = id;
    }
    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
}
