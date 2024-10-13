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
import com.example.tvapp.models.MoviesResponse


class MoviePresenter : Presenter() {
    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_movie, parent, false)

        val params = view.layoutParams
        params.width = getWidthInPercent(parent!!.context, 13)

        view.layoutParams = params

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, item: Any?) {
        val content = item as? MoviesResponse.Result.Detail ?: return

        val movie_title: TextView = viewHolder?.view?.findViewById(R.id.movie_title) ?: return
        val movie_cover: ImageView = viewHolder.view.findViewById(R.id.poster_image)

        movie_title.text = content.title
        movie_title.visibility = if (content.search_title != null) View.VISIBLE else View.GONE

        val params = viewHolder.view.layoutParams

        params.height = if (content.search_title != null) {
            getHeightInPercent(viewHolder.view.context, 45)
        } else {
            getHeightInPercent(viewHolder.view.context, 32)
        }

        viewHolder.view.layoutParams = params

        Glide.with(viewHolder.view.context)
            .load(content.poster_url)
            .into(movie_cover)
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