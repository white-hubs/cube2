package com.guhecloud.cube.biz.daily.task.task.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskUserPO;
import com.guhecloud.cube.biz.daily.task.enums.DailyTaskUserTypeEnum;
import com.guhecloud.cube.biz.daily.task.param.DailyParticipateParam;
import com.guhecloud.cube.biz.daily.task.task.dao.DailyTaskUserMapper;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskUserService;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 任务人员
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DailyTaskUserServiceImpl extends ServiceImpl<DailyTaskUserMapper, DailyTaskUserPO> implements DailyTaskUserService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatchByTaskId(Map<Long, Set<DailyParticipateParam>> taskUserMap) {
        if (MapUtils.isEmpty(taskUserMap)) {
            return;
        }
        List<DailyTaskUserPO> taskUserSaveList = new ArrayList<>();
        for (var entry : taskUserMap.entrySet()) {
            for (DailyParticipateParam param : entry.getValue()) {
                taskUserSaveList.add(DailyTaskUserPO
                                             .builder()
                                             .id(IdWorker.getId())
                                             .masterId(entry.getKey())
                                             .taskUserType(StringUtils.isBlank(param.getTaskUserType()) ?
                                                           DailyTaskUserTypeEnum.ASSIGNEE.name() : param.getTaskUserType())
                                             .superintendentType(param.getParticipateType().name())
                                             .targetId(param.getTargetId())
                                             .build());
            }

        }
        this.saveBatch(taskUserSaveList);
    }
}
