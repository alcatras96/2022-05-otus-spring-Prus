import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class CommentsService {

    constructor(private http: HttpClient) {
    }

    deleteByBookId(bookId: string): Observable<void> {
        return this.http.delete<void>('/api/v1/book/' + bookId + "/comment");
    }

    addByBookId(bookId: string, commentText: string): Observable<void> {
        return this.http.post<void>('/api/v1/book/' + bookId + "/comment", commentText);
    }
}
