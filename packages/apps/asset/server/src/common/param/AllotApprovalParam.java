package com.guhecloud.cube.srv.gen.property.management.server.common.param;

import annotation.FormField;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 资产调拨态表单
 */
@Data
public class AllotApprovalParam {
    @FormField(label = "移交部门")
    private Long overDepId;
    @FormField(label = "接收部门id")
    private Long receiveDepId;
    @FormField(label = "移交日期")
    private LocalDateTime receiveTime;
    @FormField(label = "移交后区域")
    private List areaIds;
    @FormField(label = "移交后的存放地点")
    private String place;
    @FormField(label = "备注说明")
    private String remarks;
    @FormField(label = "附件")
    private List<Long> files;
    @FormField(label = "资产id集合")
    private List<Long> propertyIds;
}
