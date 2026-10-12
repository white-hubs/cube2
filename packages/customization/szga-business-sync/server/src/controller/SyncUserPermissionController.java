package com.guhecloud.cube.srv.gen.business.info.sync.server.controller;

import com.guhecloud.cube.srv.gen.business.info.sync.server.controller.vo.UserPermissionVO;
import com.guhecloud.cube.srv.gen.business.info.sync.server.service.SyncUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/11 16:13
 * @Description: 用户权限接口
 */

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SyncUserPermissionController {
    private final SyncUserService syncUserService;

    /**
     * 查询指定用户权限信息
     *
     * @param mobile 关键字 用户id
     */
    @GetMapping("/user/permission/mobile")
    public UserPermissionVO getUserPermissionByMobile(@RequestParam(value = "mobile") String mobile) {
        return syncUserService.getUserPermissionById(mobile);
    }

    /**
     * 修改用户权限
     */
    @PostMapping("/user/permission/modify")
    public Boolean userPermissionModify(@RequestBody UserPermissionVO userPermissionVO) {
        return syncUserService.userPermissionModify(userPermissionVO);
    }
}
