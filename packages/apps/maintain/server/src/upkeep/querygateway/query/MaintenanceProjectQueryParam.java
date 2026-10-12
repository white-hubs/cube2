package com.guhecloud.cube.srv.gen.maintain.server.upkeep.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lrj
 * @Description 维保配置项
 * @date 2022-4-29 15:12:36
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MaintenanceProjectQueryParam extends Paging {

    /**
     * 设备品类
     */
    private String categoryCode;
    /**
     * 维保项内容
     */
    private String maintenanceContent;

    private Integer tenant;

    private String keyword;

}