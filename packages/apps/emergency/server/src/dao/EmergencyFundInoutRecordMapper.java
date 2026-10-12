package com.guhecloud.cube.biz.emergency.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyFundInoutRecord;
import com.guhecloud.cube.biz.emergency.server.param.CommonStatParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyFundInoutRecordQueryParam;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFundInoutStatVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：应急资金收支记录 Repository接口
 *
 * @author Appet
 * @date 2024-01-22 10:08:36
 */
@Repository
public interface EmergencyFundInoutRecordMapper extends BaseMapper<EmergencyFundInoutRecord> {

    List<EmergencyFundInoutStatVO> fundInoutStatByDate(Page page, @Param("param") EmergencyFundInoutRecordQueryParam param);

    List<EmergencyFundInoutStatVO> fundInoutTrendStatByDate(@Param("param") CommonStatParam param);
}