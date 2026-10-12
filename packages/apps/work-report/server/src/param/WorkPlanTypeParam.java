package com.guhecloud.cube.srv.gen.reportmanagement.server.param;

import lombok.Data;

/**
 * @author Appet
 * @Description 工作安排类型
 * @date 2024-01-04 10:08:36
 */
@Data
public class WorkPlanTypeParam {

    //编码
    private String code;
    //类型名称
    private String name;
    //状态 0 禁用 1 启用
    private Boolean status;

}