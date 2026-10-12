package com.guhecloud.cube.biz.emergency.server.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyFile;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyFileQueryParam;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFileDataCollectVO;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFileEventTypeVO;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFileEventTypeVO1;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFilePageVO;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmergencyFileMapper extends BaseMapper<EmergencyFile> {

    @InterceptorIgnore(tenantLine = "on")
    List<EmergencyFileDataCollectVO> safetyManagementDataCollect(@Param("period") Period period);

    List<EmergencyFileEventTypeVO> selectEmergencyFileEventType(@Param("period") Period period);

    List<EmergencyFileEventTypeVO1> selectNoSetEmergencyFileEventType(@Param("eventTypeCodeList") List<String> eventTypeCodeList);

    Page<EmergencyFilePageVO> selectEmergencyFilePage(Page page, @Param("param") EmergencyFileQueryParam param);
}
