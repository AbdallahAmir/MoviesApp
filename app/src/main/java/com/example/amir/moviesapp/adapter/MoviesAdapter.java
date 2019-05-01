package com.example.amir.moviesapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.amir.moviesapp.DetailsActivity;
import com.example.amir.moviesapp.R;
import com.example.amir.moviesapp.model.Movie;

import java.util.List;
import java.util.ResourceBundle;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.myViewHolder> {

    private Context mContext;
    private List<Movie>movieList;

    public MoviesAdapter(Context mContext,List<Movie>movieList){
        this.mContext=mContext;
        this.movieList=movieList;
    }

    @NonNull
    @Override
    public MoviesAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_card,parent,false);
        return new myViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MoviesAdapter.myViewHolder viewHolder, int position) {
        viewHolder.title.setText(movieList.get(position).getOriginalTitle());
        String vote=Double.toString(movieList.get(position).getVoteAverage());
        viewHolder.userrating.setText(vote);

        Glide.with(mContext).load(movieList.get(position).getPosterPath())
                .placeholder(R.drawable.load).into(viewHolder.thumbnail); //load image from drawable(no internet)


    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        public TextView title,userrating;
        public ImageView thumbnail;
        public myViewHolder(View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            userrating=itemView.findViewById(R.id.userrating);
            thumbnail=itemView.findViewById(R.id.thumbnail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=getAdapterPosition();
                    if (pos!=RecyclerView.NO_POSITION){
                        Movie clickedData=movieList.get(pos);
                        Intent intent=new Intent(mContext, DetailsActivity.class);
                        intent.putExtra("original_title",movieList.get(pos).getOriginalTitle());
                        intent.putExtra("poster_path",movieList.get(pos).getPosterPath());
                        intent.putExtra("overview",movieList.get(pos).getOverview());
                        intent.putExtra("vote_average",Double.toString(movieList.get(pos).getVoteAverage()));
                        intent.putExtra("release_date",movieList.get(pos).getReleaseDate());
                        intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        Toast.makeText(v.getContext(),"you click"+clickedData.getOriginalTitle(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
