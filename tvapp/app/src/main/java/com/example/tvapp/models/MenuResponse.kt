package com.example.tvapp.models

data class MenuResponse(
    val result: List<MenuItem> = listOf()
) {
    data class MenuItem(
        val id: Int,
        val title: String,
        val icon: String,
        val identificator: String
    )
}