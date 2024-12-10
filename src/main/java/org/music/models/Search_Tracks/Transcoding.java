package org.music.models.Search_Tracks;

public class Transcoding{
        public String url;
        public String preset;
        public int duration;
        public boolean snipped;
        public Format format;
        public String quality;

        public boolean isSnipped() {
                return snipped;
        }
        public String getUrl() {
                return url;
        }
        public String getPreset() {
                return preset;
        }
        public int getDuration() {
                return duration;
        }
        public Format getFormat() {
                return format;
        }
        public String getQuality() {
                return quality;
        }
    }
