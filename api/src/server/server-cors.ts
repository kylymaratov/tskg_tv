import cors from 'cors';
import { Express } from 'express';

export const setServerCors = (app: Express) => {
    app.use(
        cors({
            origin: '*',
            methods: '*',
            credentials: true,
        })
    );
};
