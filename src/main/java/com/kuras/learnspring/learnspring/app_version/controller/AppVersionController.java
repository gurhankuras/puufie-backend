package com.kuras.learnspring.learnspring.app_version.controller;


import com.kuras.learnspring.learnspring.app_version.dto.AppVersionConfigResponse;
import com.kuras.learnspring.learnspring.app_version.service.AppVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/app-version")
public class AppVersionController {

    private final AppVersionService service;

    @GetMapping
    ResponseEntity<AppVersionConfigResponse> getVersion(@RequestParam String platform, @RequestParam String version) {
        var res = service.getVersionInfo(platform, version);
        return ResponseEntity.ok(res);
    }
}
