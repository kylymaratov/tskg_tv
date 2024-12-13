package com.example.tvapp.fragments

import android.content.Context
 import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tvapp.MyApplication
import com.example.tvapp.R
import com.example.tvapp.models.Movie
import com.example.tvapp.adapters.MovieGridAdapter
import com.example.tvapp.utils.Common
import kotlinx.coroutines.launch

class MoviesGridFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private var categoryId: Int = 12
    private var pageId: Int = 1
    private lateinit var movies: MutableList<Movie>
    private var isEndOfListReached = false
    private var stopLoadMore: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_movies_grid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.movies_grid)
        recycleEvents()
    }

    fun bindMoviesData(data: MutableList<Movie>, categoryId: Int, pageId: Int) {
        this.categoryId = categoryId
        this.pageId = pageId
        this.movies = data

        val adapter = recyclerView.adapter
        if (adapter is MovieGridAdapter) {
            adapter.updateMovies(data)
        } else {
            drawMoviesData()
        }
    }

    fun clearMoviesData() {
        val adapter = recyclerView.adapter
        if (adapter is MovieGridAdapter) {
            adapter.clearMovies()
        }
    }

    private fun drawMoviesData() {
        val widthPerItem = dpToPx(requireContext(), 165)
        val heightPerItem = dpToPx(requireContext(), 260)
        val screenWidth = resources.displayMetrics.widthPixels

        val itemsPerRow = screenWidth / widthPerItem

        val spacing = dpToPx(requireContext(), 10)
        val gridLayoutManager = GridLayoutManager(requireContext(), itemsPerRow)

        recyclerView.addItemDecoration(Common.GridSpacingItemDecoration(itemsPerRow, spacing, true))

        recyclerView.layoutManager = gridLayoutManager

        val adapter = MovieGridAdapter(movies, heightPerItem)
        recyclerView.adapter = adapter

    }

    private fun recycleEvents() {
        if (pageId != 0) {
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = recyclerView.layoutManager as? GridLayoutManager ?: return

                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                    if (!isEndOfListReached && lastVisibleItemPosition == totalItemCount - 1) {
                        isEndOfListReached = true
                        loadMoreMovies( )
                    }
                }
            })
        }
    }

    private fun loadMoreMovies() {
       lifecycleScope.launch {
           try {
               if (!stopLoadMore) {
                   pageId = pageId + 1
                   val data = (requireActivity().application as MyApplication).getCategoryData(categoryId, pageId);
                   val startPosition = movies.size
                   data.movies.forEach { movie ->
                       if (movies.none { it.movieId == movie.movieId }) {
                           movies.add(movie)
                       }
                   }
                   recyclerView.adapter?.notifyItemRangeInserted(startPosition, data.movies.size)
               }

           } catch (error: Exception) {
               if (pageId >= 5) {
                   stopLoadMore = true
               }
           } finally {
               isEndOfListReached = false
           }
       }
    }

    private fun dpToPx(context: Context, dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }
}

