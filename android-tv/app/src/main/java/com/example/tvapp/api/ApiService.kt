package com.example.tvapp.api

import com.example.tvapp.models.VideoModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url

data class SearchResponse (
        val name: String,
        val url: String
        )

interface ApiService {
    @GET
    suspend fun getHtmlPage(@Url url: String): Response<String>


    @GET
    suspend fun search(@Url url: String): Response<List<SearchResponse>>

    @GET("/show/episode/episode.json")

    suspend fun getEpisode(@Query("episode") episodeSourceId: String,
                               @Header("x-requested-with") requestedWith: String = "XMLHttpRequest"): Response<VideoModel>
}

