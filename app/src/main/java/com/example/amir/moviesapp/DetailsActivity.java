package com.example.amir.moviesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.amir.moviesapp.adapter.TrailerAdapter;
import com.example.amir.moviesapp.api.Client;
import com.example.amir.moviesapp.api.Service;
import com.example.amir.moviesapp.model.Trailer;
import com.example.amir.moviesapp.model.TrailerRespones;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {
    TextView nameOfMovie , plotSynopsis,userRating,releaseDate;
    ImageView imageView;
    private RecyclerView recyclerView;
    private List<Trailer> trailerList;
    private TrailerAdapter adapter;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initCollapsingToolbar();

        imageView= findViewById(R.id.thumbnail_image_header);
        nameOfMovie=findViewById(R.id.title);
        plotSynopsis=findViewById(R.id.plotsynopsis);
        userRating=findViewById(R.id.userrating);
        releaseDate=findViewById(R.id.releasedate);

        Intent intentthatstart=getIntent();
        if (intentthatstart.hasExtra("original_title")){

            String thumbnail=getIntent().getExtras().getString("poster_path");
            String movieName=getIntent().getExtras().getString("original_title");
            String synopsis=getIntent().getExtras().getString("overview");
            String rating=getIntent().getExtras().getString("vote_average");
            String dateOfRelease=getIntent().getExtras().getString("release_date");

            Glide.with(this)
                    .load(thumbnail).placeholder(R.drawable.load)
                    .into(imageView);

            nameOfMovie.setText(movieName);
            plotSynopsis.setText(synopsis);
            userRating.setText(rating);
            releaseDate.setText(dateOfRelease);
        }else {
            Toast.makeText(this, "No API Data", Toast.LENGTH_SHORT).show();
        }
        MaterialFavoriteButton materialFavoriteButton=
                (MaterialFavoriteButton) findViewById(R.id.favorite_btn);
        final SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        materialFavoriteButton.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
            @Override
            public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                 if (favorite) {
                     SharedPreferences.Editor editor = getSharedPreferences("com.example.amir.moviesapp.DetialsActivity", MODE_PRIVATE).edit();
                     editor.putBoolean("Favorite Added", true);
                     editor.commit();
                     saveFavorite();
                     Snackbar.make(buttonView, "Added to Favorite", Snackbar.LENGTH_SHORT).show();
                 }else {
                     SharedPreferences.Editor editor=getSharedPreferences("com.example.amir.moviesapp.DetialsActivity",MODE_PRIVATE).edit();
                     editor.putBoolean("Favorite Removed",true);
                     editor.commit();
                     Snackbar.make(buttonView,"Removed from Fav",Snackbar.LENGTH_SHORT).show();}
                 }
        });
        intViews();
    }
    private void initCollapsingToolbar(){
        final CollapsingToolbarLayout collapsingToolbarLayout= findViewById(R.id.collapsing_Toolbar);
        collapsingToolbarLayout.setTitle("");
        AppBarLayout appBarLayout=findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow=false;
            int srcollRange=-1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (srcollRange==-1){
                    srcollRange=appBarLayout.getTotalScrollRange();
                }
                if (srcollRange+verticalOffset==0){
                    collapsingToolbarLayout.setTitle(getString(R.string.movie_details));
                    isShow=true;
                }else if(isShow){
                    collapsingToolbarLayout.setTitle("");
                    isShow=false;
                }
            }
        });
    }

    private void intViews(){
        trailerList=new ArrayList<>();
        adapter=new TrailerAdapter(this,trailerList);

        recyclerView=findViewById(R.id.recycler_View1);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        loadJSON();
    }

    private void loadJSON() {
        int movie_id=getIntent().getExtras().getInt("id");

    try {

        if (BuildConfig.THE_MOVIE_DB_API_TOKEN.isEmpty()){
            Toast.makeText(getApplicationContext(),"Please obtian API KEY",Toast.LENGTH_SHORT).show();
            return;
        }
        Client client=new Client();
        Service apiService=client.getClient().create(Service.class);
        Call<TrailerRespones> call=apiService.getMovieTrailer(movie_id,BuildConfig.THE_MOVIE_DB_API_TOKEN);
        call.enqueue(new Callback<TrailerRespones>() {
            @Override
            public void onResponse(Call<TrailerRespones> call, Response<TrailerRespones> response) {
                List<Trailer> trailer=response.body().getResults();
                recyclerView.setAdapter(new TrailerAdapter(getApplicationContext(),trailer));
                recyclerView.smoothScrollToPosition(0); }
            @Override
            public void onFailure(Call<TrailerRespones> call, Throwable t) {
                Log.d("Error", "onFailure: "+t.getMessage());
                Toast.makeText(DetailsActivity.this,"Error fetching Trailer",Toast.LENGTH_SHORT).show();
            }
        });
    }
    catch (Exception e){
        Log.d("Error", e.getMessage());
        Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
    }
    }


}