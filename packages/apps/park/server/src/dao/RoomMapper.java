package com.guhecloud.cube.srv.gen.park.management.server.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.IndustryStatisticsVo;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.ParkFloorsVo;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.RentalTrendsVo;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.RoomDeletedVO;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.RoomVO;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.UtilizationRateVo;
import com.guhecloud.cube.srv.gen.park.management.server.po.RoomPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 房间 Mapper 接口
 * </p>
 *
 * @author zwb
 */
@Mapper
public interface RoomMapper extends BaseMapper<RoomPO> {
    @InterceptorIgnore(tenantLine = "on")
    List<RoomVO> tenantRoom(@Param("tenant") Integer tenant);

    @InterceptorIgnore(tenantLine = "on")
    List<RoomDeletedVO> tenantRoomByIds(@Param("tenant") Integer tenant, @Param("list") List<Long> list);

    @InterceptorIgnore(tenantLine = "on")
    List<RoomDeletedVO> tenantStopRoomById(@Param("tenant") Integer tenant, @Param("list") List<Long> list);

    BigDecimal getProportion(@Param("status") String status);

    List<UtilizationRateVo> getBuildingSpaceUtilizationRate(@Param("list") List<String> list);

    List<IndustryStatisticsVo> getIndustryStatisticsEnterprises();

    BigDecimal getOverallSituationParkLeasing();

    List<RentalTrendsVo> getRentalTrends(@Param("st") LocalDateTime st, @Param("et") LocalDateTime et);

    List<ParkFloorsVo> getRentalRatioParkFloors(@Param("areaId") String areaId);
}
