package org.music.models.Search_Album;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Track{
    public String artwork_url;
    public Object caption;
    public boolean commentable;
    public int comment_count;
    public Date created_at;
    public String description;
    public boolean downloadable;
    public int download_count;
    public int duration;
    public int full_duration;
    public String embeddable_by;
    public String genre;
    public boolean has_downloads_left;
    public int id;
    public String kind;
    public String label_name;
    public Date last_modified;
    public String license;
    public int likes_count;
    public String permalink;
    public String permalink_url;
    public int playback_count;
    @JsonProperty("public")
    public boolean mypublic;
    public PublisherMetadata publisher_metadata;
    public Object purchase_title;
    public Object purchase_url;
    public Date release_date;
    public int reposts_count;
    public Object secret_token;
    public String sharing;
    public String state;
    public boolean streamable;
    public String tag_list;
    public String title;
    public String uri;
    public String urn;
    public int user_id;
    public Object visuals;
    public String waveform_url;
    public Date display_date;
    public Media media;
    public String station_urn;
    public String station_permalink;
    public String track_authorization;
    public String monetization_model;
    public String policy;
    public User user;

    public String getArtwork_url() {
        return artwork_url;
    }

    public Object getCaption() {
        return caption;
    }

    public boolean isCommentable() {
        return commentable;
    }

    public int getComment_count() {
        return comment_count;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDownloadable() {
        return downloadable;
    }

    public int getDownload_count() {
        return download_count;
    }

    public int getDuration() {
        return duration;
    }

    public int getFull_duration() {
        return full_duration;
    }

    public String getEmbeddable_by() {
        return embeddable_by;
    }

    public String getGenre() {
        return genre;
    }

    public boolean isHas_downloads_left() {
        return has_downloads_left;
    }

    public int getId() {
        return id;
    }

    public String getKind() {
        return kind;
    }

    public String getLabel_name() {
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

    public String getPermalink() {
        return permalink;
    }

    public String getPermalink_url() {
        return permalink_url;
    }

    public int getPlayback_count() {
        return playback_count;
    }

    public boolean isMypublic() {
        return mypublic;
    }

    public PublisherMetadata getPublisher_metadata() {
        return publisher_metadata;
    }

    public Object getPurchase_title() {
        return purchase_title;
    }

    public Object getPurchase_url() {
        return purchase_url;
    }

    public Date getRelease_date() {
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

    public String getState() {
        return state;
    }

    public boolean isStreamable() {
        return streamable;
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

    public String getUrn() {
        return urn;
    }

    public int getUser_id() {
        return user_id;
    }

    public Object getVisuals() {
        return visuals;
    }

    public String getWaveform_url() {
        return waveform_url;
    }

    public Date getDisplay_date() {
        return display_date;
    }

    public Media getMedia() {
        return media;
    }

    public String getStation_urn() {
        return station_urn;
    }

    public String getStation_permalink() {
        return station_permalink;
    }

    public String getTrack_authorization() {
        return track_authorization;
    }

    public String getMonetization_model() {
        return monetization_model;
    }

    public String getPolicy() {
        return policy;
    }

    public User getUser() {
        return user;
    }
}
