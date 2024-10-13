package com.example.tvapp.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log

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
import androidx.lifecycle.lifecycleScope
import com.example.tvapp.DetailsActivity
import com.example.tvapp.MyApplication
import com.example.tvapp.api.Repository
import com.example.tvapp.models.MoviesResponse
import com.example.tvapp.presenters.MoviePresenter
import kotlinx.coroutines.launch


class MoviesListFragment : RowsSupportFragment() {
    private var runnable: Runnable? = null
    private var handler: Handler? = null
    lateinit var identificator: String

    private lateinit var repository: Repository
    private var itemSelectedListener: ((MoviesResponse.Result.Detail) -> Unit)? = null

    private val listRowPresenter = object : ListRowPresenter(FocusHighlight.ZOOM_FACTOR_MEDIUM) {
        override fun isUsingDefaultListSelectEffect(): Boolean {
            return false
        }
    }.apply {
        shadowEnabled = true
    }

    private val rootAdapter: ArrayObjectAdapter = ArrayObjectAdapter(listRowPresenter)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as MyApplication).repository

        val homeFragment = parentFragment as? HomeFragment
        identificator = homeFragment?.getIdentification() ?: ""

        adapter = rootAdapter

        onItemViewSelectedListener = ItemViewSelectedListener()
        onItemViewClickedListener = ItemViewClickListener()

        handler = Handler(Looper.getMainLooper());
    }


    fun bindMoviesData(movies: MoviesResponse  ) {
        rootAdapter.clear()
        if (movies.result.size != 0) {
            createListRow(movies)
            setSelectedPosition(0, true)
        }
    }

    fun createListRow(movies: MoviesResponse) {
        movies.result.forEachIndexed { index, movie ->
            if (movie.details.size != 0) {
                val arrayObjectAdapter = ArrayObjectAdapter(MoviePresenter())

                movie.details.forEach {
                    arrayObjectAdapter.add(it)
                }

                if (movie.identificator.isNullOrEmpty()) {
                    val headerItem = HeaderItem(movie.title)
                    val listRow = ListRow(headerItem, arrayObjectAdapter)
                    rootAdapter.add(listRow)
                } else {
                    val headerItem = HeaderItem("${movie.title} (${movie.identificator})")
                    val listRow = ListRow(headerItem, arrayObjectAdapter)
                    rootAdapter.add(listRow)
                }
            }
        }
    }

    fun setOnContentSelectedListener(listener: (MoviesResponse.Result.Detail) -> Unit) {
        this.itemSelectedListener = listener
    }

    inner class ItemViewSelectedListener : OnItemViewSelectedListener {
        override fun onItemSelected(
            itemViewHolder: Presenter.ViewHolder?,
            item: Any?,
            rowViewHolder: RowPresenter.ViewHolder?,
            row: Row
        ) {
            if (item is MoviesResponse.Result.Detail && handler !== null) {
                    runnable?.let {
                        handler?.removeCallbacks(it)
                    }

                    runnable = Runnable {
                        lifecycleScope.launch {
                            if (item.seasons != null) {
                                itemSelectedListener?.invoke(item)
                            }else {
                                val localIdentificator = getLocalIdentificator(row.headerItem.name)

                                Log.d("local id", localIdentificator.toString())

                                val seasons = repository.getMovieSeasons( localIdentificator ?: identificator, item.movie_id)

                                item.seasons = seasons
                                itemSelectedListener?.invoke(item)
                            }

                        }
                    }

                    handler?.postDelayed(runnable!!, 1000)
            }

        }
    }

    private fun getLocalIdentificator(title: String): String? {
        val startIndex = title.indexOf("(")
        val endIndex = title.indexOf(")")

        return if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
             title.substring(startIndex + 1, endIndex).takeIf { it.isNotEmpty() }
        } else {
             null
        }
    }
    inner class ItemViewClickListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder?,
            item: Any?,
            rowViewHolder: RowPresenter.ViewHolder?,
            row: Row
        ) {
            if (item is MoviesResponse.Result.Detail && context !== null) {
                if (item.seasons != null) {
                    val localIdentificator = getLocalIdentificator(row.headerItem.name)

                    val intent = Intent(context, DetailsActivity::class.java)
                    intent.putExtra("movie", item)

                    intent.putExtra("identificator", localIdentificator ?: identificator)

                    context?.startActivity(intent)
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        rootAdapter.clear()

        handler?.let { handler ->
            runnable?.let {
                handler.removeCallbacks(it)
            }
        }
    }


}