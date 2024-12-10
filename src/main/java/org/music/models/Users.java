package org.music.models;

import org.music.models.Search_User.Collection;

import java.util.ArrayList;

public class Users{
    public ArrayList<Collection> collection;
    public int total_results;
    public String next_href;
    public String query_urn;

    public ArrayList<Collection> getCollection() {
        return collection;
    }

    public String getNext_href() {
        return next_href;
    }

    public String getQuery_urn() {
        return query_urn;
    }

    public int getTotal_results() {
        return total_results;
    }
}

