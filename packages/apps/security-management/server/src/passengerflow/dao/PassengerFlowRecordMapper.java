package com.guhecloud.cube.srv.gen.securitymanagement.server.passengerflow.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.passengerflow.entity.PassengerFlowRecord;
import com.guhecloud.cube.srv.gen.securitymanagement.server.passengerflow.param.PassengerFlowStatisticsParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.passengerflow.vo.AnalysisVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.passengerflow.vo.RankingPassengerFlowVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PassengerFlowRecordMapper extends BaseMapper<PassengerFlowRecord> {
    Integer getPassengerFlowRecordCount(@Param("way") String way, @Param("startTime") LocalDateTime startTime,
                                        @Param("endTime") LocalDateTime endTime
                                       );

    List<RankingPassengerFlowVo> getRankingPassengerFlowRegions(@Param("flag") String flag,
                                                                @Param("param") PassengerFlowStatisticsParam param
                                                               );

    List<AnalysisVo> getAnalysisChartPassengerMuseum(@Param("st") LocalDateTime st, @Param("et") LocalDateTime et,
                                                     @Param("timeFlag") String timeFlag
                                                    );

    void insertOrUpdatePassengerFlowStat(List<PassengerFlowRecord> dataList);
}
