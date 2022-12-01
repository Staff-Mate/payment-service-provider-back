package com.psp.authservice.repository;

import com.psp.authservice.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface RegularUserRepository extends UserRepository {

    User findByApiKey(String apiKey);

}
