package com.minicloud.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaunchResponse {
    private String instanceId;
    private String status;
}
