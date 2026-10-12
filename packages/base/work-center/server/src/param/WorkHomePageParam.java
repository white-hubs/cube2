package com.guhecloud.cube.biz.work.center.server.param;

import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/4 11:58
 * @Description:
 */
@Data
public class WorkHomePageParam {

    /**
     * 类型
     */
    private String type;

    /**
     * 日期
     */
    private Period period;

    /**
     * 业务类型集合
     */
    private Set<String> businessTypes;

    /**
     * 人员id集合
     */
    private List<Long> userIdList;

}
