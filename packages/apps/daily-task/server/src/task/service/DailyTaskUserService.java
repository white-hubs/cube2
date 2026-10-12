package com.guhecloud.cube.biz.daily.task.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskUserPO;
import com.guhecloud.cube.biz.daily.task.param.DailyParticipateParam;

import java.util.Map;
import java.util.Set;

public interface DailyTaskUserService extends IService<DailyTaskUserPO> {
    void saveBatchByTaskId(Map<Long, Set<DailyParticipateParam>> taskUserMap);

}
