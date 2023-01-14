package com.psp.authservice.repository;

import com.psp.authservice.model.Administrator;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministratorRepository extends UserRepository<Administrator> {
}
