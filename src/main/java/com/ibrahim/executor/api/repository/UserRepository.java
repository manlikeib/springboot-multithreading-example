package com.ibrahim.executor.api.repository;

import com.ibrahim.executor.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
