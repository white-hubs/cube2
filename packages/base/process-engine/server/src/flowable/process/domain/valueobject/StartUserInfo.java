package com.guhecloud.cube.base.flow.flowable.process.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StartUserInfo {
    private Long startUserId;

    private String startUserName;

    private Long organizationId;

    private String organizationName;
    private String mobile;

    public StartUserInfo(Long startUserId, Long organizationId, String organizationName) {
        this.startUserId = startUserId;
        this.organizationId = organizationId;
        this.organizationName = organizationName;
    }
}
