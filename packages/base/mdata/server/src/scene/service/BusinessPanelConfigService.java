package com.guhecloud.cube.biz.mdata.server.scene.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.mdata.server.scene.entity.BusinessPanelConfig;
import com.guhecloud.cube.biz.mdata.server.scene.enumeration.BusinessPanelPositionEnum;
import com.guhecloud.cube.biz.mdata.server.scene.param.BusinessPanelConfigModifyParam;

import java.util.List;

public interface BusinessPanelConfigService extends IService<BusinessPanelConfig> {

    Long save(BusinessPanelConfigModifyParam param);

    void saveBatch(List<BusinessPanelConfigModifyParam> params);

    BusinessPanelConfig getByBusinessId(Long projectId, Long businessId);

    BusinessPanelConfig getByBusinessId(Long projectId, Long businessId, BusinessPanelPositionEnum panelPosition);

    List<BusinessPanelConfig> listByBusinessId(Long projectId, Long businessId);

    List<BusinessPanelConfig> listByBusinessIds(Long projectId, List<Long> businessIds);

    void remove(Long projectId, Long businessId);

    void removeByBusinessId(Long projectId, Long businessId);

    void remove(Long projectId, Long businessId, BusinessPanelPositionEnum panelPosition);
}
