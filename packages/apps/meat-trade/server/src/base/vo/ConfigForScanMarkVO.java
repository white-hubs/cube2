package com.guhecloud.cube.biz.meattrade.server.base.vo;

import com.guhecloud.cube.biz.meattrade.server.base.enumeration.ConfigEnum;
import lombok.Data;

/**
 * 基础配置信息
 */
@Data
public class ConfigForScanMarkVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 业务类型
     */
    private ConfigEnum type;

    /**
     * 业务数值
     */
    private Float value;

}
