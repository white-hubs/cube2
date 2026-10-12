package com.guhecloud.cube.biz.ouaa.server.permission.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.permission.common.po.PermissionPO;
import com.guhecloud.cube.biz.ouaa.server.permission.common.request.PermissionModify;
import com.guhecloud.cube.biz.ouaa.server.permission.common.vo.PermissionVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface PermissionService extends IService<PermissionPO> {

    Long save(PermissionModify permissionModify);

    void update(Long id, PermissionModify permissionModify);

    void remove(Long id);

    List<PermissionVO> getPermissions(String keyword);

    void export(HttpServletRequest req, HttpServletResponse resp);
}
