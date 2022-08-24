import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Genre} from "../models/genre.model";

@Injectable({
    providedIn: 'root'
})
export class GenresService {

    constructor(private http: HttpClient) {
    }

    getAll(): Observable<Genre[]> {
        return this.http.get<Genre[]>('/api/v1/genre');
    }
}
