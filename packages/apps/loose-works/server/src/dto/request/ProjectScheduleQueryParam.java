package com.guhecloud.cube.srv.gen.loose.works.dto.request;

import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/11/8 15:01
 * @Description:
 */
@Data
public class ProjectScheduleQueryParam {
    /**
     * 工程Id集合
     */
    private List<Long> projectIds;

    /**
     * 工程Id集合
     */
    private List<String> projectNames;
    /**
     * 工程编号
     */
    private List<String> projectNumList;
}
