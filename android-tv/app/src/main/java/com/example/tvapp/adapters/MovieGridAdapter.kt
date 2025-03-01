package com.example.tvapp.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.tvapp.DetailsActivity
import com.example.tvapp.R
import com.example.tvapp.models.Movie


class MovieGridAdapter(private val movies: MutableList<Movie>, private val itemHeight: Int) :
    RecyclerView.Adapter<MovieGridAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie_grid, parent, false)

        val params = view.layoutParams
        params.height =itemHeight

        view.layoutParams = params

        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailsActivity::class.java)
            intent.putExtra("movie", movie)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = movies.size

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val movieTitle: TextView = itemView.findViewById(R.id.movie_title)
        private val moviePoster: ImageView = itemView.findViewById(R.id.poster_image)

        fun bind(movie: Movie) {
            movieTitle.text = movie.title
            Glide.with(itemView.context)
                .load(movie.posterUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false)
                .into(moviePoster)

            itemView.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    movieTitle.isSelected = true
                    itemView.animate()
                        .scaleX(1.05f)
                        .scaleY(1.05f)
                        .setDuration(200)
                        .start()
                } else {
                    movieTitle.isSelected = false
                    itemView.animate()
                        .scaleX(1.0f)
                        .scaleY(1.0f)
                        .setDuration(200)
                        .start()
                }
            }
        }
    }

    fun updateMovies(newMovies: MutableList<Movie>) {
        movies.clear()
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }

    fun clearMovies() {
        movies.clear()
        notifyDataSetChanged()
    }

}