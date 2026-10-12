package com.guhecloud.cube.biz.ouaa.server.dto.retrieve;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RetrieveCacheDTO {
    private Long userId;
    private String resetSessionId;
    private String secretKey;
}
