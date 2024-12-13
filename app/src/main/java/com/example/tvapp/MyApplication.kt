package com.example.tvapp
import android.app.Application
import com.example.tvapp.api.ApiService
import com.example.tvapp.api.RetrofitHtmlHelper
import com.example.tvapp.api.RetrofitJsonHelper
import com.example.tvapp.models.Movie
import com.example.tvapp.models.MovieDetails
import com.example.tvapp.models.MoviesList
import com.example.tvapp.parser.DataParser

class MyApplication : Application() {
      val BASE_URL: String = BuildConfig.BASE_URL
      val htmlRequest: ApiService = RetrofitHtmlHelper.create(BASE_URL)
      val jsonRequest: ApiService = RetrofitJsonHelper.create(BASE_URL)
      val dataParser: DataParser = DataParser()



      suspend fun getMovieDetails(movie: Movie): MovieDetails {
            try {
                  val result = htmlRequest.getHtmlPage(movie.movieId)
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

      suspend fun getCategoryData(categoryNum: Int, page: Int): MoviesList {
            try {
                  data class Category(val id: Int, val title: String)

                  val categories = listOf(
                        Category(id = 12, title = "Аниме"),
                        Category(id = 3, title = "Мультфильмы"),
                        Category(id = 18, title = "Турекцие фильмы")
                  )

                  val url = dataParser.getCategoryUrl(categoryNum, page)

                  val result = htmlRequest.getHtmlPage(url)
                  val body = result.body()

                  if (!result.isSuccessful || body == null) {
                        throw IllegalStateException("Failed to fetch category from the server")
                  }

                  val parsedMovies = dataParser.parseCategoryPage(body.toString(),BASE_URL, "Аниме")

                  return parsedMovies
            } catch (error: Exception) {
                  throw error
            }
      }

}