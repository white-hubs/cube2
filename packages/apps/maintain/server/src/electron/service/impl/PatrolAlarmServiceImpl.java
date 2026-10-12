package com.guhecloud.cube.srv.gen.maintain.server.electron.service.impl;

import com.guhecloud.platform.base.util.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.electron.dao.PatrolAlarmMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolAlarm;
import com.guhecloud.cube.srv.gen.maintain.server.electron.param.PatrolAlarmParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolAlarmService;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.PatrolAlarmVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 巡更报警
 *
 * @author : appet
 * @date : 2023-10-19
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PatrolAlarmServiceImpl extends ServiceImpl<PatrolAlarmMapper, PatrolAlarm> implements PatrolAlarmService {
    private final PatrolAlarmMapper patrolAlarmMapper;

    @Override
    public Pagination<PatrolAlarmVO> getPage(PatrolAlarmParam param) {
        Page<PatrolAlarm> page = patrolAlarmMapper.selectPage(PagingUtils.convert(param), Wrappers.<PatrolAlarm>lambdaQuery()
                .and(StringUtils.isNotBlank(param.getKeyword()), q ->
                        q.like(PatrolAlarm::getAlarmContent, param.getKeyword()))
                .or().like(PatrolAlarm::getAlarmPoint, param.getKeyword()));
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            List<PatrolAlarmVO> list = page.getRecords().stream().map(d -> {
                PatrolAlarmVO alarm = ObjectUtils.copying(d, PatrolAlarmVO.class);
                if (StringUtils.isBlank(alarm.getAlarmResult())) {
                    alarm.setAlarmResult("未处理");
                }
                return alarm;
            }).collect(Collectors.toList());
            return PagingUtils.convert(page, list);
        }
        return Pagination.empty();
    }
}
