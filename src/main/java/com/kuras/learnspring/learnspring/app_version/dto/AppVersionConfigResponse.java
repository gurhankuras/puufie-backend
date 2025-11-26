package com.kuras.learnspring.learnspring.app_version.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AppVersionConfigResponse {
    private String platform;
    private boolean optionalUpdate;
    private boolean forceUpdate;

    private String latestVersion;

    private String storeUrl;

}
