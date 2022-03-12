package com.synechron.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.synechron.user.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}