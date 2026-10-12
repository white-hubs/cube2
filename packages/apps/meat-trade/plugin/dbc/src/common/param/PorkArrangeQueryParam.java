package com.guhecloud.cube.sn.meattrade.plugin.common.param;

import com.guhecloud.cube.sn.meattrade.plugin.common.enumeration.PorkArrangePushDataStatusEnum;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * 理货数据查询参数
 *
 * @author lanchun
 * @date 2025/1/3 18:02
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PorkArrangeQueryParam extends Paging {

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 理货数据推送状态
     */
    private PorkArrangePushDataStatusEnum status;

    /**
     * 理货时间
     */
    private Period period;

    /**
     * 理货设备集合
     */
    private Set<String> deviceIds;
}
