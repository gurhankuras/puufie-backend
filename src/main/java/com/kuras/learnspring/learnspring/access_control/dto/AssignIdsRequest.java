package com.kuras.learnspring.learnspring.access_control.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record AssignIdsRequest(
        @NotEmpty List<Long> ids
) { }