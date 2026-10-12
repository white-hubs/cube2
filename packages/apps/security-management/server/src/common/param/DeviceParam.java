package com.guhecloud.cube.srv.gen.securitymanagement.server.common.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.util.List;

@Data
public class DeviceParam extends Paging {
    private String keyword;
    private String areaId;
    private String applicationCode;
    private List<Long> ids;
    private List<String> tagCodes;
    /**
     * 产品id
     */
    private String productId;
    /**
     * 设备品类code
     */
    private String deviceProdCategoryCode;
    /**
     * 过滤的设备主键
     */
    private List<Long> filterDeviceIds;
}
