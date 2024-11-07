import { Schema } from 'mongoose';
import { Movie } from '@/parser/types';

const MovieSchema = new Schema<Movie>({
    movie_id: String,
    country: String,
    genre: String,
    title: String,
    year: String,
    poster_url: String,
});
