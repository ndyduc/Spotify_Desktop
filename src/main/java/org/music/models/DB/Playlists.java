package org.music.models.DB;

public class Playlists {
    private String id;
    private String name;
    private String owner;
    private String description;
    private Boolean is_shuffle;
    private String Image;
    private String status;
    private String created_at;
    private Boolean is_pin;
    private Boolean is_dl;

    public Playlists(String id, String name, String owner, String description, Boolean is_shuffle, String image, String status, String created_at, Boolean is_pin, Boolean is_dl) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.is_shuffle = is_shuffle;
        Image = image;
        this.status = status;
        this.created_at = created_at;
        this.is_pin = is_pin;
        this.is_dl = is_dl;
    }

    public Boolean getIs_dl() {
        return is_dl;
    }

    public void setIs_dl(Boolean is_dl) {
        this.is_dl = is_dl;
    }

    public Boolean getIs_pin() {
        return is_pin;
    }

    public void setIs_pin(Boolean is_pin) {
        this.is_pin = is_pin;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public Boolean getIs_shuffle() {
        return is_shuffle;
    }

    public void setIs_shuffle(Boolean is_shuffle) {
        this.is_shuffle = is_shuffle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
