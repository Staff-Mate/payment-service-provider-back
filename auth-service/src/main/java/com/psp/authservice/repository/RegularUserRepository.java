package com.psp.authservice.repository;

import com.psp.authservice.model.RegularUser;
import com.psp.authservice.model.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegularUserRepository extends UserRepository<RegularUser>, JpaSpecificationExecutor<RegularUser> {

    RegularUser findByApiKey(String apiKey);

}
