package com.arkksoft.store.models.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arkksoft.store.models.entity.User;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);

    /* @Query(value = "select * from users where email= :email", nativeQuery = true)
    Optional<User> findUserByEmail(@Param("email") String email); */
}
