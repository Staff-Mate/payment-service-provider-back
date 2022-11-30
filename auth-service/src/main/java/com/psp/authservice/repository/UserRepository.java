package com.psp.authservice.repository;

import com.psp.authservice.model.User;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

}
