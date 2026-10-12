package com.guhecloud.cube.biz.vmonitor.server.param.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * 用户批量删除条件
 *
 * @author lanchun
 * @date 2024/8/23 9:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBatchDeleteParam {

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 组织结构主键集合
     */
    private Set<Long> organizationIds;

    /**
     * 用户主键集合
     */
    private Set<Long> userIds;
}
