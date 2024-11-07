import { config } from 'dotenv';

config();

interface ServerEnv {
    isProd: boolean;
    env: NodeJS.ProcessEnv;
}

export const serverEnv: ServerEnv = {
    isProd: process.env.NODE_ENV === 'production',
    env: process.env,
};
