package com.example.tvapp.models

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Movie(
    val movieId: String,
    val posterUrl: String,
    val title: String,
    val genre: String,
    val country: String?,
    val year: String,
    var details: MovieDetails?
): Parcelable

@Parcelize
data class MovieEpisode(
    val episodeId: Int,
    val episodeTitle: String,
    val quality: String,
    val duration: String,
    val episodeSourceId: String
): Parcelable

@Parcelize
data class MovieSeason(
    val seasonId: Int,
    val episodes: List<MovieEpisode>
): Parcelable


@Parcelize
data class MovieDetails (
    val seasons: List<MovieSeason>,
    val description: String
) : Parcelable


data class MoviesList(
    val title: String,
    val movies: MutableList<Movie>
)
