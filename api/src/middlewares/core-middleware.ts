import { ServerError } from '@/server/server-error';
import { NextFunction, Request, Response } from 'express';
import { query, validationResult } from 'express-validator';

export class CoreMiddleware {
    getLogs(): any[] {
        return [
            query('type').notEmpty().withMessage('type field required'),
            this.checkValidationResult,
        ];
    }

    search(): any[] {
        return [
            query('query').notEmpty().withMessage('query field required'),
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
