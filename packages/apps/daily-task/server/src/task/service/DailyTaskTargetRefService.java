package com.guhecloud.cube.biz.daily.task.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskTargetRefPO;

import java.util.Set;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/12/24 14:24
 * @Description:
 */
public interface DailyTaskTargetRefService extends IService<DailyTaskTargetRefPO> {
    Set<Long> getTargetIds(Set<Long> taskIds);
}
