package com.guhecloud.cube.base.alarm.server.flow.application.querygateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.alarm.server.flow.domain.po.AlarmLevelPlayConfigPO;
import com.guhecloud.cube.base.alarm.server.flow.infrastructure.repository.mapper.AlarmLevelPlayConfigMapper;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmLevelPlayConfigVO;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.param.BusinessKeysParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmLevelPlayConfigQueryService {

    private final FileClient fileClient;

    private final AlarmLevelPlayConfigMapper alarmLevelPlayConfigMapper;

    public List<AlarmLevelPlayConfigVO> getAlarmLevelPlayConfigs() {
        List<AlarmLevelPlayConfigPO> levelPlayConfigPOS = this.alarmLevelPlayConfigMapper.selectList(null);
        if (CollectionUtils.isEmpty(levelPlayConfigPOS)) {
            return Collections.emptyList();
        }
        List<AlarmLevelPlayConfigVO> alarmLevelPlayConfigVOS = ObjectUtils.copying(levelPlayConfigPOS, AlarmLevelPlayConfigVO.class
                                                                                  );
        extend(alarmLevelPlayConfigVOS);
        return alarmLevelPlayConfigVOS;
    }

    public AlarmLevelPlayConfigVO getAlarmLevelPlayConfig(String alarmLevelCode) {
        AlarmLevelPlayConfigPO alarmLevelPlayConfigPO = this.alarmLevelPlayConfigMapper.selectOne(Wrappers
                                                                                                          .<AlarmLevelPlayConfigPO>lambdaQuery()
                                                                                                          .eq(AlarmLevelPlayConfigPO::getAlarmLevelCode, alarmLevelCode)
                                                                                                 );
        if (Objects.isNull(alarmLevelPlayConfigPO)) {
            return null;
        }
        AlarmLevelPlayConfigVO alarmLevelPlayConfigVO = ObjectUtils.copying(alarmLevelPlayConfigPO, AlarmLevelPlayConfigVO.class
                                                                           );
        extend(Collections.singletonList(alarmLevelPlayConfigVO));
        return alarmLevelPlayConfigVO;
    }

    private void extend(List<AlarmLevelPlayConfigVO> alarmLevelPlayConfigVOS) {
        List<Long> ids = alarmLevelPlayConfigVOS.stream()
                .map(AlarmLevelPlayConfigVO::getId)
                .collect(Collectors.toList());
        BusinessKeysParam param = new BusinessKeysParam();
        param.setBusinessKeys(ids);
        Map<Long, List<FileVO>> fileMap = this.fileClient.getByBusinessKeys(param);
        alarmLevelPlayConfigVOS.forEach(alarmLevelPlayConfigVO -> {
            if (fileMap.containsKey(alarmLevelPlayConfigVO.getId())) {
                List<FileVO> fileVOS = fileMap.get(alarmLevelPlayConfigVO.getId());
                alarmLevelPlayConfigVO.setFile(fileVOS.get(0));
            }
        });
    }
}
