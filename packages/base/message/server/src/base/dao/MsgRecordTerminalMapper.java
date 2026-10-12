package com.guhecloud.cube.base.message2.server.base.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.message2.server.base.entity.MsgRecordTerminal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

@Mapper
public interface MsgRecordTerminalMapper extends BaseMapper<MsgRecordTerminal> {

    int deletedByBusinessIds(@Param("businessIds") Set<String> businessIds, @Param("deleteTimestamp") Long deleteTimestamp);
}
