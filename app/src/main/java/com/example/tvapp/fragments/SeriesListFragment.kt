package com.example.tvapp.fragments

import android.os.Bundle
 import android.view.View

import androidx.leanback.app.RowsSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.FocusHighlight
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.leanback.widget.OnItemViewSelectedListener
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.Row
import androidx.leanback.widget.RowPresenter
import com.example.tvapp.models.MoviesResponse
import com.example.tvapp.models.SeriesResponse
 import com.example.tvapp.presenters.SeriaPresenter
 import android.content.Intent
import android.util.Log
import androidx.lifecycle.lifecycleScope
 import com.example.tvapp.MyApplication
 import com.example.tvapp.VideoPlayerActivity
 import com.example.tvapp.api.Repository
 import kotlinx.coroutines.launch

class SeriesListFragment : RowsSupportFragment() {
    private var itemSelectedListener: ((MoviesResponse.Result.Detail) -> Unit)? = null


    private val listRowPresenter = object : ListRowPresenter(FocusHighlight.ZOOM_FACTOR_SMALL) {
        override fun isUsingDefaultListSelectEffect(): Boolean {
            return true
        }
    }.apply {
        shadowEnabled = false
    }

    private val rootAdapter: ArrayObjectAdapter = ArrayObjectAdapter(listRowPresenter)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = rootAdapter

        onItemViewSelectedListener = ItemViewSelectedListener()
        onItemViewClickedListener = ItemViewClickListener()


    }

    fun bindSeriesData(seasons: SeriesResponse) {

        seasons.seasons.forEachIndexed { index, season ->
            val arrayObjectAdapter = ArrayObjectAdapter(SeriaPresenter())

            season.episodes.forEach {
                arrayObjectAdapter.add(it)
            }
            val headerItem = HeaderItem("Сезон: ${season.season_id}")
            val listRow = ListRow(headerItem, arrayObjectAdapter)

            rootAdapter.add(listRow)
        }
    }


    private fun openVideo(episodes: List<SeriesResponse.Episodes>, currentIndex: Int) {
        val episodeUrls = episodes.map { it.episode_source_id }.toCollection(ArrayList())
        val detailsFragment = parentFragment as? DetailsFragment
        val identificator = detailsFragment?.getIdentification() ?: ""
        val movie_id = detailsFragment?.getMovieId() ?: ""

        val intent = Intent(context, VideoPlayerActivity::class.java).apply {
            putStringArrayListExtra("EPISODE_SOURCE_LIST", episodeUrls)
            putExtra("CURRENT_EPISODE", currentIndex - 1)
            putExtra("MOVIE_ID", movie_id)
            putExtra("IDENTIFICATOR", identificator)
        }

        startActivity(intent)
    }

    fun getSeasonsFromRootAdapter(season_id: Int): List<SeriesResponse.Episodes>? {
        val listRow = (0 until rootAdapter.size()).map { rootAdapter.get(it) as ListRow }
            .find { row ->
                val seasonHeader = row.headerItem.name
                val currentSeasonId = seasonHeader.replace("Сезон: ", "").toInt()
                currentSeasonId == season_id
            }

        return listRow?.let { row ->
            val arrayObjectAdapter = row.adapter as ArrayObjectAdapter
            (0 until arrayObjectAdapter.size()).map { arrayObjectAdapter.get(it) as SeriesResponse.Episodes }
        }
    }


    inner class ItemViewSelectedListener : OnItemViewSelectedListener {
        override fun onItemSelected(
            itemViewHolder: Presenter.ViewHolder?,
            item: Any?,
            rowViewHolder: RowPresenter.ViewHolder?,
            row: Row?
        ) {
            if (item is MoviesResponse.Result.Detail) {
                itemSelectedListener?.invoke(item)
                }
            }

        }

    inner class ItemViewClickListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder?,
            item: Any?,
            rowViewHolder: RowPresenter.ViewHolder?,
            row: Row
        ) {
            if (item is SeriesResponse.Episodes) {
                lifecycleScope.launch {
                    val seasonHeader = row.headerItem.name
                    val currentSeasonId = seasonHeader.replace("Сезон: ", "").toInt()

                    val episodes = getSeasonsFromRootAdapter(currentSeasonId)
                    if (episodes != null) {
                         openVideo(episodes, item.episode_id.toInt())
                    }
                }
            }
        }
    }
}