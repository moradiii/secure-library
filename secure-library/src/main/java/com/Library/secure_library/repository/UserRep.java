package com.Library.secure_library.repository;

import com.Library.secure_library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRep extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

}
