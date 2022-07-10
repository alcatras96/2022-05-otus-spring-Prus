create table genres
(
    id   bigserial primary key,
    name varchar(50) not null
);

create table authors
(
    id        bigserial primary key,
    full_name varchar(100) not null
);

create table books
(
    id        bigserial primary key,
    name      varchar(255) not null,
    author_id bigint references authors (id) on delete cascade
);

create table books_genres
(
    book_id  bigint references books (id) on delete cascade,
    genre_id bigint references genres (id) on delete cascade,
    primary key (book_id, genre_id)
);