package com.guhecloud.cube.biz.emergency.server.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @author Appet
 * @Description 通用统计参数
 * @date 2024-01-26 10:08:36
 */
@Data
public class CommonStatParam {
    private String type;
    //分组的格式 例如： %Y-%m-%d
    private String groupFormat;
    private Period period;
}