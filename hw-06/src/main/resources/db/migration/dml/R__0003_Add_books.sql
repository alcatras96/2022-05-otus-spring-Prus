insert into books(name, author_id)
values ('Pulp fiction', (select id from authors where full_name = 'Ivan Ivanovich Ivanov')),
       ('How to learn C++ in 24 hours',(select id from authors where full_name = 'Ivan Ivanovich Ivanov')),
       ('Sherlock Holmes',(select id from authors where full_name = 'Arthur Conan Doyle')),
       ('Tips for surviving on mars',(select id from authors where full_name = 'Alexander Alexandrovich Alexandrov')),
       ('History of Ancient Rome',(select id from authors where full_name = 'Maxim Maximovich Maximov'));

insert into books_genres (book_id, genre_id)
values ((select id from books where name = 'Pulp fiction'), (select id from genres where name = 'comedy')),
       ((select id from books where name = 'Pulp fiction'), (select id from genres where name = 'action')),
       ((select id from books where name = 'Pulp fiction'), (select id from genres where name = 'drama')),
       ((select id from books where name = 'How to learn C++ in 24 hours'), (select id from genres where name = 'comedy')),
       ((select id from books where name = 'How to learn C++ in 24 hours'), (select id from genres where name = 'drama')),
       ((select id from books where name = 'Sherlock Holmes'), (select id from genres where name = 'drama')),
       ((select id from books where name = 'Sherlock Holmes'), (select id from genres where name = 'detective')),
       ((select id from books where name = 'Sherlock Holmes'), (select id from genres where name = 'thriller')),
       ((select id from books where name = 'Tips for surviving on mars'), (select id from genres where name = 'science')),
       ((select id from books where name = 'History of Ancient Rome'), (select id from genres where name = 'science')),
       ((select id from books where name = 'History of Ancient Rome'), (select id from genres where name = 'history'));