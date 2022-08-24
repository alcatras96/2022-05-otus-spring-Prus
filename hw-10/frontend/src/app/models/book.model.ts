import {Genre} from "./genre.model";

export class Book {
    id: string;
    name: string;
    authorName: string;
    genres: Genre[];
    comments: string[];
}
