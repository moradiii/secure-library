package com.Library.secure_library.repository;

import com.Library.secure_library.model.BorrowRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRequestRep extends JpaRepository<BorrowRequest, Long> {
    List<BorrowRequest> findByUserId(Long userId);

    boolean existsByUserIdAndBookIdAndStatus(Long userId, Long bookId, String status);
}