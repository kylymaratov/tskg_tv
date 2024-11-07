import { Router } from 'express';
import tskgApi from '@/api/tskg/tskg-api';
import coreApi from './core/core-api';

const api = Router();

api.use('/core/', coreApi);
api.use(`/tskg/`, tskgApi);

export default api;
