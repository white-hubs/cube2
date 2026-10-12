package com.guhecloud.cube.srv.gen.securitymanagement.server.information.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.SecurityDeviceInfoVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.dto.InformationPlanDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.entity.InformationPlanEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.param.InformationPlanParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.vo.InformationPlanVO;
import com.guhecloud.platform.base.model.Pagination;

public interface InformationPlanService extends IService<InformationPlanEntity> {

    void saveInformationPlan(InformationPlanDTO informationPlanDTO);

    InformationPlanDTO getMaterialsByProgramIds(Long id);

    Pagination<InformationPlanVO> getInformationPlanPage(InformationPlanParam param);

    void deleteInformationPlan(Long id);

    void updateInformationPlanStatus(Long id, Integer isEnable);

    Pagination<SecurityDeviceInfoVO> getUnusedDevicePage(InformationPlanParam param);

    void startPlan(String taskId);

    void stopPlan(String taskId);
}
