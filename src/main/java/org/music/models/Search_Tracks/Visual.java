package org.music.models.Search_Tracks;

import java.util.ArrayList;

public class Visual {
        public String urn;
        public int entry_time;
        public String visual_url;
        public String link;
        public boolean enabled;
        public ArrayList<Visual> visuals;
        public Object tracking;

        public String getVisual_url() {
                return visual_url;
        }

        public String getUrn() {
                return urn;
        }

        public int getEntry_time() {
                return entry_time;
        }

        public String getLink() {
                return link;
        }

        public boolean isEnabled() {
                return enabled;
        }

        public ArrayList<Visual> getVisuals() {
                return visuals;
        }

        public Object getTracking() {
                return tracking;
        }
}
