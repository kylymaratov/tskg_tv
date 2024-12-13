package com.example.tvapp.fragments

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.tvapp.MyApplication
import com.example.tvapp.R
import com.example.tvapp.models.Movie
 import com.example.tvapp.models.MoviesList
import com.example.tvapp.parser.DataParser
import kotlinx.coroutines.launch

class HomeFragment: Fragment(R.layout.fragment_home){
    val headerFragment: HeaderFragment = HeaderFragment()
    val moviesListFragment: MoviesListFragment = MoviesListFragment()

    val dataParser: DataParser = DataParser()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataProcess(view)
        setFramgents()
    }

    private fun setFramgents() {
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.header_fragment, headerFragment)
        transaction.replace(R.id.movies_list_fragment, moviesListFragment)

        transaction.commit()
    }

    private fun setMovies(movies: List<MoviesList>) {
        moviesListFragment.bindMoviesData(movies)
        moviesListFragment.setOnContentSelectedListener {changeMovieHandler(it)}
    }

    private fun getDataProcess(view: View) {
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val errorText =  view.findViewById<TextView>(R.id.errorText)

        lifecycleScope.launch {
            try{
                progressBar.visibility = ProgressBar.VISIBLE
                val movies = getData()
                setMovies(movies)
            } catch(error: Exception) {
                errorText.visibility = TextView.VISIBLE
                errorText.text = error.message
            } finally {
                progressBar.visibility = ProgressBar.GONE
            }
        }
    }

    private fun changeMovieHandler(movie: Movie) {
        lifecycleScope.launch {
            try {
                if(movie.details == null) {
                     val movieDetails = (requireActivity().application as MyApplication).getMovieDetails(movie)
                     movie.details = movieDetails
                }
                  headerFragment.updateBanner(movie)
            }catch (error:Exception){
                movie.details = null
            }
        }
    }

    private suspend fun getDataByUrl(url: String): List<MoviesList> {
        try {
            val request = (requireActivity().application as MyApplication).htmlRequest
            val BASE_URL = (requireActivity().application as MyApplication).BASE_URL

            val result = request.getHtmlPage(url)

            val body = result.body()

            if (!result.isSuccessful || body == null) {
                throw IllegalStateException("Failed to fetch data from the server")
            }

            val movies = dataParser.parseHomePage(body.toString(), BASE_URL)

            return movies
        } catch (error: Exception) {
            throw error
        }
    }


    private suspend fun getData(): List<MoviesList> {
        try {
            val request = (requireActivity().application as MyApplication).htmlRequest
            val BASE_URL = (requireActivity().application as MyApplication).BASE_URL

            val result = request.getHtmlPage("/")

            val body = result.body()

            if (!result.isSuccessful || body == null) {
                throw IllegalStateException("Failed to fetch data from the server")
            }

            val movies = dataParser.parseHomePage(body.toString(), BASE_URL)

            return movies
        } catch (error: Exception) {
            throw error
        }
    }
}