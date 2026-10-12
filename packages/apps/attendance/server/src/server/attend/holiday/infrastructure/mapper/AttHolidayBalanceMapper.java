package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayBalancePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 假期余额
 */
@Mapper
public interface AttHolidayBalanceMapper extends BaseMapper<AttHolidayBalancePO> {
    /**
     * 增量修改余额
     *
     * @param staffId       人员id
     * @param holidayRuleId 规则id
     * @param available     变更可用额度 负数为减,空不修改
     * @param occupy        变更占用额度 负数为减,空不修改
     * @param updateRemark  修改备注
     */
    void incrementalUpdateBalance(@Param("staffId") Long staffId, @Param("holidayRuleId") Long holidayRuleId,
                                  @Param("available") Long available, @Param("occupy") Long occupy,
                                  @Param("updateRemark") String updateRemark
                                 );
}
