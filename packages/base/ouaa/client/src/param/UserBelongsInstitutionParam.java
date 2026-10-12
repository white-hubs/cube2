package com.guhecloud.cube.biz.ouaa.client.param;

import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/7/12 11:52
 * @Description:
 */

@Data
public class UserBelongsInstitutionParam {

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 机构类型
     */
    private String type;
    /**
     * 条件组合类型
     */
    private String conditionType;

    /**
     * 机构Id
     */
    private List<Long> institutionIds;
}
