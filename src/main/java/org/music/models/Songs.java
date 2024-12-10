package org.music.models;

import org.music.models.Search_Tracks.Collection;

import java.util.ArrayList;

public class Songs {
    private ArrayList<Collection> collection;
    private String next_href;
    private int total_results;
    private String query_urn;

    public void setQuery_urn(String query_urn) {
        this.query_urn = query_urn;
    }


    public ArrayList<Collection> getCollection() {
        return collection;
    }
    public String getQuery_urn() {
        return query_urn;
    }
    public String getNextHref() {
        return next_href;
    }
    public int getTotalResults() {
        return total_results;
    }

}