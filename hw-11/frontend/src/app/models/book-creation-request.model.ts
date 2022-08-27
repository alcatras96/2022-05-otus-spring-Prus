import {Genre} from "./genre.model";

export class BookCreationRequest {
    name: string;
    authorName: string;
    genreIds: string[];

    constructor(name: string, authorName: string, genres: Genre[]) {
        this.name = name;
        this.authorName = authorName;
        this.genreIds = genres.map(genre => genre.id);
    }
}
