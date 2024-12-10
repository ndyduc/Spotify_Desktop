package org.music.models;

import org.music.models.Search_Album.Collection;

import java.util.ArrayList;

public class Albums{
    public ArrayList<Collection> collection;
    public int total_results;
    public String next_href;
    public String query_urn;

    public ArrayList<Collection> getCollection() {
        return collection;
    }

    public int getTotal_results() {
        return total_results;
    }

    public String getNext_href() {
        return next_href;
    }

    public String getQuery_urn() {
        return query_urn;
    }
}
