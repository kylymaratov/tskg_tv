import createRequest from '@/parser/request';
import {
    FormatedDetails,
    FormatHomeResult,
    FromatSeasonsResult,
    TsKgFormat,
} from '@/parser/tskg/format';
import { Movie, TsKgMovieData } from '@/parser/types';
import { ServerError } from '@/server/server-error';

interface ParseHomeResult {
    baseUrl: string;
    data: FormatHomeResult;
}

interface SearchResponse {
    url: string;
    name: string;
}

class TsKgParser {
    private baseUrl = 'https://www.ts.kg';
    private request = createRequest(this.baseUrl);
    private formatter = new TsKgFormat();

    constructor() {}

    private getUrl(category: number, page: number) {
        return `/show?subtitles=0&star=0&zero=0&page=${page}&nextPage=14&sortby=updated&genre=0&category=${category}&network=0`;
    }

    async search(query: string): Promise<ParseHomeResult> {
        try {
            const response = await this.request<SearchResponse[]>(
                '/shows/search/' + query,
                'GET',
                null,
                { 'x-requested-with': 'XMLHttpRequest' }
            );

            const result: ParseHomeResult = {
                baseUrl: this.baseUrl,
                data: {
                    result: [],
                },
            };

            if (response.data.length) {
                result.data.result.push({
                    title: 'TS.KG',
                    identificator: 'tskg',
                    details: response.data.map((item) => {
                        return {
                            movie_id: item.url,
                            title: item.name,
                            genre: '',
                            poster_url:
                                this.baseUrl +
                                item.url.replace('show', 'posters') +
                                '.png',

                            search_title: item.name,
                        };
                    }),
                });
            }

            return result;
        } catch (error) {
            throw new ServerError('Service Unavailable', 503);
        }
    }

    async getHome(): Promise<ParseHomeResult> {
        try {
            const response = await this.request();

            const formatedData = this.formatter.formatHome(
                response.data,
                this.baseUrl
            );

            const randomNumber = Math.floor(Math.random() * 5) + 1;

            const kyrgyzUrl = '/category/kyrgyz_tv_series';
            const animeUrl = this.getUrl(12, randomNumber);
            const multfilmsUrl = this.getUrl(3, randomNumber);
            const turkeyUrl = this.getUrl(18, randomNumber);
            const animes = await this.getCategoryFragment(animeUrl, 'Аниме');

            const multfilms = await this.getCategoryFragment(
                multfilmsUrl,
                'Мультфильмы для детей'
            );
            const kyrgyz = await this.getCategoryFragment(
                kyrgyzUrl,
                'Кыргызские фильмы'
            );
            const turkey = await this.getCategoryFragment(
                turkeyUrl,
                'Турекцие фильмы'
            );

            formatedData.result.push(kyrgyz);
            formatedData.result.push(multfilms);
            formatedData.result.push(turkey);
            formatedData.result.push(animes);

            return {
                baseUrl: this.baseUrl,
                data: formatedData,
            };
        } catch (error) {
            throw new ServerError('Service Unavailable', 503);
        }
    }

    async getCategoryFragment(
        url: string,
        title: string
    ): Promise<FormatedDetails> {
        try {
            const response = await this.request(url);

            const formated = this.formatter.formatCategory(
                response.data,
                this.baseUrl,
                title
            );

            return formated;
        } catch (error) {
            throw new ServerError('Service Unavailable', 503);
        }
    }

    async fetchEpisodes(movie_id: string): Promise<FromatSeasonsResult> {
        try {
            const response = await this.request(movie_id);

            const formatedData = this.formatter.formatEpisodes(response.data);

            return formatedData;
        } catch (error) {
            throw new ServerError('Service Unavailable', 503);
        }
    }

    async fetchEpisode(
        movie: Movie,
        episode_source_id: string
    ): Promise<TsKgMovieData> {
        try {
            const url = `/show/episode/episode.json?episode=${episode_source_id}`;
            const response = await this.request<TsKgMovieData>(
                url,
                'GET',
                {},
                {
                    'x-requested-with': 'XMLHttpRequest',
                }
            );

            return response.data;
        } catch (error) {
            throw new ServerError('Service Unavailable', 503);
        }
    }
}

export default TsKgParser;
