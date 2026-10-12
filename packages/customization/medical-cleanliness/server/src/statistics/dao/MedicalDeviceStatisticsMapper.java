package com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.entity.MedicalDeviceStatistics;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.param.MedicalDeviceParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.vo.MedicalDeviceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-01  11:47
 * @Description:
 * @Version: 1.0
 */
@Mapper
public interface MedicalDeviceStatisticsMapper extends BaseMapper<MedicalDeviceStatistics> {
    Page<MedicalDeviceVO> getMedicalDevicePage(@Param("page") Page page, @Param("param") MedicalDeviceParam param);

}
