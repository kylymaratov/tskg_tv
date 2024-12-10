package com.example.tvapp.fragments

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tvapp.R
import com.example.tvapp.models.Movie
import com.example.tvapp.adapters.MovieGridAdapter

class MoviesGridFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView

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
    }


    fun bindMoviesData(movies: List<Movie>) {
        if (movies.isNotEmpty() && ::recyclerView.isInitialized) {


            val widthPerItem = dpToPx(requireContext(), 170)
            val heightPerItem = dpToPx(requireContext(), 250)
            val screenWidth = resources.displayMetrics.widthPixels

            val itemsPerRow = screenWidth / widthPerItem

            val spacing = dpToPx(requireContext(), 10)
            val gridLayoutManager = GridLayoutManager(requireContext(), itemsPerRow)

            recyclerView.addItemDecoration(GridSpacingItemDecoration(itemsPerRow, spacing, true))

            recyclerView.layoutManager = gridLayoutManager

            val adapter = MovieGridAdapter(movies, heightPerItem)
            recyclerView.adapter = adapter
        }
    }
    private fun dpToPx(context: Context, dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }
}

class GridSpacingItemDecoration(
    private val spanCount: Int,
    private val spacing: Int,
    private val includeEdge: Boolean
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        if (includeEdge) {

            outRect.left = spacing - column * spacing / spanCount
            outRect.top = spacing


            outRect.right = (column + 1) * spacing / spanCount
            outRect.bottom = spacing
        } else {

            outRect.left = column * spacing / spanCount
            outRect.right = spacing - (column + 1) * spacing / spanCount
            outRect.top = spacing
            outRect.bottom = spacing
        }
    }
}