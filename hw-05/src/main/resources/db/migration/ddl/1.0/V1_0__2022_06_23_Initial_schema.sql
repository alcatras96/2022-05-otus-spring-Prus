drop table if exists books;
drop table if exists authors;
drop table if exists genres;
drop table if exists books_genres;

create table genres
(
    id   bigserial not null primary key,
    name varchar(50)
);

create table authors
(
    id        bigserial not null primary key,
    full_name varchar(100)
);

create table books
(
    id        bigserial not null primary key,
    name      varchar(255),
    author_id bigint references authors (id) on delete cascade
);

create table books_genres
(
    book_id  bigint references books (id) on delete cascade,
    genre_id bigint references genres (id) on delete cascade,
    primary key (book_id, genre_id)
);
