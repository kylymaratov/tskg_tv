import * as mongoose from 'mongoose';
import { MongooseOptions } from 'mongoose';

export const connect = async (url: string, params: MongooseOptions) => {
    await mongoose.connect(url, params);
};
