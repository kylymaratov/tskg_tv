import { Express } from 'express';
import { serverEnv } from '@/server/server-env';
import { connect } from 'mongoose';
import os from 'os';

const beforeRun = async () => {
    await connect(serverEnv.env.MONGO_URL || '', {});
};

const afterRun = (PORT: number) => {
    const interfaces = os.networkInterfaces();

    let HOST = 'localhost';

    for (const iface of Object.values(interfaces)) {
        if (iface) {
            for (const details of iface) {
                if (details.family === 'IPv4' && !details.internal) {
                    HOST = details.address;
                    break;
                }
            }
        }
    }

    console.info(`Api running on: http://${HOST}:${PORT}`);
};

const serverRun = async (app: Express) => {
    await beforeRun();

    const PORT: number = serverEnv.isProd ? 5001 : 5002;

    app.listen(PORT, () => afterRun(PORT));
};

export default serverRun;
