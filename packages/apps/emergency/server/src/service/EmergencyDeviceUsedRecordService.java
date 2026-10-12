package com.guhecloud.cube.biz.emergency.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyDeviceUsedRecord;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyDeviceUsedRecordParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyDeviceUsedRecordQueryParam;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyDeviceUsedRecordVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：应急设备使用记录服务层
 *
 * @author Appet
 * @date 2024-01-26 10:08:36
 */
public interface EmergencyDeviceUsedRecordService extends IService<EmergencyDeviceUsedRecord> {

    Long add(EmergencyDeviceUsedRecordParam param);

    void update(Long id, EmergencyDeviceUsedRecordParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    EmergencyDeviceUsedRecordVO getById(Long id);

    Pagination<EmergencyDeviceUsedRecordVO> getPage(EmergencyDeviceUsedRecordQueryParam param);

}