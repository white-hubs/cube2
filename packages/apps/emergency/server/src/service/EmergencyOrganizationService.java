package com.guhecloud.cube.biz.emergency.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyOrganization;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyOrganizationParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyOrganizationQueryParam;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyOrganizationVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：应急组织管理服务层
 *
 * @author Appet
 * @date 2024-01-22 10:08:36
 */
public interface EmergencyOrganizationService extends IService<EmergencyOrganization> {

    Long add(EmergencyOrganizationParam param);

    void update(Long id, EmergencyOrganizationParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    EmergencyOrganizationVO getById(Long id);

    Pagination<EmergencyOrganizationVO> getPage(EmergencyOrganizationQueryParam param);

}