package com.minicloud.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProvisionDbRequest {
    private String name;
    private String dbName;
    private String rootPassword;
}
