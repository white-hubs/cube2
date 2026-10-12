package com.guhecloud.cube.base.flow.flowable.task.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.po.TaskRunningPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaskRunningLogMapper extends BaseMapper<TaskRunningPO> {
    void batchInsert(@Param("list") List<TaskRunningPO> list);
}
