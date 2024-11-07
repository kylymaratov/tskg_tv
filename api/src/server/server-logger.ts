import { Request, Response } from 'express';
import morgan from 'morgan';
import * as path from 'path';
import { createWriteStream, existsSync, mkdirSync } from 'fs';

export const logDirPath = path.join(__dirname, '../../', 'logs');

(() => {
    try {
        if (existsSync(logDirPath)) return;

        mkdirSync(logDirPath);
    } catch (error) {
        console.error(`Failed create logs dir: ${error}`);
    }
})();

const accessLogStream = createWriteStream(logDirPath + '/access.log', {
    flags: 'a',
});
const errorLogStream = createWriteStream(logDirPath + '/error.log', {
    flags: 'a',
});

export const createAccessLogger = () => {
    return morgan(
        function (tokens: any, req: Request, res: Response) {
            return [
                tokens.method(req, res),
                tokens.url(req, res),
                '|',
                `${tokens.status(req, res)} status`,
                `${tokens.res(req, res, 'content-length')} bytes`,
                '-',
                tokens['response-time'](req, res),
                'ms',
                '|',
                `Request time: ${new Date().toLocaleString()}`,
            ].join(' ');
        },
        {
            stream: accessLogStream,
            skip: (req, res) => res.statusCode >= 400,
        }
    );
};

export const createErrorLogger = () => {
    return morgan(
        function (tokens: any, req: Request, res: Response) {
            return [
                tokens.method(req, res),
                tokens.url(req, res),
                '|',
                `${tokens.status(req, res)} status`,
                `${tokens.res(req, res, 'content-length')} bytes`,
                '-',
                tokens['response-time'](req, res),
                'ms',
                '|',
                `Request time: ${new Date().toLocaleString()}`,
            ].join(' ');
        },
        {
            stream: errorLogStream,
            skip: (req, res) => res.statusCode < 400,
            immediate: true,
        }
    );
};
