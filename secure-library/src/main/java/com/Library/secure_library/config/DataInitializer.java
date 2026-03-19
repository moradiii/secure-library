package com.Library.secure_library.config;

import com.Library.secure_library.model.Book;
import com.Library.secure_library.model.Role;
import com.Library.secure_library.model.User;
import com.Library.secure_library.repository.BookRep;
import com.Library.secure_library.repository.UserRep;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration

public class DataInitializer {
        @Bean
        CommandLineRunner init(UserRep userRep,
                        BookRep bookRep,
                        PasswordEncoder passwordEncoder) {
                return args -> {

                        if (userRep.count() == 0) {
                                userRep.save(User.builder()
                                                .username("user1")
                                                .password(passwordEncoder.encode("1234"))
                                                .role(Role.USER)
                                                .build());
                                userRep.save(User.builder()
                                                .username("admin")
                                                .password(passwordEncoder.encode("admin"))
                                                .role(Role.ADMIN)
                                                .build());
                        }

                        if (bookRep.count() == 0) {
                                bookRep.save(Book.builder()
                                                .title("The Great Gatsby")
                                                .author("F. Scott Fitzgerald")
                                                .category("Classic")
                                                .available(true)
                                                .build());
                                bookRep.save(Book.builder()
                                                .title("To Kill a Mockingbird")
                                                .author("Harper Lee")
                                                .category("Classic")
                                                .available(true)
                                                .build());
                                bookRep.save(Book.builder()
                                                .title("Harry Potter")
                                                .author("J.K. Rowling")
                                                .category("Fantasy")
                                                .available(true)
                                                .build());
                        }

                };
        }

}
