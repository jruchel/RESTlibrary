package org.whatever.library.utils;

import org.whatever.library.model.Author;
import org.whatever.library.model.Book;

import java.util.*;

public class Utils {

    public static List<Book> compress(List<Book> books) {
        if (getAuthorsSet(books).size() != 1) return null;
        Map<Book, Integer> mapOfBooks = new TreeMap<>(Comparator.comparing(o -> (o.getTitle() + o.getAuthor())));

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
}
