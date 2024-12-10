package org.music.models.Search_Tracks;

public class PublisherMetadata{
        public int id;
        public String urn;
        public String artist;
        public String album_title;
        public boolean contains_music;
        public String isrc;
        public boolean explicit;
        public String release_title;

        public int getId() {
                return id;
        }
        public String getUrn() {
                return urn;
        }
        public String getArtist() {
                return artist;
        }
        public String getAlbum_title() {
                return album_title;
        }
        public boolean isContains_music() {
                return contains_music;
        }
        public String getIsrc() {
                return isrc;
        }
        public boolean isExplicit() {
                return explicit;
        }
        public String getRelease_title() {
                return release_title;
        }
    }
