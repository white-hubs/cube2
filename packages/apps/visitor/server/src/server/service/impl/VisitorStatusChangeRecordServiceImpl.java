package com.guhecloud.cube.srv.gen.visitor.management.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.visitor.management.server.enums.ReservationStatusEnum;
import com.guhecloud.cube.srv.gen.visitor.management.server.mapper.VisitorStatusChangeRecordMapper;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.VisitorStatusChangeRecordPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.service.VisitorStatusChangeRecordService;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.VisitorStatusChangeRecordParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.VisitorStatusChangeRecordResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 访客状态变更记录
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VisitorStatusChangeRecordServiceImpl implements VisitorStatusChangeRecordService {

    private final VisitorStatusChangeRecordMapper visitorStatusChangeRecordMapper;

    /**
     * 访客通行趋势
     *
     * @param param 条件
     *
     * @return 趋势
     */
    @Override
    public List<VisitorStatusChangeRecordResult> visitorPassTrend(VisitorStatusChangeRecordParam param) {
        List<String> statusList = new ArrayList<>();
        statusList.add(String.valueOf(ReservationStatusEnum.ON_THE_VISIT.getStatus()));
        statusList.add(String.valueOf(ReservationStatusEnum.DEPARTURE.getStatus()));
        return visitorStatusChangeRecordMapper.visitorPassTrend(Wrappers.<VisitorStatusChangeRecordPO>lambdaQuery()
                                                                        .ge(VisitorStatusChangeRecordPO::getCreateTime, param.getPeriod()
                                                                                .getBegin())
                                                                        .le(VisitorStatusChangeRecordPO::getCreateTime, param.getPeriod()
                                                                                .getEnd())
                                                                        .in(VisitorStatusChangeRecordPO::getStatus, statusList)
                                                               );
    }
}
