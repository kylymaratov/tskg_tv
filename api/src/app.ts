import './path-register';

import express from 'express';
import { setServerCors } from '@/server/server-cors';
import { setServerMiddlewares } from '@/server/server-middlewares';
import serverRun from '@/server/server-runner';

async function bootstrap() {
    try {
        const app = express();

        setServerCors(app);
        setServerMiddlewares(app);

        await serverRun(app);
    } catch (error) {
        console.log(`Server failed with error: ${error}`);
    }
}

bootstrap();
