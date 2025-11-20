package com.kuras.learnspring.learnspring.repository;


import java.util.Optional;

import com.kuras.learnspring.learnspring.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByCode(String code);

    boolean existsByCode(String code);
}

