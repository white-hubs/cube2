package com.guhecloud.cube.srv.gen.safety.management.service;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.EmergencyFileQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.EmergencyFileClassifyVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.SafetyEmergencyFileVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.EmergencyFileStatisticsVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * @author wql
 * @describe 预案文件
 * @date 2023/10/8 17:45
 */
public interface EmergencyFileStatisticService {
    EmergencyFileStatisticsVO getEmergencyFileStatistic(BaseQuery query);

    Pagination<SafetyEmergencyFileVO> getEmergencyFileList(EmergencyFileQuery query);

    List<EmergencyFileClassifyVO> getEmergencyFileClassify();

    Integer countNum(BaseQuery cardQuery);

    Integer count(BaseStatisticsQuery query);
}
