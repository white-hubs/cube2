package com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.entity.MedicalAlarmStatistics;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.dao.MedicalAlarmStatisticsMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.param.MedicalDeviceParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.service.MedicalAlarmStatisticsService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.vo.MedicalAlarmVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-01  11:45
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MedicalAlarmStatisticsServiceImpl extends ServiceImpl<MedicalAlarmStatisticsMapper, MedicalAlarmStatistics> implements MedicalAlarmStatisticsService {
    @Override
    public Pagination<MedicalAlarmVO> getMedicalAlarmPage(MedicalDeviceParam param) {
        Page<MedicalAlarmVO> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = this.baseMapper.getMedicalAlarmPage(page, param);
        return PagingUtils.convert(page);
    }

    @Override
    public Boolean addMedicalAlarm(List<MedicalAlarmStatistics> params) {
        return this.saveBatch(params);
    }
}
