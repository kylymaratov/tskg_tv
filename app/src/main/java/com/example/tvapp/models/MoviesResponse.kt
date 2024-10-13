package com.example.tvapp.models

import android.os.Parcel
import android.os.Parcelable

data class MoviesResponse(
    val result: List<Result> = listOf()
) {
    data class Result(
        val details: List<Detail> = listOf(),
        val identificator: String? = "",
        val title: String = ""
    ) {
        data class Detail(
            val movie_id: String = "",
            val genre: String = "",
            val country: String = "",
            val title: String = "",
            val poster_url: String = "",
            val year: String = "",
            var seasons: SeriesResponse? = null,
            var search_title: String? = null
        ) : Parcelable {
            constructor(parcel: Parcel) : this(
                movie_id = parcel.readString() ?: "",
                genre = parcel.readString() ?: "",
                country = parcel.readString() ?: "",
                title = parcel.readString() ?: "",
                poster_url = parcel.readString() ?: "",
                year = parcel.readString() ?: "",
                seasons = parcel.readParcelable(SeriesResponse::class.java.classLoader)
            )

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(movie_id)
                parcel.writeString(genre)
                parcel.writeString(country)
                parcel.writeString(title)
                parcel.writeString(poster_url)
                parcel.writeString(year)
                parcel.writeParcelable(seasons, flags)
            }

            override fun describeContents(): Int {
                return 0
            }

            companion object CREATOR : Parcelable.Creator<Detail> {
                override fun createFromParcel(parcel: Parcel): Detail {
                    return Detail(parcel)
                }

                override fun newArray(size: Int): Array<Detail?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
}