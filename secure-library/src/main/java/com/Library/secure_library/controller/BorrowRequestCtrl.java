package com.Library.secure_library.controller;

import com.Library.secure_library.model.Book;
import com.Library.secure_library.model.BorrowRequest;
import com.Library.secure_library.model.RequestStatus;
import com.Library.secure_library.model.User;
import com.Library.secure_library.repository.BookRep;
import com.Library.secure_library.repository.BorrowRequestRep;
import com.Library.secure_library.repository.UserRep;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class BorrowRequestCtrl {

    private static final Logger logger = LoggerFactory.getLogger(BorrowRequestCtrl.class);
    private static final String REDIRECT_BOOKS = "redirect:/books";
    private static final String REDIRECT_ADMIN_REQUESTS = "redirect:/admin/requests";

    private final BorrowRequestRep borrowRequestRep;
    private final BookRep bookRep;
    private final UserRep userRep;

    @PostMapping("/borrow/{bookId}")
    public String requestBorrow(@PathVariable Long bookId, Authentication authentication) {
        String username = authentication.getName();

        User user = userRep.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Book book = bookRep.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        boolean alreadyPending = borrowRequestRep.findAll().stream()
                .anyMatch(request -> request.getUser().getId().equals(user.getId())
                        && request.getBook().getId().equals(bookId)
                        && request.getStatus() == RequestStatus.PENDING);

        if (!book.isAvailable()) {
            logger.warn("Borrow request refused because book is unavailable. User: {}, Book ID: {}", username, bookId);
        } else if (alreadyPending) {
            logger.warn("Duplicate pending borrow request blocked. User: {}, Book ID: {}", username, bookId);
        } else {
            BorrowRequest request = BorrowRequest.builder()
                    .user(user)
                    .book(book)
                    .status(RequestStatus.PENDING)
                    .build();

            borrowRequestRep.save(request);
            logger.info("Borrow request created by user: {} for book: {}", username, book.getTitle());
        }

        return REDIRECT_BOOKS;
    }

    @GetMapping("/admin/requests")
    public String viewRequests(Model model) {
        model.addAttribute("requests", borrowRequestRep.findAll());
        return "requests";
    }

    @GetMapping("/admin/requests/approve/{id}")
    public String approveRequest(@PathVariable Long id) {
        BorrowRequest request = borrowRequestRep.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        Book book = request.getBook();

        if (request.getStatus() != RequestStatus.PENDING) {
            logger.warn("Approve ignored because request is not pending. Request ID: {}", id);
        } else if (!book.isAvailable()) {
            logger.warn("Approve failed because book already unavailable. Request ID: {}, Book ID: {}", id,
                    book.getId());
        } else {
            request.setStatus(RequestStatus.APPROVED);
            book.setAvailable(false);

            bookRep.save(book);
            borrowRequestRep.save(request);

            logger.info("Borrow request approved. Request ID: {}, Book: {}, User: {}",
                    request.getId(),
                    book.getTitle(),
                    request.getUser().getUsername());
        }

        return REDIRECT_ADMIN_REQUESTS;
    }

    @GetMapping("/admin/requests/reject/{id}")
    public String rejectRequest(@PathVariable Long id) {
        BorrowRequest request = borrowRequestRep.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        if (request.getStatus() != RequestStatus.PENDING) {
            logger.warn("Reject ignored because request is not pending. Request ID: {}", id);
        } else {
            request.setStatus(RequestStatus.REJECTED);
            borrowRequestRep.save(request);

            logger.info("Borrow request rejected. Request ID: {}, Book: {}, User: {}",
                    request.getId(),
                    request.getBook().getTitle(),
                    request.getUser().getUsername());
        }

        return REDIRECT_ADMIN_REQUESTS;
    }
}