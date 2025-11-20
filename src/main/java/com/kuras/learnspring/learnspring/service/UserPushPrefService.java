package com.kuras.learnspring.learnspring.service;

import com.kuras.learnspring.learnspring.dto.UserPushNotificationPreferenceDto;
import com.kuras.learnspring.learnspring.entity.UserPushPreference;
import com.kuras.learnspring.learnspring.error.BusinessException;
import com.kuras.learnspring.learnspring.error.ErrorCode;
import com.kuras.learnspring.learnspring.mapper.UserPushPreferenceMapper;
import com.kuras.learnspring.learnspring.repository.UserPushPreferenceRepository;
import com.kuras.learnspring.learnspring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserPushPrefService {
    private final UserRepository userRepository;
    private final UserPushPreferenceRepository prefRepo;

    public UserPushNotificationPreferenceDto create(Long userId, UserPushNotificationPreferenceDto dto) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        var entity = UserPushPreferenceMapper.toEntity(dto);
        entity.setUser(user);
        var saved = prefRepo.save(entity);
        return UserPushPreferenceMapper.toDto(saved);
    }

    public UserPushNotificationPreferenceDto getByUserId(Long userId) {
        var prefs = prefRepo.getByUserId(userId);

        if (!prefs.isPresent()) {
            prefs = Optional.of(UserPushPreference.getDefault());
        }
        return UserPushPreferenceMapper.toDto(prefs.orElseThrow());
    }

    public UserPushNotificationPreferenceDto update(Long prefId, UserPushNotificationPreferenceDto dto) {
        var entity = prefRepo.findById(prefId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        UserPushPreferenceMapper.updateEntity(dto, entity);
        var saved = prefRepo.save(entity);
        return UserPushPreferenceMapper.toDto(saved);
    }
}
