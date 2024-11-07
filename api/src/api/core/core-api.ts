import { CoreMiddleware } from '@/middlewares/core-middleware';
import { CoreService } from '@/services/core-service';
import { Router } from 'express';

const coreApi = Router();
const coreService = new CoreService();
const coreMiddleware = new CoreMiddleware();

coreApi.get('/menu', coreService.getMenu);
coreApi.get('/logs', coreMiddleware.getLogs(), coreService.getLogs);
coreApi.get('/search', coreMiddleware.search(), coreService.search);

export default coreApi;
