package com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway.query.ParkingPayRecordQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po.ParkingPayRecordPO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingPayRecordPageVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingPayTotalVO;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ParkingPayRecordMapper extends BaseMapper<ParkingPayRecordPO> {

    Page<ParkingPayRecordPageVO> getPage(Page page, @Param("param") ParkingPayRecordQueryParam param, @Param("period") Period period);

    List<ParkingPayRecordPageVO> listAccessInByIdentifyIds(@Param("identifyIds") List<String> identifyIds);

    ParkingPayTotalVO selectParkingPayTotal(@Param("period") Period period);

}
