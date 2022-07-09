package ru.otus.dao.impl.relation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookGenreRelation {

    private Long bookId;
    private Long genreId;

    public boolean isBookIdEqual(Long bookId) {
        return this.bookId.equals(bookId);
    }
}
