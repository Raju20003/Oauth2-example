package com.springBoot.Oauth2Example.repository;

import com.springBoot.Oauth2Example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findByEmail(String email);
    Boolean existsByEmail(String email);
}
