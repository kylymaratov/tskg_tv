package com.example.tvapp.api

import com.example.tvapp.models.MenuResponse
import com.example.tvapp.models.MoviesResponse
import com.example.tvapp.models.SeriesResponse
import com.example.tvapp.models.VideoInfoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url


data class MovieIdRequest(val movie_id: String)

data class WatchRequest(val movie_id: String, val episode_source_id: String)

interface ApiService {
    @GET
    suspend fun getHome(@Url url: String): Response<MoviesResponse>

    @GET("core/menu")
    suspend fun getMenu(): Response<MenuResponse>

    @GET("core/search")
    suspend fun search(@Query("query") query: String): Response<MoviesResponse>

    @POST
    suspend fun getMovieSeasons(@Url url: String, @Body() movieId: MovieIdRequest): Response<SeriesResponse>

    @POST
    suspend fun watchMovie(@Url url: String , @Body() watch: WatchRequest): Response<VideoInfoResponse>
}

