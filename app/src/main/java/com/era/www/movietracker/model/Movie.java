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
    private String overview;
    private String released;
    private String trailer;
    private String homePage;
    private int rate;
    private String certification;

    private String formattedNumber;

    public Movie(int revenue, String title, int year, int rank, int movieTraktId, String overview, String released, String trailer, String homePage, int rate, String certification) {
        this.revenue = revenue;
        this.title = title;
        this.year = year;
        this.rank = rank;
        this.movieTraktId = movieTraktId;
        this.overview = overview;
        this.released = released;
        this.trailer = trailer;
        this.homePage = homePage;
        this.rate = rate;
        this.certification = certification;
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

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }
}
