package com.example.tvapp.presenters


import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.leanback.widget.Presenter
import com.example.tvapp.R
import com.example.tvapp.models.MovieEpisode

class SeriaListPresenter: Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_episode, parent, false)

        val params = view.layoutParams

        params.width = getWidthInPercent(parent!!.context, 20)
        params.height = getHeightInPercent(parent.context, 12)



        return ViewHolder(view)
    }


    fun getWidthInPercent(context: Context, percent: Int): Int {
        val width = context.resources.displayMetrics.widthPixels ?: 0
        return (width * percent) / 100
    }

    fun getHeightInPercent(context: Context, percent: Int): Int {
        val width = context.resources.displayMetrics.heightPixels ?: 0
        return (width * percent) / 100
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, item: Any?) {
        val content = item as? MovieEpisode

        val txtEpisode_title =  viewHolder?.view?.findViewById<TextView>(R.id.episode_title)
        val txtEpisode_quality   = viewHolder?.view?.findViewById<TextView>(R.id.episode_quality)
        val txtEpisode_duration = viewHolder?.view?.findViewById<TextView>(R.id.episode_duration)

        txtEpisode_title?.text =  "Серия: ${content?.episodeTitle}"
        txtEpisode_quality?.text   = "Качество: ${content?.quality}"
        txtEpisode_duration?.text = "Длительность: ${content?.duration}"

        viewHolder?.view?.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) {
            viewHolder.view?.setBackgroundColor(Color.DKGRAY)
        } else {
            viewHolder.view?.setBackgroundColor(Color.parseColor("#0F0F0F"))
        }
        }


    }


    override fun onUnbindViewHolder(viewHolder: ViewHolder?) {}
}