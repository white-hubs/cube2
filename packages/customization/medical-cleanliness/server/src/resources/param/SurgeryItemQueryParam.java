package com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 手术室物品入参
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SurgeryItemQueryParam extends Paging {
    private Long id;
    private Integer tenant;
    private String tenantName;
    /**
     * 手术室id
     */
    private Long deviceId;
    /**
     * 物品名称
     */
    private String name;

    private Long deptId;
    /**
     * 设备id集合
     */
    private List<Long> deviceIdList;

}
