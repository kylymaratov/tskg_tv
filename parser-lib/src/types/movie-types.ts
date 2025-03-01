export interface MovieEpisodes {
  episodeId: number
  title: string
  quality: string
  duration: string
  sourceId: string
}

export interface MovieSeasons {
  seasonId: number
  episodes: MovieEpisodes[]
}

export interface MovieDetails {
  description: string
  seasons: MovieSeasons[]
}

export interface Movie {
  movieId: string
  poster: string
  title: string
  genre: string
  country: string
  year: string | null
  details: MovieDetails | null
}
