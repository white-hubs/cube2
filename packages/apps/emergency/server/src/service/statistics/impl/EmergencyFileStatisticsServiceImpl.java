package com.guhecloud.cube.biz.emergency.server.service.statistics.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyFile;
import com.guhecloud.cube.biz.emergency.server.param.statistics.EmergencyFileStatisticsParam;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyFileService;
import com.guhecloud.cube.biz.emergency.server.service.statistics.EmergencyFileStatisticsService;
import com.guhecloud.cube.biz.emergency.server.vo.statistics.EmergencyFileStatisticsVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyFileStatisticsServiceImpl implements EmergencyFileStatisticsService {

    private final EmergencyFileService emergencyFileService;

    @Override
    public EmergencyFileStatisticsVO getEmergencyFileStatistics(EmergencyFileStatisticsParam param) {
        long totalNum = this.emergencyFileService.count(Wrappers.<EmergencyFile>lambdaQuery()
                                                                .between(param.getPeriod() !=
                                                                         null, EmergencyFile::getCreateTime, ObjectUtils.ifNull(param.getPeriod(), p -> p.getBegin()), ObjectUtils.ifNull(param.getPeriod(), p -> p.getEnd()))
                                                       );
        EmergencyFileStatisticsVO emergencyFileStatisticsVO = new EmergencyFileStatisticsVO();
        emergencyFileStatisticsVO.setTotalNum(totalNum);
        return emergencyFileStatisticsVO;
    }
}
