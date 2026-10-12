package com.guhecloud.cube.biz.ouaa.server.permission.common.controller;

import com.guhecloud.cube.biz.ouaa.server.permission.common.request.PermissionModify;
import com.guhecloud.cube.biz.ouaa.server.permission.common.service.PermissionService;
import com.guhecloud.cube.biz.ouaa.server.permission.common.vo.PermissionVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 权限管理
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping("/permission")
    public Long save(@Valid @RequestBody PermissionModify permissionModify) {
        return this.permissionService.save(permissionModify);
    }

    @PutMapping("/permission/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody PermissionModify permissionModify) {
        this.permissionService.update(id, permissionModify);
    }

    @DeleteMapping("/permission/{id}")
    public void remove(@PathVariable("id") Long id) {
        this.permissionService.remove(id);
    }

    @GetMapping("/permissions")
    public List<PermissionVO> getPermissions(@RequestParam(required = false) String keyword) {
        return this.permissionService.getPermissions(keyword);
    }

    /**
     * 导出权限
     */
    @GetMapping("/permission/export")
    public void export(HttpServletRequest req, HttpServletResponse resp) {
        //导出
        permissionService.export(req, resp);
    }
}
