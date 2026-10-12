package com.guhecloud.cube.biz.linkage.server.facade.iot.reqeust;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 单属性设置命令
 *
 * @author lanchun
 * @date 2022/6/22 16:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertySetActionRequest {

    /**
     * 设备主键
     */
    private String deviceId;

    /**
     * 属性编号
     */
    private String propCode;

    /**
     * 属性值
     */
    private String value;
}
