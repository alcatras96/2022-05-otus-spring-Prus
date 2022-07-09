package ru.otus.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dao.api.AuthorDao;
import ru.otus.domain.Author;
import ru.otus.service.api.AuthorsService;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthorsServiceImpl implements AuthorsService {

    private final AuthorDao authorDao;

    @Transactional
    @Override
    public void save(Author author) {
        if (author.getId() == null) {
            authorDao.insert(author);
        } else {
            authorDao.update(author);
        }
    }

    @Override
    public Author getById(Long id) {
        return authorDao.getById(id);
    }

    @Override
    public List<Author> getAll() {
        return authorDao.getAll();
    }

    @Transactional
    @Override
    public void updateFullNameById(Long id, String fullName) {
        authorDao.updateFullNameById(id, fullName);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        authorDao.deleteById(id);
    }
}
