package org.music.models.Search_Album;

import java.util.ArrayList;
import java.util.Date;

public class User{
    public String avatar_url;
    public Object city;
    public int comments_count;
    public Object country_code;
    public Date created_at;
    public ArrayList<CreatorSubscription> creator_subscriptions;
    public CreatorSubscription creator_subscription;
    public Object description;
    public int followers_count;
    public int followings_count;
    public String first_name;
    public String full_name;
    public int groups_count;
    public int id;
    public String kind;
    public Date last_modified;
    public String last_name;
    public int likes_count;
    public int playlist_likes_count;
    public String permalink;
    public String permalink_url;
    public int playlist_count;
    public Object reposts_count;
    public int track_count;
    public String uri;
    public String urn;
    public String username;
    public boolean verified;
    public Object visuals;
    public Badges badges;
    public String station_urn;
    public String station_permalink;

    public String getAvatar_url() {
        return avatar_url;
    }

    public Object getCity() {
        return city;
    }

    public int getComments_count() {
        return comments_count;
    }

    public Object getCountry_code() {
        return country_code;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public ArrayList<CreatorSubscription> getCreator_subscriptions() {
        return creator_subscriptions;
    }

    public CreatorSubscription getCreator_subscription() {
        return creator_subscription;
    }

    public Object getDescription() {
        return description;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public int getFollowings_count() {
        return followings_count;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getFull_name() {
        return full_name;
    }

    public int getGroups_count() {
        return groups_count;
    }

    public int getId() {
        return id;
    }

    public String getKind() {
        return kind;
    }

    public Date getLast_modified() {
        return last_modified;
    }

    public String getLast_name() {
        return last_name;
    }

    public int getLikes_count() {
        return likes_count;
    }

    public int getPlaylist_likes_count() {
        return playlist_likes_count;
    }

    public String getPermalink() {
        return permalink;
    }

    public String getPermalink_url() {
        return permalink_url;
    }

    public int getPlaylist_count() {
        return playlist_count;
    }

    public Object getReposts_count() {
        return reposts_count;
    }

    public int getTrack_count() {
        return track_count;
    }

    public String getUri() {
        return uri;
    }

    public String getUrn() {
        return urn;
    }

    public String getUsername() {
        return username;
    }

    public boolean isVerified() {
        return verified;
    }

    public Object getVisuals() {
        return visuals;
    }

    public Badges getBadges() {
        return badges;
    }

    public String getStation_urn() {
        return station_urn;
    }

    public String getStation_permalink() {
        return station_permalink;
    }
}
