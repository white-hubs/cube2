package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.dto.HolidayConsumptionDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po.HolidayDeductDetailPO;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Set;

@Mapper
public interface HolidayDeductDetailMapper extends BaseMapper<HolidayDeductDetailPO> {

    /**
     * 假期消费汇总
     *
     * @param staffIds
     * @param period
     *
     * @return
     */
    List<HolidayConsumptionDTO> selectConsumptionSummaryByStaff(@Param("staffIds") Set<Long> staffIds,
                                                                @Nullable @Param("period") Period period
                                                               );
}
