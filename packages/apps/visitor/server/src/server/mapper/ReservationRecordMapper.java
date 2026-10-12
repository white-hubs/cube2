package com.guhecloud.cube.srv.gen.visitor.management.server.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.ReservationRecordPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 预约记录
 */
@Mapper
public interface ReservationRecordMapper extends BaseMapper<ReservationRecordPO> {

    /**
     * 查询预约数
     *
     * @param wrapper 条件
     *
     * @return 预约数
     */
    @Select("select count(`id`) from `reservation_record` ${ew.customSqlSegment} ")
    Integer getReservationNum(@Param(Constants.WRAPPER) Wrapper<ReservationRecordPO> wrapper);
}
