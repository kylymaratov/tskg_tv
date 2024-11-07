import TsKgParser from '@/parser/tskg/parser';
import { ServerError } from '@/server/server-error';
import { logDirPath } from '@/server/server-logger';
import { NextFunction, Request, Response } from 'express';
import * as fs from 'fs';

const menu = [
    {
        id: 0,
        title: 'TS.KG',
        icon: 'https://www.ts.kg/img/logo.svg',
        identificator: 'tskg',
    },
    {
        id: 1,
        title: 'YummyAnime',
        icon: '',
        identificator: 'yummyanime',
    },
];

export class CoreService {
    private tsKgParser: TsKgParser = new TsKgParser();
    constructor() {}

    getMenu = (req: Request, res: Response, next: NextFunction) => {
        res.status(200).json({ result: menu });
    };

    getLogs = async (req: Request, res: Response, next: NextFunction) => {
        try {
            const { type } = req.query;

            const logs: string = await new Promise((resolve, reject) => {
                fs.readFile(logDirPath + `/${type}.log`, (err, data) => {
                    if (err)
                        return reject(new ServerError('Failed read file', 500));
                    resolve(data.toString());
                });
            });

            const logsArr = logs.split('\n');

            res.status(200).json({ type, logs: logsArr });
        } catch (error) {
            next(error);
        }
    };

    search = async (req: Request, res: Response, next: NextFunction) => {
        try {
            const { query } = req.query;
            const searchResult = await this.tsKgParser.search(query as string);

            res.status(200).json(searchResult.data);
        } catch (error) {
            next(error);
        }
    };
}
