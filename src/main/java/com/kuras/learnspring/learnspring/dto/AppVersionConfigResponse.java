package com.kuras.learnspring.learnspring.dto;


import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

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
