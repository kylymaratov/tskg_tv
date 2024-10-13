package com.example.tvapp.models

import android.os.Parcel
import android.os.Parcelable

data class SeriesResponse(
    val seasons: List<Seasons> = listOf(),
    val description: String = "",
    val movie_id: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(Seasons.CREATOR) ?: listOf(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(seasons)
        parcel.writeString(description)
        parcel.writeString(movie_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SeriesResponse> {
        override fun createFromParcel(parcel: Parcel): SeriesResponse {
            return SeriesResponse(parcel)
        }

        override fun newArray(size: Int): Array<SeriesResponse?> {
            return arrayOfNulls(size)
        }
    }

    data class Seasons(
        val season_id: Number = 0,
        val episodes: List<Episodes> = listOf()
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readValue(Number::class.java.classLoader) as Number,
            parcel.createTypedArrayList(Episodes.CREATOR) ?: listOf()
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeValue(season_id)
            parcel.writeTypedList(episodes)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Seasons> {
            override fun createFromParcel(parcel: Parcel): Seasons {
                return Seasons(parcel)
            }

            override fun newArray(size: Int): Array<Seasons?> {
                return arrayOfNulls(size)
            }
        }
    }

    data class Episodes(
        val episode_id: Number = 1,
        val episode_title: String = "",
        val quality: String = "",
        val duration: String = "",
        val episode_source_id: String = "",
        val episode_source: String = ""
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readValue(Number::class.java.classLoader) as Number,
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: ""
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeValue(episode_id)
            parcel.writeString(episode_title)
            parcel.writeString(quality)
            parcel.writeString(duration)
            parcel.writeString(episode_source_id)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Episodes> {
            override fun createFromParcel(parcel: Parcel): Episodes {
                return Episodes(parcel)
            }

            override fun newArray(size: Int): Array<Episodes?> {
                return arrayOfNulls(size)
            }
        }
    }
}