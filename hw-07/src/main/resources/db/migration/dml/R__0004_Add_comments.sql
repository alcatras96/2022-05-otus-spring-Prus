insert into comments(book_id, text)
values ((select id from books where name = 'Pulp fiction'), 'comment 1'),
       ((select id from books where name = 'Pulp fiction'), 'comment 2'),
       ((select id from books where name = 'Pulp fiction'), 'comment 3'),
       ((select id from books where name = 'Tips for surviving on mars'), 'comment 4'),
       ((select id from books where name = 'Sherlock Holmes'), 'comment 5');