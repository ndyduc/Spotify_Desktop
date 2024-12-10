package org.music.models.Search_YT;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;

public class SearchYT {
    public String etag;
    public ArrayList<Item> items;
    public String kind;
    public String nextPageToken;
    public PageInfo pageInfo;
    public String regionCode;
}


