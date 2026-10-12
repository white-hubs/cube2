package com.guhecloud.cube.base.flow.flowable.task.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.flow.client.dto.TaskVariableDTO;
import com.guhecloud.cube.base.flow.flowable.task.infrastructure.po.TaskRuNodePO;
import com.guhecloud.cube.base.flow.flowable.task.interfaces.dto.ProcessIdBySourceDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author pengjian
 * @date 2022/8/29 17:59
 * @description
 */
@Mapper
public interface TaskRuNodeMapper extends BaseMapper<TaskRuNodePO> {
    List<TaskVariableDTO> queryTaskByVariables(@Param("endTime") LocalDateTime endTime);

    List<TaskVariableDTO> queryRunTaskByProInsIdAndAssignee(@Param("processInstanceId") String processInstanceId,
                                                            @Param("assignee") String assignee
                                                           );

    /**
     * 根据流程id获取来源工单id
     *
     * @param processInstanceId
     *
     * @return
     */
    String getSourceProcessIdByProcessId(@Param("processInstanceId") String processInstanceId);

    List<ProcessIdBySourceDTO> getProcessIdBySourceId(@Param("processInstanceIdList") List<String> processInstanceIdList);
}
