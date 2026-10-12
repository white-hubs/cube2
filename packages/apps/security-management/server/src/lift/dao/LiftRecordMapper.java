package com.guhecloud.cube.srv.gen.securitymanagement.server.lift.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lift.entity.LiftRecord;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lift.param.LiftRecordParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lift.vo.LiftRecordVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lift.vo.OperationsAnalysisVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface LiftRecordMapper extends BaseMapper<LiftRecord> {
    Page<LiftRecordVo> getLiftRecord(Page page, @Param("param") LiftRecordParam param);

    List<OperationsAnalysisVo> getOperationsAnalysis(@Param("dateFormat") String timeFlag, @Param("st") LocalDateTime st,
                                                     @Param("et") LocalDateTime et
                                                    );
}
