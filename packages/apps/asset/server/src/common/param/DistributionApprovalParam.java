package com.guhecloud.cube.srv.gen.property.management.server.common.param;

import annotation.FormField;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 资产派发动态表单
 */
@Data
public class DistributionApprovalParam {
    @FormField(label = "使用部门")
    private Long receiveDepId;
    @FormField(label = "领用人")
    private Long receiveUserId;
    @FormField(label = "出库后存放区域")
    private List areaIds;
    @FormField(label = "派发日期")
    private LocalDateTime receiveTime;
    @FormField(label = "出库后的存放地点")
    private String place;
    @FormField(label = "备注说明")
    private String remarks;
    @FormField(label = "附件")
    private List<Long> files;
    @FormField(label = "资产id集合")
    private List<Long> propertyIds;
}
