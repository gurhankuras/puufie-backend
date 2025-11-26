package com.kuras.learnspring.learnspring.auth.service;

import com.kuras.learnspring.learnspring.auth.dto.PasswordPolicyDto;
import com.kuras.learnspring.learnspring.auth.dto.PasswordRequirementsDto;
import com.kuras.learnspring.learnspring.auth.entity.PasswordPolicy;
import com.kuras.learnspring.learnspring.auth.repository.PasswordPolicyRepository;
import com.kuras.learnspring.learnspring.common.error.BusinessException;
import com.kuras.learnspring.learnspring.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PasswordPolicyService {
    private final PasswordPolicyRepository repository;
    private final PasswordValidator passwordValidator;

    public PasswordRequirementsDto getLatestPasswordRequirements() {
        PasswordPolicy policy = repository.findCurrentPolicy()
                .orElseThrow(() -> new BusinessException(ErrorCode.PASSWORD_POLICY_NOT_FOUND));
        return PasswordRequirementsDto.from(policy);
    }

    public PasswordPolicyDto getLatestPasswordPolicy() {
        PasswordPolicy policy = repository.findCurrentPolicy()
                .orElseThrow(() -> new BusinessException(ErrorCode.PASSWORD_POLICY_NOT_FOUND));
        return PasswordPolicyDto.from(policy);
    }


    public void checkPassword(String password, PasswordRequirementsDto currentPolicy) {
        var passwordPolicyValidationResult = passwordValidator.validate(password, currentPolicy);

        if (!passwordPolicyValidationResult.isSuccess())  {
            Map<String, List<String>> details = new HashMap<>();
            details.put("errorMessages", passwordPolicyValidationResult.getErrors());
            throw new BusinessException(ErrorCode.PASSWORD_DOES_NOT_CONFORM_TO_POLICY, details);
        }
    }

    public void checkPassword(String password) {
        var currentPolicy = getLatestPasswordRequirements();
        var passwordPolicyValidationResult = passwordValidator.validate(password, currentPolicy);

        if (!passwordPolicyValidationResult.isSuccess())  {
            Map<String, List<String>> details = new HashMap<>();
            details.put("errorMessages", passwordPolicyValidationResult.getErrors());
            throw new BusinessException(ErrorCode.PASSWORD_DOES_NOT_CONFORM_TO_POLICY, details);
        }
    }
}
