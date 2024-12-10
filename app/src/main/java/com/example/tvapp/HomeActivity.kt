package com.example.tvapp

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.example.tvapp.fragments.HeaderFragment
import com.example.tvapp.fragments.MenuFragment
import com.example.tvapp.fragments.MoviesGridFragment
import com.example.tvapp.fragments.MoviesListFragment
import com.example.tvapp.models.Movie
import com.example.tvapp.models.MovieDetails
import com.example.tvapp.models.MoviesList
import com.example.tvapp.parser.DataParser
import kotlinx.coroutines.launch

class HomeActivity : FragmentActivity() {
    val headerFragment: HeaderFragment = HeaderFragment()
    val moviesListFragment: MoviesListFragment = MoviesListFragment()
    val moviesGridFragment: MoviesGridFragment = MoviesGridFragment()

    val dataParser: DataParser = DataParser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        getDataProgress()
        setFramgents()
    }

    private fun setFramgents() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.header_fragment, headerFragment)
        transaction.replace(R.id.movies_list_fragment, moviesListFragment)

        transaction.commit()
    }

    private fun setMovies(movies: List<MoviesList>) {
        moviesListFragment.bindMoviesData(movies)
        moviesListFragment.setOnContentSelectedListener {changeMovieHandler(it)}
    }

    private fun getDataProgress() {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val errorText = findViewById<TextView>(R.id.errorText)

        lifecycleScope.launch {
            try{
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
            if(movie.details == null) {
                val movieDetails = getMovieDetails(movie)
                movie.details = movieDetails
            }
            headerFragment.updateBanner(movie)
        }
    }

    private suspend fun getMovieDetails(movie: Movie): MovieDetails {
        try {
            val request = (application as MyApplication).request
            val result = request.getHtmlPage(movie.movieId)
            val body = result.body()

            if (!result.isSuccessful || body == null) {
                throw IllegalStateException("Failed to fetch movie details from the server")
            }

            val formatedMovieDetails = dataParser.parseMovieDetails(body)

            return formatedMovieDetails
        } catch (error: Exception) {
            throw error
        }
    }

    private suspend fun getData(): List<MoviesList>  {
        try {
            val request = (application as MyApplication).request
            val BASE_URL = (application as MyApplication).BASE_URL

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
    private suspend fun getCategoryData(movies: MutableList<MoviesList>): List<MoviesList> {
        try {
            data class Category(val id: Int, val title: String)

            val request = (application as MyApplication).request
            val BASE_URL = (application as MyApplication).BASE_URL

            val categories = listOf(
                Category(id = 12, title = "Аниме"),
                Category(id = 3, title = "Мультфильмы"),
                Category(id = 18, title = "Турекцие фильмы")
            )

            for (category in categories) {
                val url = dataParser.getCategoryUrl(category.id)

                val result = request.getHtmlPage(url)
                val body = result.body()

                if (!result.isSuccessful || body == null) continue

                val parsedMovies = dataParser.parseCategoryPage(body.toString(),BASE_URL, category.title)

                movies.add(parsedMovies)
            }
            return movies
        } catch (error: Exception) {
            throw error
        }
    }
}