package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dao.api.BookDao;
import ru.otus.domain.Book;
import ru.otus.service.api.BooksService;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BooksServiceJdbc implements BooksService {

    private final BookDao bookDao;

    @Transactional
    @Override
    public void save(Book book) {
        if (book.getId() == null) {
            bookDao.insert(book);
        } else {
            bookDao.update(book);
        }
    }

    @Override
    public Book getById(Long id) {
        return bookDao.getById(id);
    }

    @Override
    public List<Book> getAll() {
        return bookDao.getAll();
    }

    @Transactional
    @Override
    public void updateName(Long id, String name) {
        bookDao.updateName(id, name);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        bookDao.deleteById(id);
    }
}
