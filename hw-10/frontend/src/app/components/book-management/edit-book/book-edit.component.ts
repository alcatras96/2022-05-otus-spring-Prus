import {Component, OnDestroy, OnInit} from '@angular/core';
import {NgxSpinnerService} from "ngx-spinner";
import {BooksService} from "../../../services/books.service";
import {CommentsService} from "../../../services/comments.service";
import {finalize, Subject, takeUntil} from "rxjs";
import {GenresService} from "../../../services/genres.service";
import {Router} from "@angular/router";
import {Genre} from "../../../models/genre.model";
import {BookCreationRequest} from "../../../models/book-creation-request.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
    selector: 'app-book-edit',
    templateUrl: './book-edit.component.html'
})
export class BookEditComponent implements OnInit, OnDestroy {

    private readonly _destroy$ = new Subject<void>();

    public genres: Genre[];
    public creationFlow: boolean = true;

    public booksForm: FormGroup;
    public commentsForm: FormGroup;

    constructor(private _loadingService: NgxSpinnerService,
                private _booksService: BooksService,
                private _commentsService: CommentsService,
                private _genresService: GenresService,
                private _router: Router,
                private _formBuilder: FormBuilder) {
        this.booksForm = _formBuilder.group({
            id: [''],
            name: ['', Validators.required],
            authorName: [''],
            genres: ['']
        });

        this.commentsForm = _formBuilder.group({
            text: ['', Validators.required]
        });
    }

    ngOnInit(): void {
        this._loadingService.show();

        const urlSearchParams = new URLSearchParams(window.location.search);
        const bookId = urlSearchParams.get("id");
        if (bookId) {
            this._getBook(bookId);
        } else {
            this.booksForm.controls["genres"].addValidators(Validators.required);
            this.booksForm.controls["authorName"].addValidators(Validators.required);
            this._getGenres();
        }
    }

    saveBookName(): void {
        this._loadingService.show();
        const bookId: string = this.booksForm.controls['id'].value;
        const bookName: string = this.booksForm.controls['name'].value;
        this._booksService.updateName(bookId, bookName)
            .pipe(
                takeUntil(this._destroy$),
                finalize(() => this._loadingService.hide())
            )
            .subscribe(() => {
                this._router.navigateByUrl("/");
            });
    }

    addComment(): void {
        this._loadingService.show();
        const bookId: string = this.booksForm.controls['id'].value;
        const text: string = this.commentsForm.controls['text'].value;
        this._commentsService.addByBookId(bookId, text)
            .pipe(
                takeUntil(this._destroy$),
                finalize(() => this._loadingService.hide())
            )
            .subscribe(() => {
                this._router.navigateByUrl("/");
            });
    }

    addBook(): void {
        const bookName: string = this.booksForm.controls['name'].value;
        const authorName: string = this.booksForm.controls['authorName'].value;
        const genres: Genre[] = this.booksForm.controls['genres'].value;

        this._booksService.save(new BookCreationRequest(bookName, authorName, genres))
            .pipe(
                takeUntil(this._destroy$),
                finalize(() => this._loadingService.hide())
            )
            .subscribe(() => {
                this._router.navigateByUrl("/");
            })
    }

    ngOnDestroy(): void {
        this._destroy$.next();
        this._destroy$.complete();
    }

    private _getBook(bookId: string): void {
        this._booksService.get(bookId)
            .pipe(
                takeUntil(this._destroy$),
                finalize(() => this._loadingService.hide())
            )
            .subscribe(book => {
                this.booksForm.controls["id"].setValue(book.id);
                this.booksForm.controls["name"].setValue(book.name);
                this.creationFlow = false;
            });
    }

    private _getGenres(): void {
        this._genresService.getAll()
            .pipe(
                takeUntil(this._destroy$),
                finalize(() => this._loadingService.hide())
            )
            .subscribe(genres => {
                this.genres = genres;
            })
    }
}
