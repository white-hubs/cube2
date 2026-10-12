package com.guhecloud.cube.base.alarm.server.flow.application.querygateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.alarm.server.flow.application.querygateway.query.AlarmReasonConfigQueryParam;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmReasonConfigPO;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmReasonConfigMapper;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmReasonConfigVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmReasonConfigQueryService {

    private final AlarmReasonConfigMapper alarmReasonConfigMapper;

    public Pagination<AlarmReasonConfigVO> queryAlarmReasonPage(AlarmReasonConfigQueryParam queryParam) {
        Page<AlarmReasonConfigPO> page = this.alarmReasonConfigMapper.selectPage(PagingUtils.convert(queryParam), Wrappers
                                                                                         .<AlarmReasonConfigPO>lambdaQuery()
                                                                                         .like(StringUtils.isNotBlank(queryParam.getKeyword()), AlarmReasonConfigPO::getReason, queryParam.getKeyword())
                                                                                         .orderByDesc(AlarmReasonConfigPO::getCreateTime)
                                                                                );
        return PagingUtils.convert(page, AlarmReasonConfigVO.class);
    }

    public List<AlarmReasonConfigVO> queryAlarmReasons(AlarmReasonConfigQueryParam queryParam) {
        List<AlarmReasonConfigPO> alarmReasonConfigPOS = this.alarmReasonConfigMapper.selectList(Wrappers
                                                                                                         .<AlarmReasonConfigPO>lambdaQuery()
                                                                                                         .like(StringUtils
                                                                                                                       .isNotBlank(queryParam
                                                                                                                                           .getKeyword()), AlarmReasonConfigPO::getReason, queryParam
                                                                                                                       .getKeyword())
                                                                                                         .orderByDesc(AlarmReasonConfigPO::getCreateTime)
                                                                                                );
        return ObjectUtils.copying(alarmReasonConfigPOS, AlarmReasonConfigVO.class);
    }
}
