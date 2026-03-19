package com.Library.secure_library.controller;

import com.Library.secure_library.model.Book;
import com.Library.secure_library.repository.BookRep;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/books")
@RequiredArgsConstructor
public class AdminBookCtrl {

    private static final Logger logger = LoggerFactory.getLogger(AdminBookCtrl.class);

    private final BookRep bookRep;

    @GetMapping("/add")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "add-book";
    }

    @PostMapping("/add")
    public String addBook(@Valid @ModelAttribute("book") Book book, BindingResult result) {
        if (result.hasErrors()) {
            return "add-book";
        }

        bookRep.save(book);
        logger.info("Admin added book: {}", book.getTitle());

        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String showEditBookForm(@PathVariable Long id, Model model) {
        Book book = bookRep.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book ID: " + id));

        model.addAttribute("book", book);
        return "edit-book";
    }

    @PostMapping("/update/{id}")
    public String updateBook(@PathVariable Long id,
            @Valid @ModelAttribute("book") Book updatedBook,
            BindingResult result) {
        if (result.hasErrors()) {
            updatedBook.setId(id);
            return "edit-book";
        }

        Book existingBook = bookRep.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book ID: " + id));

        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setCategory(updatedBook.getCategory());
        existingBook.setAvailable(updatedBook.isAvailable());

        bookRep.save(existingBook);
        logger.info("Admin updated book with id: {}", id);

        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        Book book = bookRep.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book ID: " + id));

        bookRep.delete(book);
        logger.info("Admin deleted book with id: {} and title: {}", id, book.getTitle());

        return "redirect:/books";
    }
}