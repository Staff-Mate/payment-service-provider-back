package com.psp.authservice.repository;

import com.psp.authservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role getById(Integer id);
    Role getByName(String name);
}
