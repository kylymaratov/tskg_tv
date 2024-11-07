import axios from 'axios';
import { getRandom } from 'random-useragent';

type RequestMethods = 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE';

const createRequest = (baseURL: string) => {
    const request = axios.create({ baseURL });

    request.interceptors.request.use(
        (request) => {
            return request;
        },
        (error) => {
            return Promise.reject(error);
        }
    );

    request.interceptors.response.use(
        (response) => {
            return response;
        },
        (error) => {
            return Promise.reject(error);
        }
    );

    return async <T>(
        url: string = '',
        method: RequestMethods = 'GET',
        data: any = {},
        headers: any = {}
    ) => {
        const randomUserAgent = getRandom();

        return await request<T>({
            url,
            method,
            data: JSON.stringify(data),
            headers: { ...headers, 'User-Agent': randomUserAgent },
            withCredentials: false,
        });
    };
};

export default createRequest;
