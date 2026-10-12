package com.guhecloud.cube.base.device.center.open.interfaces.web.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class DevicePageQry extends Paging {
    /**
     * 关键字 设备名称或者设备型号名称
     */
    private String keyword;
    /**
     * 区域id
     */
    private String lastAreaId;
    /**
     * 部门id
     */
    private String departmentId;
    /**
     * 产品品类
     */
    private String prodCategoryCode;
    /**
     * 设备id集合
     */
    private List<Long> filterIds;

    private Set<String> filterTagCodes;

}
