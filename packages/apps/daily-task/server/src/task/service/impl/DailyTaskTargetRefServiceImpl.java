package com.guhecloud.cube.biz.daily.task.task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskTargetRefPO;
import com.guhecloud.cube.biz.daily.task.task.dao.DailyTaskTargetRefMapper;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskTargetRefService;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/12/24 14:24
 * @Description:
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DailyTaskTargetRefServiceImpl extends ServiceImpl<DailyTaskTargetRefMapper, DailyTaskTargetRefPO> implements DailyTaskTargetRefService {
    @Override
    public Set<Long> getTargetIds(Set<Long> taskIds) {
        List<DailyTaskTargetRefPO> dailyTaskTargetRefPOS = this.lambdaQuery()
                .in(DailyTaskTargetRefPO::getTaskId, taskIds)
                .list();
        if (CollectionUtils.isEmpty(dailyTaskTargetRefPOS)) {
            return new HashSet<>();
        }
        return dailyTaskTargetRefPOS.stream().map(DailyTaskTargetRefPO::getTargetRefId).collect(Collectors.toSet());
    }
}
