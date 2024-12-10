package org.music.models.DB;

public class Queue_Tracks {
    private String  id;
    private String owner;
    private String is_shuffle;
    private String is_loop;

    public String  getId() {
        return id;
    }
    public void setId(String  id) {
        this.id = id;
    }
    public String getIs_loop() {
        return is_loop;
    }
    public void setIs_loop(String is_loop) {
        this.is_loop = is_loop;
    }
    public String getIs_shuffle() {
        return is_shuffle;
    }
    public void setIs_shuffle(String is_shuffle) {
        this.is_shuffle = is_shuffle;
    }
    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
}
