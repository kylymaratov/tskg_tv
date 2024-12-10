package com.example.tvapp.api

import com.example.tvapp.models.Video
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {
    @GET
    suspend fun getHtmlPage(@Url url: String): Response<String>

    @GET("/show/episode/episode.json")
    suspend fun getEpisode(@Query("episode") episodeSourceId: String): Response<Video>
}

