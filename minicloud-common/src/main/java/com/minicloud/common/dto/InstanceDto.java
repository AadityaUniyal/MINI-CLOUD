package com.minicloud.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstanceDto {
    private Long id;
    private String instanceId;
    private String name;
    private String state;
    private String type;
    private String publicIp;
    private String privateIp;
    private String availabilityZone;
    private Instant launchTime;
}
