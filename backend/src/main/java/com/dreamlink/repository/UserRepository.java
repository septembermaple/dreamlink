package com.dreamlink.repository;

import com.dreamlink.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByPhone(String phone);
    Optional<User> findByUsernameOrPhone(String username, String phone);

}
