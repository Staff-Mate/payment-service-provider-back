package com.psp.authservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Role implements GrantedAuthority {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name="name")
    String name;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Permission> permissions;

    @Override
    public String getAuthority() {
        return name;
    }

    public List<String> getPermissionNames() {
        List<String> authorities = new ArrayList<>();
        for (Permission permission : permissions)
            authorities.add(permission.getName());
        return authorities;
    }

}
