import * as cheerio from 'cheerio'
import { request } from '../utils/request'
import { BASE_URL } from '../constants/constants'
import { ParserReturned } from '../types/parser-types'
import { MovieDetails, MovieEpisodes, MovieSeasons } from '../types/movie-types'

export class Parser {
  private getCategoryUrl(categoryNumber: number, page: number = 1): string {
    return `/show?search=&categoryId=${categoryNumber}&genreId=0&networkId=0&year=0&countryCode=&isStarred=0&sortBy=updated&page=${page}`
  }

  async getHomePage(): Promise<ParserReturned[]> {
    try {
      const body = await request({ url: BASE_URL, method: 'GET' })
      const $doc = cheerio.load(body)

      const res: ParserReturned[] = [
        {
          title: 'Новинки',
          movies: [],
        },
        {
          title: 'Популярное',
          movies: [],
        },
      ]

      $doc('.app-shows-container > .app-shows-item-full').each((i, el) => {
        const $item = $doc(el)

        const movie = {
          movieId: $item.find('a').attr('href') || '',
          poster: BASE_URL + ($item.find('a > img').attr('src') || ''),
          title: $item.find('.app-shows-card-title').text().trim(),
          genre: $item.find('.app-shows-card-tooltip').text().trim(),
          country:
            $item
              .find('.app-shows-card-tooltip > .app-shows-card-flag')
              .attr('alt') || '',
          year: '',
          details: null,
        }

        if (!movie.movieId || !movie.poster) return

        const extractYear = movie.genre.split(',')
        const year = extractYear[0] || ''
        movie.genre = extractYear.slice(1).join(',').trim()
        movie.year = year

        res[i % 2 === 0 ? 0 : 1].movies.push(movie)
      })

      return res
    } catch (error) {
      console.error(`Failed get home page: ${error}`)
    }
  }

  async getCategoryPage(
    categoryNumber: number,
    categoryTitle: string,
    categoryPage: number = 1
  ): Promise<ParserReturned> {
    try {
      const url = this.getCategoryUrl(categoryNumber, categoryPage)
      const body = await request({ url, method: 'GET' })
      const $doc = cheerio.load(body)

      const res: ParserReturned = {
        title: categoryTitle,
        movies: [],
      }

      $doc('.app-shows-container > .app-shows-item-full').each((i, el) => {
        const $item = $doc(el)

        const movie = {
          movieId: $item.find('a').attr('href') || '',
          poster: BASE_URL + ($item.find('a > img').attr('src') || ''),
          title: $item.find('.app-shows-card-title').text().trim(),
          genre: $item.find('.app-shows-card-tooltip').text().trim(),
          country:
            $item
              .find('.app-shows-card-tooltip > .app-shows-card-flag')
              .attr('alt') || '',
          year: '',
          details: null,
        }

        if (!movie.movieId || !movie.poster) return

        const extractYear = movie.genre.split(',')
        const year = extractYear[0] || ''
        movie.genre = extractYear.slice(1).join(',').trim()
        movie.year = year

        res.movies.push(movie)
      })

      return res
    } catch (error) {
      console.error(`Failed get categories page: ${error}`)
    }
  }

  async getDetailsPage(movieId: string): Promise<MovieDetails> {
    try {
      const body = await request({ url: movieId, method: 'GET' })
      const $doc = cheerio.load(body)

      const details: MovieDetails = {
        description: $doc('.app-show-description').text(),
        seasons: [],
      }

      $doc('.app-show-seasons-section-full').each((i, sn) => {
        const season: MovieSeasons = {
          seasonId: i + 1,
          episodes: [],
        }

        $doc(sn)
          .find('.app-show-season-collapse > table > tbody > tr')
          .each((j, ep) => {
            const episode: MovieEpisodes = {
              sourceId: $doc(ep)
                .find('td > span')
                .eq(1)
                .find('a')
                .attr('id')
                ?.split('-')
                .pop(),
              episodeId: j + 1,
              duration: $doc(ep)
                .find('td > span > small')
                .text()
                .replace('| ', ''),
              title: $doc(ep).find('td > span > a').text().trim(),
              quality: $doc(ep)
                .find('td > span > a')
                .text()
                .split(' ')[0]
                .trim(),
            }
            if (!episode.sourceId) return

            season.episodes.push(episode)
          })
        details.seasons.push(season)
      })

      return details
    } catch (error) {
      console.error(`Failed get details page: ${error} `)
    }
  }
}
