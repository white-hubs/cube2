package com.guhecloud.cube.srv.gen.business.info.sync.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.business.info.sync.server.controller.vo.OrganizationInfoVO;
import com.guhecloud.cube.srv.gen.business.info.sync.server.entity.Organization;
import com.guhecloud.platform.base.model.Period;

import java.util.List;

/**
 * @author 15561
 * @description 针对表【organization(组织机构信息表)】的数据库操作Service
 * @createDate 2024-12-17 16:11:33
 */
public interface SyncOrganizationService extends IService<Organization> {

    /**
     * 全量获取部门数据
     *
     * @param keyword
     * @param period
     *
     * @return
     */
    List<OrganizationInfoVO> getOrganizationInfoListAll(String keyword, Period period);

}
