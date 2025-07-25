package com.vexa.vexa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vexa.vexa.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
 Optional<Role> findByName(String name);
}
