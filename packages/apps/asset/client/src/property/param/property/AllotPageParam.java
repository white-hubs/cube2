package com.guhecloud.cube.property.management.client.property.param.property;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AllotPageParam extends Paging {
    /**
     * 操作类型（调拨：ALLOT_UNIT；派发：ALLOT_PERSONAL）
     */
    private String type;
    /**
     * 资产名称、编码
     */
    private String keyword;
    /**
     * 状态
     */
    private String status;
    /**
     * 移交日期
     */
    private LocalDateTime receiveTime;
    /**
     * 资产类别
     */
    private Long typeId;
    /**
     * 移交部门
     */
    private Long overDepId;
    /**
     * 接收部门
     */
    private Long receiveDepId;
    private String processInstanceId;
}
