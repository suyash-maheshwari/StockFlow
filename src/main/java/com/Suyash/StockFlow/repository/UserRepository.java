package com.Suyash.StockFlow.repository;

import com.Suyash.StockFlow.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByIdAndEnabledTrue(Long userId);

    Page<User> findByEnabledTrue(Pageable pageable);
}
