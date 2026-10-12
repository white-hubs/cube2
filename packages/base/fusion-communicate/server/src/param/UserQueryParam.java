package com.guhecloud.cube.biz.fusion.communicate.server.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserQueryParam {

    private Set<Long> organizationIds;
    private Set<Long> userIds;
    private Boolean enabled;
    private String keyword;
}
