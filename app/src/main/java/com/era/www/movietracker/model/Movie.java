package com.era.www.movietracker.model;

/**
 * Created by Mohamed on 5/1/2017.
 */

public class Movie {

    private int revenue;
    private String title;
    private int year;
    private int rank;
    private int movieTraktId;

    private String formattedNumber;

    public Movie(int revenue, String title, int year, int rank, int movieTraktId) {
        this.revenue = revenue;
        this.title = title;
        this.year = year;
        this.rank = rank;
        this.movieTraktId = movieTraktId;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getMovieTraktId() {
        return movieTraktId;
    }

    public void setMovieTraktId(int movieTraktId) {
        this.movieTraktId = movieTraktId;
    }

    public String getFormattedNumber() {
        return formattedNumber;
    }

    public void setFormattedNumber(String formattedNumber) {
        this.formattedNumber = formattedNumber;
    }
}
