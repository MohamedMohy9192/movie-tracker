package com.era.www.movietracker.model;

/**
 * Created by Mohamed on 5/1/2017.
 */

public class BoxOfficeMovie {

    private String name;

    private int revenue;

    private byte rank;

    public BoxOfficeMovie(String name, int revenue, byte rank) {
        this.name = name;
        this.revenue = revenue;
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    public byte getRank() {
        return rank;
    }

    public void setRank(byte rank) {
        this.rank = rank;
    }
}
