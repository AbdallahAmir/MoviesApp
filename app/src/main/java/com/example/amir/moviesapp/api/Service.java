package com.example.amir.moviesapp.api;

import com.example.amir.moviesapp.model.MoviesRespones;
import com.example.amir.moviesapp.model.TrailerRespones;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {

    @GET("movie/popular")
    Call<MoviesRespones> getPopularMovies(@Query("api_key")String apiKey);
    @GET("movie/top_rated")
    Call<MoviesRespones> getTopRatedMovies(@Query("api_key")String apiKey);
    @GET("movie/{movie_id}/videos")
    Call<TrailerRespones> getMovieTrailer(@Path("movie_id") int id, @Query("api_key") String apiKey);

}
