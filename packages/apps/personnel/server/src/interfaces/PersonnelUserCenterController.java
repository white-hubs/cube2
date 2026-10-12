package com.guhecloud.cube.srv.gen.personnel.management.server.interfaces;

import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo.UserCenterVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.querygateway.PersonnelUserCenterService;
import com.guhecloud.cube.srv.gen.personnel.management.server.querygateway.query.UserCenterParam;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/6 17:07
 * 用户中心API
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/user-center")
public class PersonnelUserCenterController {
    private final PersonnelUserCenterService personnelUserCenterService;

    /**
     * 用户中心列表（员工信息+访客信息）
     *
     * @return
     */
    @GetMapping("/page")
    public Pagination<UserCenterVO> getUserCenterPage(UserCenterParam userCenterParam) {
        return personnelUserCenterService.getUserCenterPage(userCenterParam);
    }

    /**
     * 用户导出
     *
     * @param param 筛选条件
     */
    @GetMapping("/users/export")
    public void export(@SpringQueryMap @Valid UserCenterParam param) {
        personnelUserCenterService.exportUsers(param);
    }
}
