package com.guhecloud.cube.biz.emergency.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencySuppliesInoutRecord;
import com.guhecloud.cube.biz.emergency.server.param.EmergencySuppliesInoutRecordParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencySuppliesInoutRecordQueryParam;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencySuppliesInoutRecordVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：应急物资出入库记录服务层
 *
 * @author Appet
 * @date 2024-01-016 10:08:36
 */
public interface EmergencySuppliesInoutRecordService extends IService<EmergencySuppliesInoutRecord> {

    Long add(EmergencySuppliesInoutRecordParam param);

    void update(Long id, EmergencySuppliesInoutRecordParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    EmergencySuppliesInoutRecordVO getById(Long id);

    Pagination<EmergencySuppliesInoutRecordVO> getPage(EmergencySuppliesInoutRecordQueryParam param);

}