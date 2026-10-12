package com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.traffic.detection.server.detection.application.querygateway.query.TrafficDetectionAreaStatQueryParam;
import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.po.TrafficDetectionHourlyPersonFlowPO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.repository.dao.TrafficDetectionHourlyPersonFlowMapper;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.statistics.TrafficDetectionAreaPersonFlowRankVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public class TrafficDetectionHourlyPersonFlowRepository
        extends ServiceImpl<TrafficDetectionHourlyPersonFlowMapper, TrafficDetectionHourlyPersonFlowPO> {

    @Transactional(rollbackFor = Exception.class)
    public void saveRecords(List<TrafficDetectionHourlyPersonFlowPO> records) {
        if (CollectionUtils.isEmpty(records)) {
            return;
        }
        this.saveBatch(records);
    }

    public boolean hasRecords() {
        return this.count() > 0;
    }

    public List<TrafficDetectionHourlyPersonFlowPO> findByDeviceIdsAndRecordTimeBetween(Collection<Long> deviceIds,
                                                                                        LocalDateTime begin,
                                                                                        LocalDateTime end
                                                                                       ) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return List.of();
        }
        return this.baseMapper.selectList(Wrappers.<TrafficDetectionHourlyPersonFlowPO>lambdaQuery()
                                                  .in(TrafficDetectionHourlyPersonFlowPO::getDeviceId, deviceIds)
                                                  .ge(begin != null, TrafficDetectionHourlyPersonFlowPO::getRecordTime, begin)
                                                  .lt(end != null, TrafficDetectionHourlyPersonFlowPO::getRecordTime, end)
                                                  .orderByAsc(TrafficDetectionHourlyPersonFlowPO::getRecordTime));
    }

    public Pagination<TrafficDetectionAreaPersonFlowRankVO> findAreaPersonFlowRankPage(TrafficDetectionAreaStatQueryParam param,
                                                                                       Period period,
                                                                                       String granularity,
                                                                                       Integer bucketCount,
                                                                                       String defaultPeakPeriod
                                                                                      ) {
        var page = this.baseMapper.selectAreaPersonFlowRankPage(PagingUtils.convert(param),
                                                                param,
                                                                period.getBegin(),
                                                                period.getEnd(),
                                                                granularity,
                                                                bucketCount,
                                                                defaultPeakPeriod);
        return PagingUtils.convert(page);
    }

    public List<TrafficDetectionAreaPersonFlowRankVO> findAreaPersonFlowRankList(TrafficDetectionAreaStatQueryParam param,
                                                                                 Period period,
                                                                                 String granularity,
                                                                                 Integer bucketCount,
                                                                                 String defaultPeakPeriod
                                                                                ) {
        return this.baseMapper.selectAreaPersonFlowRankList(param,
                                                            period.getBegin(),
                                                            period.getEnd(),
                                                            granularity,
                                                            bucketCount,
                                                            defaultPeakPeriod);
    }
}
