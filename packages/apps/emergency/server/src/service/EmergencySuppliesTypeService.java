package com.guhecloud.cube.biz.emergency.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencySuppliesType;
import com.guhecloud.cube.biz.emergency.server.param.EmergencySuppliesTypeParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencySuppliesTypeQueryParam;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencySuppliesTypeVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：应急物资类型管理服务层
 *
 * @author Appet
 * @date 2024-01-016 10:08:36
 */
public interface EmergencySuppliesTypeService extends IService<EmergencySuppliesType> {

    Long add(EmergencySuppliesTypeParam param);

    void update(Long id, EmergencySuppliesTypeParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    EmergencySuppliesTypeVO getById(Long id);

    Pagination<EmergencySuppliesTypeVO> getPage(EmergencySuppliesTypeQueryParam param);

}