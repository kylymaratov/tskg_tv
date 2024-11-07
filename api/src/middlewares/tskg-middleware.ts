import { ServerError } from '@/server/server-error';
import { NextFunction, Request, Response } from 'express';
import { body, query, validationResult } from 'express-validator';

export class TsKgMiddleware {
    getEpisodes(): any[] {
        return [
            body('movie_id').notEmpty().withMessage('movie_id field required'),
            this.checkValidationResult,
        ];
    }

    watchEpisode(): any[] {
        return [
            body('movie_id').notEmpty().withMessage('movie_id field required'),
            body('episode_source_id')
                .notEmpty()
                .withMessage('episode_source_id field required'),
            this.checkValidationResult,
        ];
    }

    private checkValidationResult(
        req: Request,
        res: Response,
        next: NextFunction
    ) {
        const errors = validationResult(req);

        if (!errors.isEmpty())
            throw new ServerError('Validation falied', 400, errors.array());

        next();
    }
}
