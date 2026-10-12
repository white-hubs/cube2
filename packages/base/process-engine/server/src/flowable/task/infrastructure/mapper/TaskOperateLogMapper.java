package com.guhecloud.cube.base.flow.flowable.task.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.po.TaskOperateLogPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author pengjian
 * @date 2022/10/28 14:09
 * @description
 */
@Mapper
public interface TaskOperateLogMapper extends BaseMapper<TaskOperateLogPO> {

    List<TaskOperateLogPO> selectTaskOperationLogs(@Param("formNo") String formNo);
}
