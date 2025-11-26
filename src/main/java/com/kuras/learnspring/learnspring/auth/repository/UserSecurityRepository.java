package com.kuras.learnspring.learnspring.auth.repository;

import com.kuras.learnspring.learnspring.auth.entity.UserSecurity;
import com.kuras.learnspring.learnspring.common.error.BusinessException;
import com.kuras.learnspring.learnspring.common.error.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSecurityRepository extends JpaRepository<UserSecurity, Long> {
    default UserSecurity findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_SECURITY_INFO_NOT_FOUND));
    }
}
