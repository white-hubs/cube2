package com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.dao.TimeControlLogMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.entity.TimeControlLog;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.param.TimeControlLogQueryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.service.TimeControlLogService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.vo.TimeControlLogVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TimeControlLogServiceImpl extends ServiceImpl<TimeControlLogMapper, TimeControlLog> implements TimeControlLogService {
    private final TimeControlLogMapper timeControlLogMapper;

    @Override
    public Pagination<TimeControlLogVo> getTimeControlLogs(TimeControlLogQueryParam param) {
        Map<String, LocalDateTime> period = param.getDate();
        Page<TimeControlLogVo> timeControlLogs = timeControlLogMapper.getTimeControlLogs(PagingUtils.convert(param), period.get("beginTime"), period.get("endTime"));
        return PagingUtils.convert(timeControlLogs);
    }

    @Override
    public Pagination<TimeControlLogVo> getTimeControlPlan(TimeControlLogQueryParam param) {
        Map<String, LocalDateTime> period = param.getDate();
        Integer timeCycle = null;
        if (ObjectUtils.isNotEmpty(period)) {
            timeCycle = DateUtils.dayOfWeekLegacy(period.get("beginTime"));
        }
        Page<TimeControlLogVo> timeControlPlan = timeControlLogMapper.getTimeControlPlan(PagingUtils.convert(param), period.get("beginTime"), period.get("endTime"), timeCycle.toString());
        return PagingUtils.convert(timeControlPlan);
    }

}
