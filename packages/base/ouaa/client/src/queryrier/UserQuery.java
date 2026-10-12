package com.guhecloud.cube.biz.ouaa.client.queryrier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author pengjian
 * @date 2022/11/15 16:04
 * @description
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserQuery {
    private Set<Long> positionIds;
    private Set<Long> appointmentIds;
    private Set<Long> roleIds;
    private Set<Long> organizationIds;
    private Set<Long> userIds;
    private Set<String> userNameList;
    private Set<String> mobileList;
    private Boolean enabled;
    private String keyword;
}
