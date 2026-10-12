package com.guhecloud.cube.base.flow.flowable.task.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.po.TaskLogPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskLogMapper extends BaseMapper<TaskLogPO> {
}
