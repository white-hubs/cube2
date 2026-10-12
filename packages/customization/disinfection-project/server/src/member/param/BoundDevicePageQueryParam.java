package com.guhecloud.cube.srv.gen.disinfection.server.member.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * BIM 绑定设备分页查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BoundDevicePageQueryParam extends Paging {
    /**
     * 设备名称/编码关键字
     */
    private String keyword;

    /**
     * 设备类型编码(产品分类编码)
     */
    private String productTypeCode;

    /**
     * 在线状态：true 在线，false 离线
     */
    private Boolean deviceState;
}

