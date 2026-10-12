package com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.subsystem.parking.client.vo.StatisticalChartDataVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway.query.ParkingAccessQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.enumeration.ParkingAccessTypeEnum;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po.ParkingAccessRecordPO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingAccessTypeBucketCountVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingAccessTypeHourCountVO;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;

@Mapper
public interface ParkingAccessRecordMapper extends BaseMapper<ParkingAccessRecordPO> {

    /**
     * 获取滞留车辆列表
     *
     * @param param
     * @param page
     *
     * @return
     */
    Page<ParkingAccessRecordPO> selectStrandedRecords(@Param("param") ParkingAccessQueryParam param, Page page);

    /**
     * 获取通行次数统计
     *
     * @param accessType
     * @param period
     *
     * @return
     */
    Integer selectParkingAccessTotal(@Param("accessType") ParkingAccessTypeEnum accessType, @Param("period") Period period,
                                     @Param("parkingInfoCodes") LinkedHashSet<String> parkingInfoCodes
                                    );

    /**
     * 时间
     */
    LocalDateTime getInTime(@Param("vehicleNo") String vehicleNo);

    /**
     * 按小时聚合通行趋势。name=小时(0-23)，valus=数量。
     */
    List<StatisticalChartDataVO> selectRecordTrendByHour(@Param("accessType") ParkingAccessTypeEnum accessType,
                                                         @Param("period") Period period,
                                                         @Param("parkingInfoCodes") LinkedHashSet<String> parkingInfoCodes);

    /**
     * 进/出类型一次按小时聚合。
     */
    List<ParkingAccessTypeHourCountVO> selectRecordTrendByHourAllTypes(@Param("period") Period period,
                                                                       @Param("parkingInfoCodes") LinkedHashSet<String> parkingInfoCodes);

    /**
     * 进/出类型一次按指定时间粒度聚合。
     */
    List<ParkingAccessTypeBucketCountVO> selectRecordTrendByBucketAllTypes(@Param("period") Period period,
                                                                           @Param("parkingInfoCodes") LinkedHashSet<String> parkingInfoCodes,
                                                                           @Param("granularity") String granularity);

}
