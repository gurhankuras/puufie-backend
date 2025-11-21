package com.kuras.learnspring.learnspring.access_control.repository;

import com.kuras.learnspring.learnspring.access_control.entity.Profile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByName(String name);

    @EntityGraph(attributePaths = {"roles", "roles.permissions"})
    @Query("select p from Profile p where p.name = :name")
    Optional<Profile> findByNameFetchGraph(@Param("name") String name);
}
