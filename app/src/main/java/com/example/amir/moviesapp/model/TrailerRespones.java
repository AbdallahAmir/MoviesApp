package com.example.amir.moviesapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailerRespones {

    @SerializedName("id")
    private int id_Trailer;
    @SerializedName("results")
    private List<Trailer> results;

    public int getId_Trailer() {
        return id_Trailer;
    }

    public void setId_Trailer(int id_Trailer) {
        this.id_Trailer = id_Trailer;
    }

    public List<Trailer> getResults() {
        return results;
    }
}
