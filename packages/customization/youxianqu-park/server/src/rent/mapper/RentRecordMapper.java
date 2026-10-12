package com.guhecloud.cube.customized.youxianqu.park.management.server.rent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.customized.youxianqu.park.management.server.rent.dto.RentRecordAreaSumDTO;
import com.guhecloud.cube.customized.youxianqu.park.management.server.rent.po.RentRecordPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 租控记录mapper
 */
@Mapper
public interface RentRecordMapper extends BaseMapper<RentRecordPO> {

    /**
     * 面积统计(面积和)
     * 等于状态查询
     *
     * @param status 状态
     * @return 结果
     */
    RentRecordAreaSumDTO RentRecordEqStatusAreaStat(@Param("status") String status);

    /**
     * 面积统计(面积和)
     * 不等于状态查询
     *
     * @param status 状态
     * @return 结果
     */
    RentRecordAreaSumDTO RentRecordNeStatusAreaStat(@Param("status") String status);
}
