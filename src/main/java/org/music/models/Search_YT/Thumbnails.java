package org.music.models.Search_YT;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Thumbnails{
    @JsonProperty("default")
    public Default mydefault;
    public High high;
    public Medium medium;
}
