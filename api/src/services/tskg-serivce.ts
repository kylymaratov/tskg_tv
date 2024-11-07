import { NextFunction, Request, Response } from 'express';
import TsKgParser from '@/parser/tskg/parser';
import { ServerError } from '@/server/server-error';

export class TskgSerivce {
    private tsKgParser: TsKgParser = new TsKgParser();
    constructor() {}

    getHome = async (req: Request, res: Response, next: NextFunction) => {
        try {
            const home = await this.tsKgParser.getHome();

            res.status(200).json(home.data);
        } catch (error) {
            next(error);
        }
    };

    getEpisodes = async (req: Request, res: Response, next: NextFunction) => {
        try {
            const { movie_id } = req.body;

            const seasons = await this.tsKgParser.fetchEpisodes(
                String(movie_id)
            );

            res.status(200).json({ ...seasons, movie_id });
        } catch (error) {
            next(error as ServerError);
        }
    };

    watchEpisode = async (req: Request, res: Response, next: NextFunction) => {
        try {
            const { movie_id, episode_source_id } = req.body;

            const episode = await this.tsKgParser.fetchEpisode(
                movie_id,
                episode_source_id
            );

            res.status(200).json(episode);
        } catch (error) {
            next(error as ServerError);
        }
    };
}
