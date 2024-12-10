package com.example.tvapp

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.tvapp.fragments.HeaderFragment
import com.example.tvapp.fragments.SeriesListFragment
import com.example.tvapp.models.Movie


class DetailsActivity : FragmentActivity() {
    val headerFragment: HeaderFragment = HeaderFragment()
    val seriesListFragment: SeriesListFragment = SeriesListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val movie = intent.getParcelableExtra<Movie>("movie") as Movie
        setMovieData(movie)

        setFragments()
        setContentView(R.layout.activity_details)
    }

    private fun setMovieData(movie: Movie) {
        movie.details?.let {
            seriesListFragment.bindSeriesData(it.seasons)
            headerFragment.updateBanner(movie, true)
        }
    }

    private fun setFragments() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.header_fragment, headerFragment)
        transaction.replace(R.id.series_list_fragment, seriesListFragment)
        transaction.commit()
    }
}