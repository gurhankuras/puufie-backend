package com.kuras.learnspring.learnspring.auth.repository;

import com.kuras.learnspring.learnspring.auth.entity.User;
import com.kuras.learnspring.learnspring.common.error.BusinessException;
import com.kuras.learnspring.learnspring.common.error.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);

    default User findByUsernameOrThrow(String username) {
        return findByUsername(username).orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    @Query("""
         select distinct u from User u
         left join fetch u.profiles p
         left join fetch p.roles r
         left join fetch r.permissions perm
         where u.username = :username
         """)
    Optional<User> findByUsernameFetchJoin(@Param("username") String username);
}
