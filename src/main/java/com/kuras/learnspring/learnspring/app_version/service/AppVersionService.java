package com.kuras.learnspring.learnspring.app_version.service;

import com.kuras.learnspring.learnspring.app_version.dto.AppVersionConfigResponse;
import com.kuras.learnspring.learnspring.app_version.model.SemVersion;
import com.kuras.learnspring.learnspring.common.error.BusinessException;
import com.kuras.learnspring.learnspring.common.error.ErrorCode;
import com.kuras.learnspring.learnspring.app_version.repository.AppVersionConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppVersionService {
    private final AppVersionConfigRepository repository;

   public AppVersionConfigResponse getVersionInfo(String platform, String version) {
       var versionInfo = repository.findByPlatform(platform)
               .orElseThrow(() -> new BusinessException(ErrorCode.PLATFORM_VERSION_CONFIG_NOT_FOUND));

       SemVersion client = SemVersion.parse(version);
       SemVersion min    = SemVersion.parse(versionInfo.getMinVersion());
       SemVersion latest = SemVersion.parse(versionInfo.getLatestVersion());

       boolean forceUpdate   = client.compareTo(min) < 0;
       boolean optionalUpdate = client.compareTo(latest) < 0 && !forceUpdate;

       var res = AppVersionConfigResponse.builder()
               .platform(versionInfo.getPlatform())
               .forceUpdate(forceUpdate)
               .optionalUpdate(optionalUpdate)
               .storeUrl(versionInfo.getStoreUrl())
               .latestVersion(versionInfo.getLatestVersion())
               .build();
       return res;
   }


}
