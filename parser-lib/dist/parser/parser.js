"use strict";
var __createBinding = (this && this.__createBinding) || (Object.create ? (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    var desc = Object.getOwnPropertyDescriptor(m, k);
    if (!desc || ("get" in desc ? !m.__esModule : desc.writable || desc.configurable)) {
      desc = { enumerable: true, get: function() { return m[k]; } };
    }
    Object.defineProperty(o, k2, desc);
}) : (function(o, m, k, k2) {
    if (k2 === undefined) k2 = k;
    o[k2] = m[k];
}));
var __setModuleDefault = (this && this.__setModuleDefault) || (Object.create ? (function(o, v) {
    Object.defineProperty(o, "default", { enumerable: true, value: v });
}) : function(o, v) {
    o["default"] = v;
});
var __importStar = (this && this.__importStar) || (function () {
    var ownKeys = function(o) {
        ownKeys = Object.getOwnPropertyNames || function (o) {
            var ar = [];
            for (var k in o) if (Object.prototype.hasOwnProperty.call(o, k)) ar[ar.length] = k;
            return ar;
        };
        return ownKeys(o);
    };
    return function (mod) {
        if (mod && mod.__esModule) return mod;
        var result = {};
        if (mod != null) for (var k = ownKeys(mod), i = 0; i < k.length; i++) if (k[i] !== "default") __createBinding(result, mod, k[i]);
        __setModuleDefault(result, mod);
        return result;
    };
})();
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.Parser = void 0;
const cheerio = __importStar(require("cheerio"));
const request_1 = require("../utils/request");
const constants_1 = require("../constants/constants");
class Parser {
    getCategoryUrl(categoryNumber, page = 1) {
        return `/show?search=&categoryId=${categoryNumber}&genreId=0&networkId=0&year=0&countryCode=&isStarred=0&sortBy=updated&page=${page}`;
    }
    getHomePage() {
        return __awaiter(this, void 0, void 0, function* () {
            try {
                const body = yield (0, request_1.request)({ url: constants_1.BASE_URL, method: 'GET' });
                const $doc = cheerio.load(body);
                const res = [
                    {
                        title: 'Новинки',
                        movies: [],
                    },
                    {
                        title: 'Популярное',
                        movies: [],
                    },
                ];
                $doc('.app-shows-container > .app-shows-item-full').each((i, el) => {
                    const $item = $doc(el);
                    const movie = {
                        movieId: $item.find('a').attr('href') || '',
                        poster: constants_1.BASE_URL + ($item.find('a > img').attr('src') || ''),
                        title: $item.find('.app-shows-card-title').text().trim(),
                        genre: $item.find('.app-shows-card-tooltip').text().trim(),
                        country: $item
                            .find('.app-shows-card-tooltip > .app-shows-card-flag')
                            .attr('alt') || '',
                        year: '',
                        details: null,
                    };
                    if (!movie.movieId || !movie.poster)
                        return;
                    const extractYear = movie.genre.split(',');
                    const year = extractYear[0] || '';
                    movie.genre = extractYear.slice(1).join(',').trim();
                    movie.year = year;
                    res[i % 2 === 0 ? 0 : 1].movies.push(movie);
                });
                return res;
            }
            catch (error) {
                console.error(`Failed get home page: ${error}`);
            }
        });
    }
    getCategoryPage(categoryNumber_1, categoryTitle_1) {
        return __awaiter(this, arguments, void 0, function* (categoryNumber, categoryTitle, categoryPage = 1) {
            try {
                const url = this.getCategoryUrl(categoryNumber, categoryPage);
                const body = yield (0, request_1.request)({ url, method: 'GET' });
                const $doc = cheerio.load(body);
                const res = {
                    title: categoryTitle,
                    movies: [],
                };
                $doc('.app-shows-container > .app-shows-item-full').each((i, el) => {
                    const $item = $doc(el);
                    const movie = {
                        movieId: $item.find('a').attr('href') || '',
                        poster: constants_1.BASE_URL + ($item.find('a > img').attr('src') || ''),
                        title: $item.find('.app-shows-card-title').text().trim(),
                        genre: $item.find('.app-shows-card-tooltip').text().trim(),
                        country: $item
                            .find('.app-shows-card-tooltip > .app-shows-card-flag')
                            .attr('alt') || '',
                        year: '',
                        details: null,
                    };
                    if (!movie.movieId || !movie.poster)
                        return;
                    const extractYear = movie.genre.split(',');
                    const year = extractYear[0] || '';
                    movie.genre = extractYear.slice(1).join(',').trim();
                    movie.year = year;
                    res.movies.push(movie);
                });
                return res;
            }
            catch (error) {
                console.error(`Failed get categories page: ${error}`);
            }
        });
    }
    getDetailsPage(movieId) {
        return __awaiter(this, void 0, void 0, function* () {
            try {
                const body = yield (0, request_1.request)({ url: movieId, method: 'GET' });
                const $doc = cheerio.load(body);
                const details = {
                    description: $doc('.app-show-description').text(),
                    seasons: [],
                };
                $doc('.app-show-seasons-section-full').each((i, sn) => {
                    const season = {
                        seasonId: i + 1,
                        episodes: [],
                    };
                    $doc(sn)
                        .find('.app-show-season-collapse > table > tbody > tr')
                        .each((j, ep) => {
                        var _a;
                        const episode = {
                            sourceId: (_a = $doc(ep)
                                .find('td > span')
                                .eq(1)
                                .find('a')
                                .attr('id')) === null || _a === void 0 ? void 0 : _a.split('-').pop(),
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
                        };
                        if (!episode.sourceId)
                            return;
                        season.episodes.push(episode);
                    });
                    details.seasons.push(season);
                });
                return details;
            }
            catch (error) {
                console.error(`Failed get details page: ${error} `);
            }
        });
    }
}
exports.Parser = Parser;
