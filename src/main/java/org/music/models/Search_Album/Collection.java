package org.music.models.Search_Album;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;

public class Collection{
    public Object artwork_url;
    public Date created_at;
    public String description;
    public int duration;
    public String embeddable_by;
    public String genre;
    public int id;
    public String kind;
    public Object label_name;
    public Date last_modified;
    public String license;
    public int likes_count;
    public boolean managed_by_feeds;
    public String permalink;
    public String permalink_url;
    @JsonProperty("public")
    public boolean mypublic;
    public Object purchase_title;
    public Object purchase_url;
    public Object release_date;
    public int reposts_count;
    public Object secret_token;
    public String sharing;
    public String tag_list;
    public String title;
    public String uri;
    public int user_id;
    public String set_type;
    public boolean is_album;
    public Date published_at;
    public Date display_date;
    public User user;
    public ArrayList<Track> tracks;
    public int track_count;

    public Object getArtwork_url() {
        return artwork_url;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public String getDescription() {
        return description;
    }

    public int getDuration() {
        return duration;
    }

    public String getEmbeddable_by() {
        return embeddable_by;
    }

    public String getGenre() {
        return genre;
    }

    public int getId() {
        return id;
    }

    public String getKind() {
        return kind;
    }

    public Object getLabel_name() {
        return label_name;
    }

    public Date getLast_modified() {
        return last_modified;
    }

    public String getLicense() {
        return license;
    }

    public int getLikes_count() {
        return likes_count;
    }

    public boolean isManaged_by_feeds() {
        return managed_by_feeds;
    }

    public String getPermalink() {
        return permalink;
    }

    public String getPermalink_url() {
        return permalink_url;
    }

    public boolean isMypublic() {
        return mypublic;
    }

    public Object getPurchase_title() {
        return purchase_title;
    }

    public Object getPurchase_url() {
        return purchase_url;
    }

    public Object getRelease_date() {
        return release_date;
    }

    public int getReposts_count() {
        return reposts_count;
    }

    public Object getSecret_token() {
        return secret_token;
    }

    public String getSharing() {
        return sharing;
    }

    public String getTag_list() {
        return tag_list;
    }

    public String getTitle() {
        return title;
    }

    public String getUri() {
        return uri;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getSet_type() {
        return set_type;
    }

    public boolean isIs_album() {
        return is_album;
    }

    public Date getPublished_at() {
        return published_at;
    }

    public Date getDisplay_date() {
        return display_date;
    }

    public User getUser() {
        return user;
    }

    public ArrayList<Track> getTracks() {
        return tracks;
    }

    public int getTrack_count() {
        return track_count;
    }
}
