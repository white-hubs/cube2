package com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo;

import lombok.Data;

/**
 * @author wql
 * @describe 故障单设备事件消息
 * @date 2023/11/7 16:14
 */
@Data
public class FaultOrderEventVO {
    /**
     * 故障单id
     */
    private Long id;
    /**
     * 故障对象id
     */
    private Long objId;
    /**
     * 流程实例id
     */
    private String processInsId;
    /**
     * 故障对象类型
     */
    private String objType;
    /**
     * 网格id
     */
    private Long areaId;
    /**
     * 故障状态
     */
    private String faultStatus;
    /**
     * 故障单名称
     */
    private String name;
    /**
     * 租户号
     */
    private Integer tenant;

}
