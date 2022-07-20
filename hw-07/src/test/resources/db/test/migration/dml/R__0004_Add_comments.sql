insert into comments(book_id, text)
values ((select id from books where name = 'Pulp fiction'), 'comment 1'),
       ((select id from books where name = 'Pulp fiction'), 'comment 2'),
       ((select id from books where name = 'Pulp fiction'), 'comment 3'),
       ((select id from books where name = 'How to learn C++ in 24 hours'), 'comment 4');