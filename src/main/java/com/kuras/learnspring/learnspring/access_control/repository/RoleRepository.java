package com.kuras.learnspring.learnspring.access_control.repository;

import java.util.Optional;

import com.kuras.learnspring.learnspring.access_control.entity.Role;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    @EntityGraph(attributePaths = {"permissions"})
    @Query("select r from Role r where r.name = :name")
    Optional<Role> findByNameFetchPermissions(@Param("name") String name);
}
