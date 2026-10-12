package com.guhecloud.cube.biz.ouaa.server.param.query;

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
public class UserMapQuery {
    private Set<String> userNames;
    private Set<Long> userIds;
    /**
     * org 部门；
     */
    private Set<String> infoTypes;

}
