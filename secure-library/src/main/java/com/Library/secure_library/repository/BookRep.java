package com.Library.secure_library.repository;

import com.Library.secure_library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRep extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByCategoryContainingIgnoreCase(String category);
}
