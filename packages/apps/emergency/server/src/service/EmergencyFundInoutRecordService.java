package com.guhecloud.cube.biz.emergency.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyFundInoutRecord;
import com.guhecloud.cube.biz.emergency.server.param.CommonStatParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyFundInoutRecordParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyFundInoutRecordQueryParam;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFundInoutRecordVO;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFundInoutStatVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：应急资金收支记录服务层
 *
 * @author Appet
 * @date 2024-01-22 10:08:36
 */
public interface EmergencyFundInoutRecordService extends IService<EmergencyFundInoutRecord> {

    Long add(EmergencyFundInoutRecordParam param);

    void update(Long id, EmergencyFundInoutRecordParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    EmergencyFundInoutRecordVO getById(Long id);

    Pagination<EmergencyFundInoutRecordVO> getPage(EmergencyFundInoutRecordQueryParam param);

    List<EmergencyFundInoutStatVO> fundInoutStatByDate(EmergencyFundInoutRecordQueryParam param);

    List<EmergencyFundInoutStatVO> fundInoutTrendStatByDate(CommonStatParam param);
}