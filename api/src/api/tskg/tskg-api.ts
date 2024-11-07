import { Router } from 'express';
import { TskgSerivce } from '@/services/tskg-serivce';
import { TsKgMiddleware } from '@/middlewares/tskg-middleware';

const tskgApi = Router();
const tsKgService = new TskgSerivce();
const tsKgMiddleware = new TsKgMiddleware();

tskgApi.get('/home', tsKgService.getHome);
tskgApi.post(
    '/episodes',
    tsKgMiddleware.getEpisodes(),
    tsKgService.getEpisodes
);
tskgApi.post('/watch', tsKgMiddleware.watchEpisode(), tsKgService.watchEpisode);

export default tskgApi;
