package com.guhecloud.cube.biz.daily.task.status.bussiness;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.daily.task.status.base.BaseState;
import com.guhecloud.cube.biz.daily.task.status.base.TaskStateProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/11 14:19
 * @Description: 日常任务状态提供器
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DailyTaskDefaultStateProvider implements TaskStateProvider {
    private final ApplicationContext applicationContext;
    private BaseMapper<BaseState> baseMapper;
    private final Map<Long, BaseState> dailyTaskMap = new ConcurrentHashMap<>();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateState() {

        Collection<BaseState> values = dailyTaskMap.values();
        baseMapper.updateById(values);
    }

    @Override
    public void batchInit(List<Long> taskIds, String stateProvider) {
        baseMapper = applicationContext.getBean(stateProvider, BaseMapper.class);
        baseMapper.selectByIds(taskIds)
                .forEach(dailyTaskPO -> dailyTaskMap.put(dailyTaskPO.getId(), dailyTaskPO));
    }

    @Override
    public List<BaseState> batchGet() {
        return new ArrayList<>(dailyTaskMap.values());
    }

    @Override
    public void clearTaskEntityInfo() {
        baseMapper = null;
        dailyTaskMap.clear();
    }

    @Override
    public void setEntity(List<BaseState> entity) {
        dailyTaskMap.putAll(entity.stream().collect(Collectors.toMap(BaseState::getId, e -> e)));
    }
}
