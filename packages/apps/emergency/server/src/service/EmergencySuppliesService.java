package com.guhecloud.cube.biz.emergency.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencySupplies;
import com.guhecloud.cube.biz.emergency.server.param.EmergencySuppliesParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencySuppliesQueryParam;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencySuppliesStatVO;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencySuppliesVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：应急物资管理服务层
 *
 * @author Appet
 * @date 2024-01-016 10:08:36
 */
public interface EmergencySuppliesService extends IService<EmergencySupplies> {

    Long add(EmergencySuppliesParam param);

    void update(Long id, EmergencySuppliesParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    EmergencySuppliesVO getById(Long id);

    Pagination<EmergencySuppliesVO> getPage(EmergencySuppliesQueryParam param);

    List<EmergencySuppliesStatVO> getEmergencySuppliesStatForStock();
}