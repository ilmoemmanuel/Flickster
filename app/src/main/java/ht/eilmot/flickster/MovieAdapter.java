package ht.eilmot.flickster;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import ht.eilmot.flickster.models.Config;
import ht.eilmot.flickster.models.Movie;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{
    // List of movies
    ArrayList<Movie> movies;
    // config needed for image urls
    Config config;
    // context for renderring
    Context context;

    // initialize with list
    public MovieAdapter(ArrayList<Movie> movies){
        this.movies = movies;

    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    // creates and inflates a new view
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // get the context and create the inflater
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // create the view using the item_movie layout
        View movieView = inflater.inflate(R.layout.item_movie, viewGroup, false);
        // return a new viewHolder
        return  new ViewHolder(movieView);
    }


    // binds and inflated view to a new item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get the movie data at the specified position
        Movie movie = movies.get(position);
        // populate the view with the movie data
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());

        // determine the current orientation
        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        // build url for poster image
        String imageUrl = null;
        // if in portrait mode, load the poster image
        if(isPortrait){
            imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
        } else {
            // load the backdrop image
            imageUrl = config.getImageUrl(config.getBackdropSize(),movie.getBackdropPath());
        }
        // get the current placeholder and imageView for the current orientation
        int placeholderId = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
        ImageView imageView = isPortrait ? holder.ivPosterImage : holder.ivBackdropImage;

        // load image using picasso
       /* Picasso.get()
                .load(imageUrl)
                .bitmapTransform(new RoundedCornersTransformation(25,0))
                .placeholder(R.drawable.flicks_movie_placeholder)
                .error(R.drawable.flicks_movie_placeholder)
                .into(holder.ivPosterImage);*/
        //  load image using glide

        RequestOptions cropOptions = new RequestOptions().centerCrop();

        Glide.with(context)
                .load(imageUrl)
                .apply(new RequestOptions().transform(new RoundedCorners(20))
                .placeholder(placeholderId).error(placeholderId))
                .into(imageView);


    }

    // returns the total number of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    //create the viewHolder as a static inner class
    public static class ViewHolder extends RecyclerView.ViewHolder{
        // track view objects
        ImageView ivPosterImage;
        ImageView ivBackdropImage;
        TextView tvTitle;
        TextView tvOverview;
        public ViewHolder(View itemView){
            super(itemView);
            // lookup view objects by id
            ivPosterImage = itemView.findViewById(R.id.ivPosterImage);
            ivBackdropImage = (ImageView) itemView.findViewById(R.id.ivBackdropImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
        }


    }
}
