package com.guhecloud.cube.biz.meattrade.server.base.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.meattrade.server.base.dao.TallyOperationLogMapper;
import com.guhecloud.cube.biz.meattrade.server.base.entity.TallyOperationLog;
import com.guhecloud.cube.biz.meattrade.server.base.param.TallyOperationLogCreateParam;
import com.guhecloud.cube.biz.meattrade.server.base.param.TallyOperationLogQueryParam;
import com.guhecloud.cube.biz.meattrade.server.base.service.TallyOperationLogService;
import com.guhecloud.cube.biz.meattrade.server.utlis.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author lzy
 * @date 2021/9/9
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TallyOperationLogServiceImpl extends ServiceImpl<TallyOperationLogMapper, TallyOperationLog> implements TallyOperationLogService {

    @Override
    public void save(TallyOperationLogCreateParam param) {
        TallyOperationLog tallyOperationLog = ObjectUtils.copying(param, TallyOperationLog.class);
        this.save(tallyOperationLog);
    }

    @Override
    public Page<TallyOperationLog> page(TallyOperationLogQueryParam queryParam) {
        if (Objects.nonNull(queryParam.getEndTime())) {
            queryParam.setStartTime(DateUtils.getMidTime(queryParam.getStartTime()));
            queryParam.setEndTime(DateUtils.addDays(DateUtils.getMidTime(queryParam.getEndTime()), 1));
        }
        return this.lambdaQuery()
                .eq(TallyOperationLog::getDeviceId, queryParam.getDeviceId())
                .between(Objects.nonNull(queryParam.getEndTime()) && Objects.nonNull(queryParam.getStartTime()),
                         TallyOperationLog::getTime, queryParam.getStartTime(), queryParam.getEndTime())
                .page(PagingUtils.convert(queryParam));
    }
}
