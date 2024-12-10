package com.example.tvapp.parser

import com.example.tvapp.models.Movie
import com.example.tvapp.models.MovieDetails
import com.example.tvapp.models.MovieEpisode
import com.example.tvapp.models.MovieSeason
import com.example.tvapp.models.MoviesList
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class DataParser {

    fun getCategoryUrl(category: Int, page: Int = 1): String {
        return "/show?search=&categoryId=$category&genreId=0&networkId=0&year=0&countryCode=&isStarred=0&sortBy=updated&page=$page"
    }

    fun parseHomePage(body: String, BASE_URL: String): MutableList<MoviesList> {
        val document: Document = Jsoup.parse(body)
        val container = document.select("#app-index-shows-new > .app-shows-item-full")

        val result = mutableListOf(
            MoviesList(title = "Новинки", movies = mutableListOf<Movie>()),
            MoviesList(title = "Популярное", movies = mutableListOf<Movie>())
        )

        container.forEachIndexed { i, item ->
            val movieId = item.select("a").attr("href")
            val posterUrl = BASE_URL + item.select("a > img").attr("src")
            val title = item.select(".app-shows-card-title").text().trim()
            var genre = item.select(".app-shows-card-tooltip").text().trim()
            val country = item.select(".app-shows-card-tooltip > .app-shows-card-flag").attr("alt")

            val genres = genre.split(",")
            val year = genres[0]
            genre = genres.drop(1).joinToString().trim()

            if (movieId.isEmpty() || posterUrl.isEmpty()) return@forEachIndexed

            result[(if (i % 2 == 0) 0 else 1)].movies.add(
                Movie(
                    movieId, posterUrl, title, genre, country, year, details = null
                )
            )
        }
        println("Number of items in container: ${container.size}")

        return result
    }

    fun  parseMovieDetails(body: String): MovieDetails {
        val document: Document = Jsoup.parse(body)
        val section = document.select(".app-show-seasons-section-full")
        val description = document.select(".app-show-description").text()
        val seasons = mutableListOf<MovieSeason>()

        section.forEachIndexed { i, item ->
            val seasonId = i + 1
            val episodes = mutableListOf<MovieEpisode>()

            item.select(".app-show-season-collapse > table > tbody > tr").forEachIndexed { j, episode ->
                val episodeSourceId = episode.select("td > span").eq(1)
                    .select("a")
                    .attr("id")
                    ?.split("-")
                    ?.lastOrNull()

                if (episodeSourceId == null) return@forEachIndexed

                val episodeId = j + 1
                val episodeTitle = episode.select("td > span > a").text().trim()
                val duration = episode.select("td > span > small")
                    .text()
                    .replace("| ", "")

                val quality = episodeTitle.split(" ")[0].trim()

                episodes.add(
                    MovieEpisode(
                        episodeId = episodeId,
                        episodeTitle = episodeTitle,
                        quality = quality,
                        duration = duration,
                        episodeSourceId = episodeSourceId
                    )
                )
            }

            seasons.add(MovieSeason(seasonId = seasonId, episodes = episodes))
        }

        return MovieDetails(seasons = seasons, description = description)
    }

    fun parseCategoryPage(body: String, BASE_URL: String, title: String): MoviesList {
        val document: Document = Jsoup.parse(body)
        val section = document.select(".app-shows-container > .app-shows-item-full")

        val result = MoviesList(
            title = title,
            movies = mutableListOf<Movie>()
        )

        section.forEach { item ->
            val movieId = item.select("a").attr("href")
            val posterUrl = BASE_URL + item.select("a > img").attr("src")
            val movieTitle = item.select(".app-shows-card-title").text().trim()
            var genre = item.select(".app-shows-card-tooltip").text().trim()
            val country = item.select(".app-shows-card-tooltip > .app-shows-card-flag").attr("alt")

            val genres = genre.split(",")
            val year = genres.firstOrNull() ?: ""
            genre = genres.drop(1).joinToString().trim()

            if (movieId.isEmpty() || posterUrl.isEmpty()) return@forEach

            result.movies.add(
                Movie(
                    movieId = movieId,
                    posterUrl = posterUrl,
                    title = movieTitle,
                    genre = genre,
                    country = country,
                    year = year,
                    details = null
                )
            )
        }

        return result
    }
}