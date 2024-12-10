package com.example.tvapp.presenters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.leanback.widget.Presenter
import com.bumptech.glide.Glide
import com.example.tvapp.R
import com.example.tvapp.models.Movie

class MoviePresenter : Presenter() {
    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_movie, parent, false)

        val params = view.layoutParams
        params.width = getWidthInPercent(parent!!.context, 14)

        view.layoutParams = params

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, item: Any?) {
        val content = item as Movie

        val movieTitle: TextView = viewHolder?.view?.findViewById(R.id.movie_title) ?: return
        val movieCover: ImageView = viewHolder.view.findViewById(R.id.poster_image)

        movieCover.visibility = View.VISIBLE

        val params = viewHolder.view.layoutParams

        params.height = getHeightInPercent(viewHolder.view.context, 34)

        viewHolder.view.layoutParams = params

        Glide.with(viewHolder.view.context)
            .load(content.posterUrl)
            .into(movieCover)
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder?) {
        viewHolder?.view?.findViewById<TextView>(R.id.movie_title)?.text = ""
        viewHolder?.view?.findViewById<ImageView>(R.id.poster_image)?.setImageDrawable(null)
    }

    private fun getWidthInPercent(context: Context, percent: Int): Int {
        val width = context.resources.displayMetrics.widthPixels
        return (width * percent) / 100
    }

    private fun getHeightInPercent(context: Context, percent: Int): Int {
        val height = context.resources.displayMetrics.heightPixels
        return (height * percent) / 100
    }
}