import { Express } from 'express';
import bodyParser from 'body-parser';
import api from '@/api/api';
import { serverErrorHandler } from './server-error';
import { createAccessLogger, createErrorLogger } from './server-logger';
import { serverRequestTime } from './server-request-time';

export const setServerMiddlewares = (app: Express) => {
    app.use(bodyParser.json());
    app.use(serverRequestTime);
    app.use(createAccessLogger());
    app.use(createErrorLogger());
    app.use('/api/', api);
    app.use(serverErrorHandler);
};
