import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {RouterModule, Routes} from '@angular/router';

import {HttpClientModule} from '@angular/common/http';

import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NgxSpinnerModule} from "ngx-spinner";
import {BookManagementMainComponent} from "./components/book-management/book-management-main.component";
import {BookEditComponent} from "./components/book-management/edit-book/book-edit.component";

const appRoutes: Routes = [
    {path: '', component: BookManagementMainComponent},
    {path: 'edit', component: BookEditComponent}
];

@NgModule({
    declarations: [
        AppComponent,
        BookManagementMainComponent,
        BookEditComponent
    ],
    imports: [
        BrowserModule,
        FormsModule,
        HttpClientModule,
        RouterModule.forRoot(appRoutes),
        NgxSpinnerModule,
        ReactiveFormsModule,
        BrowserAnimationsModule
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
