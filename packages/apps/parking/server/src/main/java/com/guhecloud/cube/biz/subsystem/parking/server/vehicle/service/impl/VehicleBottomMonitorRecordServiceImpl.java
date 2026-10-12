package com.guhecloud.cube.biz.subsystem.parking.server.vehicle.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.subsystem.parking.server.common.util.DateUnitUtils;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.dao.VehicleBottomMonitorRecordMapper;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.entity.VehicleBottomMonitorRecord;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.param.VehicleBottomMonitorRecordQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.param.VehicleBottomMonitorRecordStatisticsQuery;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.service.VehicleBottomMonitorRecordService;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo.VehicleBottomMonitorRecordPageVO;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo.statistics.vehiclebottom.ReasonTrendTotalVO;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo.statistics.vehiclebottom.ReasonTypeTotalVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VehicleBottomMonitorRecordServiceImpl extends ServiceImpl<VehicleBottomMonitorRecordMapper, VehicleBottomMonitorRecord> implements VehicleBottomMonitorRecordService {

    @Override
    public Pagination<VehicleBottomMonitorRecordPageVO> getPage(VehicleBottomMonitorRecordQueryParam param) {
        Page<VehicleBottomMonitorRecord> monitorRecordPage = this.baseMapper
                .selectPage(PagingUtils.convert(param),
                            Wrappers
                                    .<VehicleBottomMonitorRecord>lambdaQuery()
                                    .eq(Objects.nonNull(param.getAccessType()), VehicleBottomMonitorRecord::getAccessType, param.getAccessType())
                                    .eq(StringUtils.isNotBlank(param.getReasonType()), VehicleBottomMonitorRecord::getReasonType, param.getReasonType())
                                    .and(StringUtils.isNotBlank(param.getKeyword()), q -> q
                                            .like(VehicleBottomMonitorRecord::getVehicleNo, param.getKeyword())
                                            .or()
                                            .like(VehicleBottomMonitorRecord::getReason, param.getKeyword()))
                                    .ge(Objects.nonNull(param.getPeriod()) && Objects.nonNull(param.getPeriod()
                                                                                                      .getBegin()), VehicleBottomMonitorRecord::getAccessTime, ObjectUtils.ifNull(param.getPeriod(), Period::getBegin))
                                    .lt(Objects.nonNull(param.getPeriod()) && Objects.nonNull(param.getPeriod()
                                                                                                      .getEnd()), VehicleBottomMonitorRecord::getAccessTime, ObjectUtils.ifNull(param.getPeriod(), Period::getEnd))
                                    .orderByDesc(VehicleBottomMonitorRecord::getAccessTime)
                           );
        return PagingUtils.convert(monitorRecordPage, VehicleBottomMonitorRecordPageVO.class);
    }

    @Override
    public List<ReasonTrendTotalVO> getReasonThreadTotal(VehicleBottomMonitorRecordStatisticsQuery param) {
        var dateField = DateUnitUtils.handleDateParam(param);
        var startDate = ObjectUtils.ifNull(param.getPeriod(), Period::getBegin);
        var endDate = ObjectUtils.ifNull(param.getPeriod(), Period::getEnd);
        List<ReasonTrendTotalVO> finalDatas = new ArrayList<>();
        //查询数据
        List<ReasonTrendTotalVO> reasonTrendTotalVOS = this.baseMapper.selectReasonTrendStatistics(param);
        for (var start = startDate; start.isBefore(endDate); start = DateUtils.offset(start, dateField, 1)) {
            String date = DateUtils.format(start, param.getDateString());
            String viewDate = DateUtils.format(start, param.getViewDateString());
            ReasonTrendTotalVO trendStatVO = reasonTrendTotalVOS.stream().filter(t -> t.getPeriod().equals(date))
                    .findAny().orElse(null);
            finalDatas.add(trendStatVO == null ? new ReasonTrendTotalVO(0L, viewDate)
                                               : new ReasonTrendTotalVO(trendStatVO.getNum(), viewDate));
        }
        return finalDatas;
    }

    @Override
    public List<ReasonTypeTotalVO> getReasonType(VehicleBottomMonitorRecordStatisticsQuery param) {
        return this.baseMapper.selectReasonTypeStatistics(param);
    }
}
