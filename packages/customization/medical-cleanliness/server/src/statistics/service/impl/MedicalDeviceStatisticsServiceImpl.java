package com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.dao.MedicalDeviceStatisticsMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.entity.MedicalDeviceStatistics;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.param.MedicalDeviceParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.service.MedicalDeviceStatisticsService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.vo.MedicalDeviceVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-01  11:43
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MedicalDeviceStatisticsServiceImpl extends ServiceImpl<MedicalDeviceStatisticsMapper, MedicalDeviceStatistics> implements MedicalDeviceStatisticsService {
    @Override
    public Boolean addMedicalDevice(List<MedicalDeviceStatistics> params) {
        return this.saveBatch(params);
    }

    @Override
    public Pagination<MedicalDeviceVO> getMedicalDevicePage(MedicalDeviceParam param) {
        Page<MedicalDeviceVO> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = this.baseMapper.getMedicalDevicePage(page, param);
        return PagingUtils.convert(page);
    }
}
