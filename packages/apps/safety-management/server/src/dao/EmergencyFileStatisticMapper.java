package com.guhecloud.cube.srv.gen.safety.management.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseStatisticsQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.EmergencyFileQuery;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.EmergencyFileClassifyVO;
import com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.EmergencyFileClassify;
import com.guhecloud.cube.srv.gen.safety.management.po.SafetyEmergencyFilePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wql
 * @describe 预案文件
 * @date 2023/10/8 17:46
 */
@Mapper
public interface EmergencyFileStatisticMapper extends BaseMapper<SafetyEmergencyFilePO> {
    List<EmergencyFileClassify> getEmergencyFileStatistic(@Param("query") BaseQuery query);

    List<EmergencyFileClassifyVO> getEmergencyFileClassify();

    Page<SafetyEmergencyFilePO> getSafetyEmergencyFilePage(Page page, @Param("query") EmergencyFileQuery query);

    Integer countNum(@Param("query") BaseQuery cardQuery);

    Integer count(@Param("query") BaseStatisticsQuery query);
}
