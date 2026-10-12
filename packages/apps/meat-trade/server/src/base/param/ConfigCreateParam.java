package com.guhecloud.cube.biz.meattrade.server.base.param;

import com.guhecloud.cube.biz.meattrade.server.base.enumeration.ConfigEnum;
import lombok.Data;

/**
 * @author lzy
 * @date 2021/10/19
 */
@Data
public class ConfigCreateParam {

    /**
     * 业务类型
     */
    private ConfigEnum type;

    /**
     * 业务数值
     */
    private Float value;
}
