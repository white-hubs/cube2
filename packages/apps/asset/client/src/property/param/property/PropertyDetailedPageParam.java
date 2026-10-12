package com.guhecloud.cube.property.management.client.property.param.property;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class PropertyDetailedPageParam extends Paging {
    private Set<Long> ids;
    /**
     * 派发单号、资产名称
     */
    private String keyword;
    /**
     * 部门ID
     */
    private Long deptId;
    /**
     * 使用部门
     */
    private String receiveDepId;
    /**
     * 资产状态(LEAVE_UNUSED:"闲置",
     * MAKE_USE_OF:"在用",
     * WEED_OUT:"报废")
     */
    private String status;
    /**
     * 资产盘点状态")
     */
    private String taskStatus;
    /**
     * 购入日期
     */
    private LocalDateTime purchaseDate;
    /**
     * 入库日期
     */
    private LocalDateTime startTime;
    /**
     * 来源
     */
    private String sourceId;
    /**
     * 资产类别
     */
    private String typeId;
    private List<Long> typeIds;
    /**
     * 区域
     */
    private List<String> areaIds;
    private String areaId;
    /**
     * 入库开始时间
     */
    private LocalDateTime warehousingStartDate;
    /**
     * 入库结束时间
     */
    private LocalDateTime warehousingEndDate;
    /**
     * 购入开始时间
     */
    private LocalDateTime purchaseStartDate;
    /**
     * 购入结束时间
     */
    private LocalDateTime purchaseEndDate;
    /**
     * 操作类型
     */
    private String businessType;
    /**
     * 排查已经绑定模型编码的资产
     */
    private String sceneType;
    /**
     * 过期时间
     */
    private LocalDateTime usageDate;
}
