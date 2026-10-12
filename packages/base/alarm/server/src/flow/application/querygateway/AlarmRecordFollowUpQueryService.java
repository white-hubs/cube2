package com.guhecloud.cube.base.alarm.server.flow.application.querygateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmRecordFollowUpPO;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmRecordFollowUpMapper;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmRecordFollowUpVO;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.param.BusinessKeysParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 报警跟进信息查询service
 *
 * @author lanchun
 * @date 2024/11/25 15:54
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmRecordFollowUpQueryService {

    private final AlarmRecordFollowUpMapper alarmRecordFollowUpMapper;

    private final FileClient fileClient;

    public List<AlarmRecordFollowUpVO> getAlarmRecordFollowUpByAlarmId(Long alarmRecordId) {
        List<AlarmRecordFollowUpPO> alarmRecordFollowUpPOS = this.alarmRecordFollowUpMapper
                .selectList(Wrappers
                                    .<AlarmRecordFollowUpPO>lambdaQuery()
                                    .eq(AlarmRecordFollowUpPO::getAlarmRecordId, alarmRecordId)
                                    .orderByDesc(AlarmRecordFollowUpPO::getFollowUpTime)
                           );
        if (CollectionUtils.isEmpty(alarmRecordFollowUpPOS)) {
            return Collections.emptyList();
        }
        List<Long> ids = alarmRecordFollowUpPOS.stream().map(AlarmRecordFollowUpPO::getId).collect(Collectors.toList());
        List<AlarmRecordFollowUpVO> alarmRecordFollowUpVOS = ObjectUtils.copying(alarmRecordFollowUpPOS, AlarmRecordFollowUpVO.class);
        BusinessKeysParam businessKeysParam = new BusinessKeysParam();
        businessKeysParam.setBusinessKeys(ids);
        Map<Long, List<FileVO>> byBusinessKeys = this.fileClient.getByBusinessKeys(businessKeysParam);
        alarmRecordFollowUpVOS.forEach(alarmRecordFollowUpVO -> {
            if (byBusinessKeys.containsKey(alarmRecordFollowUpVO.getId())) {
                alarmRecordFollowUpVO.setFiles(byBusinessKeys.get(alarmRecordFollowUpVO.getId()));
            }
        });
        return alarmRecordFollowUpVOS;
    }
}
