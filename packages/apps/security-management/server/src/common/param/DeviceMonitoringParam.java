package com.guhecloud.cube.srv.gen.securitymanagement.server.common.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.util.List;

@Data
public class DeviceMonitoringParam extends Paging {
    //区域id(多级数组）
    private List<String> areaId;

    //状态
    private Boolean deviceStatus;

    //类型id
    private String deviceProdCategoryCodeConcat;

    //收藏设备id
    private String deviceId;

    private String keyword;
    //app融合
    private String appAreaId;
    /**
     * 租户id
     */
    private Integer tenant;

    /**
     * 标签编号集合
     */
    private List<String> tagCodes;

}
