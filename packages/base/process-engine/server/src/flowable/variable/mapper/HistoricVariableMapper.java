package com.guhecloud.cube.base.flow.flowable.variable.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface HistoricVariableMapper {

    @Select("select TEXT_ from act_hi_varinst where TASK_ID_ is null and PROC_INST_ID_=#{processInstanceId} and NAME_=#{variableName}")
    String selectProcessVariable(@Param("variableName") String variableName, @Param("processInstanceId") String processInstanceId);
}
