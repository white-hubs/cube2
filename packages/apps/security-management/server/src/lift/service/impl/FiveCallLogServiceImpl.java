package com.guhecloud.cube.srv.gen.securitymanagement.server.lift.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lift.dao.FiveCallLogMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lift.entity.FiveCallLog;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lift.param.FiveCallLogParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lift.service.FiveCallLogService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lift.vo.FiveCallLogVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 五方通话记录服务
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FiveCallLogServiceImpl extends ServiceImpl<FiveCallLogMapper, FiveCallLog> implements FiveCallLogService {

    /**
     * 五方通话记录分页列表
     *
     * @param param 条件入参
     *
     * @return 分页列表
     */
    @Override
    public Pagination<FiveCallLogVo> fiveCallLogPage(FiveCallLogParam param) {
        Period period = param.getPeriod();
        boolean periodBool = period != null && period.getBegin() != null && period.getEnd() != null;
        Page<FiveCallLog> page = this.page(PagingUtils.convert(param), Wrappers.<FiveCallLog>lambdaQuery()
                                                   .between(periodBool, FiveCallLog::getCallTime, ObjectUtils.ifNull(period, Period::getBegin), ObjectUtils.ifNull(period, Period::getEnd))
                                                   .orderByDesc(FiveCallLog::getCallTime, FiveCallLog::getId)
                                          );

        return PagingUtils.convert(page, FiveCallLogVo.class);
    }
}
