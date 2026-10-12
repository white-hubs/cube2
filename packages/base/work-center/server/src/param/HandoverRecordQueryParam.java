package com.guhecloud.cube.biz.work.center.server.param;

import com.guhecloud.cube.biz.work.center.server.enumeration.HandoverStatusEnum;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/10/30 17:00
 * @Description:
 */
@Data
public class HandoverRecordQueryParam extends Paging {

    private String keyword;

    private Period period;

    private HandoverStatusEnum status;

    /**
     * 交班人
     */
    private Long shiftOverUser;

    /**
     * 接班人
     */
    private Long takeOverUser;

    /**
     * 是否只查询当前所属用户
     */
    private Boolean permission;
}
