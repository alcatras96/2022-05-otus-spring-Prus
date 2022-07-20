create table comments
(
    id      bigserial primary key,
    book_id bigint references books (id) on delete cascade,
    text varchar(255) not null
);
