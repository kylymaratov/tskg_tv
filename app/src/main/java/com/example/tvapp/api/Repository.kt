package com.example.tvapp.api

 import androidx.lifecycle.MutableLiveData
 import com.example.tvapp.models.MenuResponse
 import com.example.tvapp.models.MoviesResponse
import com.example.tvapp.models.SeriesResponse
 import com.example.tvapp.models.VideoInfoResponse

class Repository(private val service: ApiService) {
    val movies = MutableLiveData<Response<MoviesResponse>?>()
    val searchResult = MutableLiveData<Response<MoviesResponse>>()
    val movieSeasonsCache: MutableLiveData<Map<String, Response<SeriesResponse>>> = MutableLiveData(mutableMapOf())

    fun clearMovies() {
        movies.value = null
    }

    suspend fun getMenu(): MenuResponse? {
        try {
            val result = service.getMenu()

            if (result.isSuccessful) {
                return result.body()
            } else {
                return null
            }
        } catch (e: Exception) {
            return null
        }
    }

    suspend fun searchMovies(query: String) {
        try {
            val result = service.search( query)

            if (result.isSuccessful) {
                result.body()?.let { responseBody ->
                    searchResult.postValue(Response.Success(responseBody))
                } ?: run {
                    searchResult.postValue(Response.Error(null))
                }
            } else {
                searchResult.postValue(Response.Error(null))
            }
        } catch (e: Exception) {
            searchResult.postValue(Response.Error(null))
        }
    }

    suspend fun getHome(repName: String) {
        if (movies.value != null) {
            return movies.postValue(movies.value)
        }

         try {
            val result = service.getHome("${repName}/home")

            if (result.isSuccessful) {
                result.body()?.let { responseBody ->
                     movies.postValue(Response.Success(responseBody))
                } ?: run {
                     movies.postValue(Response.Error(null))
                }
            } else {
                 movies.postValue(Response.Error(null))
            }
        } catch (e: Exception) {
             movies.postValue(Response.Error(null))
        }
    }

    suspend fun getMovieSeasons(repName: String, movie_id: String): SeriesResponse? {
         val cachedResponse = movieSeasonsCache.value?.get(movie_id)

         if (cachedResponse is Response.Success) {
            return cachedResponse.data
        }

        return try {
            val result = service.getMovieSeasons("${repName}/episodes", MovieIdRequest(movie_id))

            if (result.isSuccessful) {
                result.body()?.also { seasonsResponse ->
                     val updatedCache = movieSeasonsCache.value?.toMutableMap() ?: mutableMapOf()
                    updatedCache[movie_id] = Response.Success(seasonsResponse)

                     if (updatedCache.size > 20) {
                         val oldestKey = updatedCache.keys.first()
                        updatedCache.remove(oldestKey)
                    }

                    movieSeasonsCache.postValue(updatedCache)
                }
            } else {
                null
            }
        } catch (e: Exception) {
             null
        }
    }

    suspend fun watchMovie(repName: String, watch: WatchRequest): VideoInfoResponse? {
        return try {
            val result = service.watchMovie("${repName}/watch", watch)

            if (result.isSuccessful) {
                result.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}