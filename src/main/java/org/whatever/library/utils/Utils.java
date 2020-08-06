package org.whatever.library.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.whatever.library.Properties;
import org.whatever.library.model.Author;
import org.whatever.library.model.Book;

import java.util.*;

public class Utils {

    public static List<Book> compress(List<Book> books) {
        if (getAuthorsSet(books).size() != 1) return null;
        Map<Book, Integer> mapOfBooks = new HashMap<>();

        for (Book b : books) {
            if (mapOfBooks.containsKey(b)) {
                mapOfBooks.replace(b, mapOfBooks.get(b) + b.getInStock());
            } else {
                mapOfBooks.put(b, b.getInStock());
            }
        }

        List<Book> result = new ArrayList<>();

        for (Book b : mapOfBooks.keySet()) {
            b.setInStock(mapOfBooks.get(b));
            result.add(b);
        }

        return result;
    }

    private static Set<Author> getAuthorsSet(List<Book> books) {
        Set<Author> authorSet = new HashSet<>();
        for (Book b : books) {
            authorSet.add(b.getAuthor());
        }

        return authorSet;
    }

    public static Pageable getPageable(int page, int elements) {
        return PageRequest.of(page - 1, elements);
    }

    public static Pageable getPageable(int page) {
        return getPageable(page, Properties.getInstance().getPageElements());
    }
}
