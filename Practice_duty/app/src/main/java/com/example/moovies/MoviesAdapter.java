package com.example.moovies;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {


    private List<Movie> movies = new ArrayList<>();

    private OnReachEndListener onReachEndListener;


    public void setOnReachEndListener(OnReachEndListener onReachEndListener) {
        this.onReachEndListener = onReachEndListener;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public MoviesAdapter() {
        super();
    }


    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.movie_item,
                parent,
                false
        );
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Log.d("OnBindViewHolder", String.valueOf(position));
        Movie movie = movies.get(position);
        Glide.with(holder.itemView)
                .load(movie.getPoster()
                        .getUrl())
                .into(holder.imageViewPoster);

        double rating = movie.getRating().getRating1();
        int backgroundId;
        if (rating > 7) {
            backgroundId = R.drawable.circle_green;
        } else if (rating > 5) {
            backgroundId = R.drawable.circle_orange;
        } else {
            backgroundId = R.drawable.circle_red;
        }

        Drawable backround = ContextCompat.getDrawable(holder.itemView.getContext(), backgroundId);
        holder.textViewRating.setBackground(backround);

        holder.textViewRating.setText(String.valueOf(rating));


        if (position >= movies.size() - 10 && onReachEndListener != null) {
            onReachEndListener.OnReachEnd();
        }

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageViewPoster;
        private final TextView textViewRating;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPoster = itemView.findViewById(R.id.imageViewPoster);
            textViewRating = itemView.findViewById(R.id.textViewRating);
        }
    }

    interface OnReachEndListener {
        void OnReachEnd();
    }


}
