package org.music.models.Search_Album;

public class PublisherMetadata{
    public int id;
    public String urn;
    public boolean contains_music;
    public String artist;
    public String album_title;
    public String upc_or_ean;
    public String isrc;
    public boolean explicit;
    public String p_line;
    public String p_line_for_display;
    public String c_line;
    public String c_line_for_display;
    public String writer_composer;
    public String release_title;

    public int getId() {
        return id;
    }

    public String getUrn() {
        return urn;
    }

    public boolean isContains_music() {
        return contains_music;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum_title() {
        return album_title;
    }

    public String getUpc_or_ean() {
        return upc_or_ean;
    }

    public String getIsrc() {
        return isrc;
    }

    public boolean isExplicit() {
        return explicit;
    }

    public String getP_line() {
        return p_line;
    }

    public String getP_line_for_display() {
        return p_line_for_display;
    }

    public String getC_line() {
        return c_line;
    }

    public String getC_line_for_display() {
        return c_line_for_display;
    }

    public String getWriter_composer() {
        return writer_composer;
    }

    public String getRelease_title() {
        return release_title;
    }
}
