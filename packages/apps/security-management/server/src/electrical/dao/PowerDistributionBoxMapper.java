package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.entity.PowerDistributionBox;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.DistributionsExcelVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.DistributionsVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.ElectricityVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.RecordTimeVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PowerDistributionBoxMapper extends BaseMapper<PowerDistributionBox> {

    Page<RecordTimeVo> getElectricityTimeRecord(Page page, @Param("startTime") LocalDateTime startTime,
                                                @Param("endTime") LocalDateTime endTime
                                               );

    Page<RecordTimeVo> getDistributionTimeRecord(Page page, @Param("startTime") LocalDateTime startTime,
                                                 @Param("endTime") LocalDateTime endTime
                                                );

    PowerDistributionBox getLastOne(@Param("deviceId") Long deviceId);

    List<ElectricityVo> getElectricityTimeRecordDetails(@Param("startTime") LocalDateTime startTime,
                                                        @Param("endTime") LocalDateTime endTime
                                                       );

    List<DistributionsVo> getDistributionsTimeRecordDetails(@Param("startTime") LocalDateTime startTime,
                                                            @Param("endTime") LocalDateTime endTime,
                                                            @Param("deviceId") Long deviceId
                                                           );

    List<DistributionsExcelVo> getDistributionsTimeRecordExcel(@Param("startTime") LocalDateTime startTime,
                                                               @Param("endTime") LocalDateTime endTime,
                                                               @Param("deviceId") Long deviceId
                                                              );

    BigDecimal getPowerConsumption(@Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);

    BigDecimal getCnpPowerConsumption(@Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);
}
