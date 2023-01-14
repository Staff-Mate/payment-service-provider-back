package com.psp.authservice.repository;

import com.psp.authservice.model.User;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public interface UserRepository<Entity extends User> extends JpaRepository<Entity, Integer> {

    User findByEmail(String email);

    List<User> findAllByRole_Name(String roleUser);
}
