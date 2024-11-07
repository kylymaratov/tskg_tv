import { NextFunction, Request, Response } from 'express';

export const serverRequestTime = (
    req: Request,
    res: Response,
    next: NextFunction
) => {
    const start = Date.now();
    const path = req.url;

    res.on('finish', () => {
        const duration = Date.now() - start;
        const contentLength = res.getHeaders()['content-length'];
        const userAgent = req.headers['user-agent'];

        console.info(
            `${req.method} ${path} - ${userAgent} | ${contentLength} bytes - ${duration}ms | ${res.statusCode}`
        );
    });

    next();
};
