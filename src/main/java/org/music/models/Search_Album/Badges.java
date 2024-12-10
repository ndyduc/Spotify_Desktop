package org.music.models.Search_Album;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;

public class Badges{
    public boolean pro;
    public boolean creator_mid_tier;
    public boolean pro_unlimited;
    public boolean verified;

    public boolean isPro() {
        return pro;
    }

    public boolean isCreator_mid_tier() {
        return creator_mid_tier;
    }

    public boolean isPro_unlimited() {
        return pro_unlimited;
    }

    public boolean isVerified() {
        return verified;
    }
}

