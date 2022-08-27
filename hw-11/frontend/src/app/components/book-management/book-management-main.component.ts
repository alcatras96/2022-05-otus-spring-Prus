import {Component, OnDestroy, OnInit} from '@angular/core';
import {BooksService} from "../../services/books.service";
import {NgxSpinnerService} from "ngx-spinner";
import {finalize, Subject, takeUntil} from "rxjs";
import {CommentsService} from "../../services/comments.service";
import {Book} from "../../models/book.model";

@Component({
    selector: 'app-book-management-main',
    templateUrl: './book-management-main.component.html'
})
export class BookManagementMainComponent implements OnInit, OnDestroy {

    private readonly _destroy$ = new Subject<void>();

    public books: Book[];

    constructor(private _loadingService: NgxSpinnerService,
                private _booksService: BooksService,
                private _commentsService: CommentsService) {
    }

    ngOnInit(): void {
        this._loadingService.show();
        this._booksService.getAll()
            .pipe(
                takeUntil(this._destroy$),
                finalize(() => this._loadingService.hide())
            )
            .subscribe(books => {
                this.books = books;
            });
    }

    deleteBook(book: Book): void {
        this._loadingService.show()
        this._booksService.delete(book.id)
            .pipe(
                takeUntil(this._destroy$),
                finalize(() => this._loadingService.hide())
            )
            .subscribe(() => {
                this.books.splice(this.books.indexOf(book), 1)
            })
    }

    deleteComments(book: Book): void {
        this._loadingService.show();
        this._commentsService.deleteByBookId(book.id)
            .pipe(
                takeUntil(this._destroy$),
                finalize(() => this._loadingService.hide())
            )
            .subscribe(() => {
                delete book.comments;
            })
    }

    ngOnDestroy(): void {
        this._destroy$.next();
        this._destroy$.complete();
    }
}
