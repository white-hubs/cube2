package com.guhecloud.cube.iot.mgt.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.iot.mgt.base.model.CommandLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommandLogMapper extends BaseMapper<CommandLog> {
}