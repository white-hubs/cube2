package com.guhecloud.cube.srv.gen.securitymanagement.server.common.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.util.List;

/**
 * @author pengjian
 * @date 2023/11/1 14:28
 * @description
 */
@Data
public class DeviceInfoPageQuery extends Paging {

    private String areaId;

    private Boolean deviceStatus;

    private String applicationCode;

    private String keyword;

    /**
     * 这2个我也不知道有什么作用，抄过来了
     */
    private String deviceId;
    private List<Long> ids;

    private String deviceProdCategoryCode;

    private String deviceProdCategoryCodeConcat;

}
