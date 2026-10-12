package com.guhecloud.cube.biz.emergency.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyUserCategory;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyUserCategoryParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyUserCategoryQueryParam;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyUserCategoryVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：应急人员分类管理服务层
 *
 * @author Appet
 * @date 2024-01-22 10:08:36
 */
public interface EmergencyUserCategoryService extends IService<EmergencyUserCategory> {

    Long add(EmergencyUserCategoryParam param);

    void update(Long id, EmergencyUserCategoryParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    EmergencyUserCategoryVO getById(Long id);

    Pagination<EmergencyUserCategoryVO> getPage(EmergencyUserCategoryQueryParam param);

}