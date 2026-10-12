package com.guhecloud.cube.srv.gen.energymanagement.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @author Appet
 * @Description 能耗采集配置
 * @date 2024-01-26 10:08:36
 */
@Data
public class EnergyCollectConfigQueryParam extends Paging {

    //设备分类code
    private String categoryCode;
    //设备分类名称
    private String categoryName;
    //采集方式：auto 自动/hand 手动录入
    private String collectType;
    //1 开启 0 关闭 是否开启采集
    private Boolean collectOn;

}