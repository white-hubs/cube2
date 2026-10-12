package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

/**
 * @author wql
 * @describe 部门参数参与巡检人数统计
 * @date 2023/12/18 19:31
 */
@Data
public class DepPersonNumVO {
    /**
     * 部门id
     */
    private Long depId;
    /**
     * 人数
     */
    private Integer personNum;
}
