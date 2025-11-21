package com.kuras.learnspring.learnspring.auth.repository;

import com.kuras.learnspring.learnspring.access_control.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);

    @Query("""
         select distinct u from User u
         left join fetch u.profiles p
         left join fetch p.roles r
         left join fetch r.permissions perm
         where u.username = :username
         """)
    Optional<User> findByUsernameFetchJoin(@Param("username") String username);
}
