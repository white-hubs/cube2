package com.guhecloud.cube.property.management.client.risk.source.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-30  16:47
 * @Description:
 * @Version: 1.0
 */
@Data
public class RiskSourceLedgerPageParam extends Paging {
    private Set<Long> ids;
    /**
     * 关键字
     */
    private String keyword;
    /**
     * 部门ID
     */
    private Long deptId;
    /**
     * 风险源分类code
     */
    private String typeCode;
    /**
     * 风险源分类code集合
     */
    private Set<String> typeCodes;
    /**
     * 维护部门
     */
    private Long maintenanceDepId;
    /**
     * 使用部门
     */
    private Long receiveDepId;
    /**
     * 状态
     */
    private String status;
    private Period period;
    /**
     * 档位id
     */
    private Long stallId;
    /**
     * 现场点
     */
    private Long sitePointId;
    private String sceneType;
    private Long areaId;
    private Boolean children = true;
    /**
     * 使用人类型
     */
    private String receiveUserType;
    private LocalDateTime purchasingDate;
    private LocalDateTime usageDate;
    /**
     * 电动车分类
     */
    private String electronMobileType;
    /**
     * 品牌
     */
    private String brand;
    /**
     * 档位id集合
     */
    private List<Long> stallIdList;
    /**
     * 现场点id集合
     */
    private List<Long> printIdList;

    public void checkStallOrPrintList() {
        boolean bool = (CollectionUtils.isEmpty(stallIdList) && CollectionUtils.isEmpty(printIdList))
                       || (!CollectionUtils.isEmpty(stallIdList) && !CollectionUtils.isEmpty(printIdList));
        Assert.isParameterError(bool, "档位和现场点有且只有其中一个！");
    }
}
