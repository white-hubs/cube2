package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wql
 * @describe 对象巡检次数
 * @date 2023/4/18 19:24
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ObjInspectionInfoVO {
    /**
     * 对象id
     */
    private Long objId;
    /**
     * 对象名
     */
    private String objName;
    /**
     * 巡检次数
     */
    private Long inspectionNum;
}
