package com.example.tvapp.fragments

 import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper

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
 import com.example.tvapp.DetailsActivity
import com.example.tvapp.models.Movie
 import com.example.tvapp.models.MoviesList
 import com.example.tvapp.presenters.MovieListPresenter



class MoviesListFragment : RowsSupportFragment() {
    private var runnable: Runnable? = null
    private var handler: Handler? = null
    private var itemSelectedListener: ((Movie) -> Unit)? = null

    private val listRowPresenter = object : ListRowPresenter(FocusHighlight.ZOOM_FACTOR_SMALL) {
        override fun isUsingDefaultListSelectEffect(): Boolean {
            return false
        }
    }.apply {
        shadowEnabled = true
    }

    private val rootAdapter: ArrayObjectAdapter = ArrayObjectAdapter(listRowPresenter)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = rootAdapter

        onItemViewSelectedListener = ItemViewSelectedListener()
        onItemViewClickedListener = ItemViewClickListener()

        handler = Handler(Looper.getMainLooper());
    }

    fun bindMoviesData(movies: List<MoviesList>) {
        if (movies.isNotEmpty()) {
            rootAdapter.clear()
            createListRow(movies)
            setSelectedPosition(0, true)
        }
    }

    private fun createListRow(movies: List<MoviesList>) {
        movies.forEach() { row ->
            val arrayObjectAdapter = ArrayObjectAdapter(MovieListPresenter())

            row.movies.forEach {
                arrayObjectAdapter.add(it)
            }

            val headerItem = HeaderItem(row.title)
            val listRow = ListRow(headerItem, arrayObjectAdapter)
            rootAdapter.add(listRow)
        }
    }

    fun setOnContentSelectedListener(listener: (Movie) -> Unit) {
        this.itemSelectedListener = listener
    }

    inner class ItemViewSelectedListener : OnItemViewSelectedListener {
        override fun onItemSelected(
            itemViewHolder: Presenter.ViewHolder?,
            item: Any?,
            rowViewHolder: RowPresenter.ViewHolder?,
            row: Row
        ) {
           item?.let {
               val movie = it as Movie

               runnable?.let {
                   handler?.removeCallbacks(it)
               }

               runnable = Runnable {
                   itemSelectedListener?.invoke(movie)
               }

               handler?.postDelayed(runnable!!, 1000)
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
            item?.let {
                val movie = it as Movie

                movie.details?.let {
                    val intent = Intent(context, DetailsActivity::class.java)
                    intent.putExtra("movie", movie)
                    context?.startActivity(intent)
                }
            }
        }
    }
}