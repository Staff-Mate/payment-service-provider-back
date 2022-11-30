package com.psp.authservice.service;

import com.psp.authservice.model.Role;
import com.psp.authservice.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository repository;

    public Role getById(Integer id) {
        return repository.getById(id);
    }

}
