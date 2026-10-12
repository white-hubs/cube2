package com.guhecloud.cube.srv.gen.personnel.management.server.querygateway;

import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo.UserCenterVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.querygateway.query.UserCenterParam;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/6 17:14
 * @Description:
 */
@Service
public interface PersonnelUserCenterService {

    /**
     * 用户中心-用户列表
     *
     * @param userCenterParam
     *
     * @return
     */
    Pagination<UserCenterVO> getUserCenterPage(UserCenterParam userCenterParam);

    /**
     * 用户中心-用户导出
     *
     * @param param
     */
    void exportUsers(@Valid UserCenterParam param);
}
