import { NextFunction, Request, Response } from 'express';

export class ServerError implements Error {
    public name: string;
    public message: string;
    public statusCode: number;
    public errors: any[];

    constructor(message: string, statusCode: number = 500, errors: any[] = []) {
        this.name = 'ServerError';
        this.message = message;
        this.statusCode = statusCode;
        this.errors = errors;

        if (Error.captureStackTrace) {
            Error.captureStackTrace(this, ServerError);
        }
    }
}

export const serverErrorHandler = (
    err: ServerError,
    req: Request,
    res: Response,
    next: NextFunction
) => {
    const statusCode = err.statusCode || 500;
    const message = err.message || 'Internal server error';

    res.status(statusCode).json({ message, statusCode, errors: err.errors });
};
