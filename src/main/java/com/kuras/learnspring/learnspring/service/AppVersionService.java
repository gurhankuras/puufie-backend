package com.kuras.learnspring.learnspring.service;

import com.kuras.learnspring.learnspring.dto.AppVersionConfigResponse;
import com.kuras.learnspring.learnspring.dto.SemVersion;
import com.kuras.learnspring.learnspring.error.BusinessException;
import com.kuras.learnspring.learnspring.error.ErrorCode;
import com.kuras.learnspring.learnspring.repository.AppVersionConfigRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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
