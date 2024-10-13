package com.example.tvapp.models

data class VideoInfoResponse(
    val id: Int,
    val title: String,
    val name: String,
    val sxe: String,
    val showName: String,
    val duration: Int,
    val voice: String,
    val video: Video,
    val link: String,
    val showLink: String,
    val download: String,
    val overlay: Overlay
) {
    data class Video(
        val fileId: Int,
        val url: String,
        val videoType: String,
        val server: String,
        val mimeType: String,
        val subtitles: String? = null
    )

    data class Overlay(
        val id: Int,
        val img: String,
        val link: String
    )
}