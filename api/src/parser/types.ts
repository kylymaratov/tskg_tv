export interface Movie {
    movie_id: string;
    poster_url: string;
    title: string;
    genre: string;
    country?: string;
    year?: string;
    description?: string;
}

export type MovieQuality = 'HD' | 'SD';

export interface MovieSeason {
    season_id: number;
    episodes: MovieEpisode[];
}

export interface MovieEpisode {
    episode_id: number;
    episode_title: string;
    episode_source_id: string;
    quality: MovieQuality;
    duration: string;
    episode_data?: TsKgMovieData;
}

export interface TsKgMovieData {
    id: number;
    title: string;
    name: string;
    sxe: string;
    showName: string;
    duration: number;
    voice: string;
    video: {
        fileId: number;
        url: string;
        videotype: string;
        server: string;
        mimetype: string;
        subtitles: null | string;
    };
    link: string;
    showLink: string;
    download: string;
    overlay: {
        id: number;
        img: string;
        link: string;
    };
}
