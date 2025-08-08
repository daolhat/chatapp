package com.project.chatapp.repository;

import com.project.chatapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Query("select u from User u where u.fullName Like %:query% or u.email Like %:query%")
    List<User> searchUser(@Param("query") String query);

}
