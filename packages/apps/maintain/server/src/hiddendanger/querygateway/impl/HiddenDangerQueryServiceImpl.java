package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.querygateway.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.TicketStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.fault.dto.QueryDTO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.PageVO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway.query.PeriodQuery;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.dao.HiddenDangerNewMapper;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo.HazardStatisticsTopVO;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo.HazardStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po.HiddenDangerNew;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.querygateway.HiddenDangerQueryService;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lzy
 * @date 2022/4/26
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HiddenDangerQueryServiceImpl extends ServiceImpl<HiddenDangerNewMapper, HiddenDangerNew> implements HiddenDangerQueryService {

    private final HiddenDangerNewMapper hiddenDangerMapper;

    @Override
    public Integer isHiddenDangerValid(String deviceId, Period period) {
        return hiddenDangerMapper.isFaultValid(deviceId, DateUtils.format(period.getBegin()), DateUtils.format(period.getEnd()));
    }

    @Override
    public Page<PageVO> getHiddenDangerCount(QueryDTO query) {
        return hiddenDangerMapper.getHiddenDangerCount(PagingUtils.convert(query), query.getNum());
    }

    @Override
    public HazardStatisticsVO getHazardStatistics(PeriodQuery period) {
        HazardStatisticsVO statistics = new HazardStatisticsVO();
        statistics.setData(hiddenDangerMapper.getHazardStatisticsByType(period.getPeriod()));
        AtomicInteger total = new AtomicInteger(0);
        AtomicInteger afoot = new AtomicInteger(0);
        AtomicInteger end = new AtomicInteger(0);
        List<HazardStatisticsTopVO> data = hiddenDangerMapper.getHazardStatistics(period.getPeriod());
        data.forEach(ex -> {
            if (TicketStatusEnum.FINISHED.name().equals(ex.getName())) {
                end.set(end.get() + ex.getValue());
            } else {
                afoot.set(afoot.get() + ex.getValue());
            }
            total.set(total.get() + ex.getValue());
        });
        statistics.setTotal(total.get());
        statistics.setAfoot(afoot.get());
        statistics.setEnd(end.get());
        return statistics;
    }
}
