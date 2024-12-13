package com.example.tvapp

import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.example.tvapp.fragments.HeaderFragment
import com.example.tvapp.fragments.SeriesListFragment
import com.example.tvapp.models.Movie
import kotlinx.coroutines.launch

class DetailsActivity : FragmentActivity() {
    val headerFragment: HeaderFragment = HeaderFragment()
    val seriesListFragment: SeriesListFragment = SeriesListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val errText = findViewById<TextView>(R.id.errorText)
        val movie = intent.getParcelableExtra<Movie>("movie") as Movie

        lifecycleScope.launch {
            if (movie.details === null) {
                try {
                    movie.details = (application as MyApplication).getMovieDetails(movie)
                } catch (error: Exception) {
                    errText.text = error.message
                    errText.visibility
                    movie.details = null
                }
            }
            setFragments()
            setMovieData(movie)
        }
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