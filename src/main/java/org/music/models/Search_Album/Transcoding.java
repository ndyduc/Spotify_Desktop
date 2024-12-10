package org.music.models.Search_Album;

public class Transcoding{
    public String url;
    public String preset;
    public int duration;
    public boolean snipped;
    public Format format;
    public String quality;

    public String getUrl() {
        return url;
    }

    public String getPreset() {
        return preset;
    }

    public int getDuration() {
        return duration;
    }

    public boolean isSnipped() {
        return snipped;
    }

    public Format getFormat() {
        return format;
    }

    public String getQuality() {
        return quality;
    }
}
