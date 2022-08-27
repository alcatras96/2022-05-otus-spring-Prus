import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {BookCreationRequest} from "../models/book-creation-request.model";
import {Book} from "../models/book.model";

@Injectable({
    providedIn: 'root'
})
export class BooksService {

    constructor(private http: HttpClient) {
    }

    getAll(): Observable<Book[]> {
        return this.http.get<Book[]>('/api/v1/book');
    }

    get(id: string): Observable<Book> {
        return this.http.get<Book>('/api/v1/book/' + id);
    }

    updateName(id: string, name: string): Observable<void> {
        return this.http.put<void>('/api/v1/book/' + id + "/name", name);
    }

    save(book: BookCreationRequest): Observable<void> {
        return this.http.post<void>('/api/v1/book', book);
    }

    delete(id: string): Observable<void> {
        return this.http.delete<void>('/api/v1/book/' + id);
    }
}
