package com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.entity.MedicalIotRealTimeData;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.param.MedicalIotRealTimeDataParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.vo.MedicalIotRealTimeDataVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: lijie
 * @CreateTime: 2024-10-21  17:38
 * @Description:
 * @Version: 1.0
 */
@Mapper
public interface MedicalIotRealTimeDataMapper extends BaseMapper<MedicalIotRealTimeData> {
    @InterceptorIgnore(tenantLine = "true")
    Page<MedicalIotRealTimeDataVO> getRealTimeDataPage(@Param("page") Page page, @Param("param") MedicalIotRealTimeDataParam param,
                                                       @Param("organizationIds") Set<Long> organizationIds
                                                      );

    @InterceptorIgnore(tenantLine = "true")
    List<MedicalIotRealTimeDataVO> getRealTimeDataNorm(@Param("param") MedicalIotRealTimeDataParam param, @Param("ids") List<Long> ids,
                                                       @Param("deviceIds") List<Long> deviceIds
                                                      );

    /**
     * 状态统计
     */
    List<Map<String, Long>> statusStatistics(@Param("organizationIds") Set<Long> organizationIds, @Param("deviceIds") List<Long> deviceIds);
}
