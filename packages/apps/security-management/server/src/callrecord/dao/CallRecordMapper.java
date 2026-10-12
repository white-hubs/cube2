package com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.entity.CallRecord;
import com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.vo.CallTrendsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CallRecordMapper extends BaseMapper<CallRecord> {

    Integer getCallStatistics(@Param("st") LocalDateTime st, @Param("et") LocalDateTime et);

    List<CallTrendsVo> getCallTrends(@Param("dateFormat") String dateFormat, @Param("st") LocalDateTime st, @Param("et") LocalDateTime et);
}
