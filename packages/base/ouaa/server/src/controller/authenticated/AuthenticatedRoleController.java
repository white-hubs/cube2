package com.guhecloud.cube.biz.ouaa.server.controller.authenticated;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.ouaa.server.dto.RoleDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.Role;
import com.guhecloud.cube.biz.ouaa.server.entity.Terminal;
import com.guhecloud.cube.biz.ouaa.server.param.RoleQueryParam;
import com.guhecloud.cube.biz.ouaa.server.service.RoleService;
import com.guhecloud.cube.biz.ouaa.server.service.TerminalService;
import com.guhecloud.cube.biz.ouaa.server.vo.RoleVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 角色管理接口
 *
 * @author Woody
 * @date 2021/7/17
 */
@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@PreAuthorize("isAuthenticated()")
public class AuthenticatedRoleController {

    private final RoleService roleService;
    private final TerminalService terminalService;

    /**
     * 获取角色信息
     *
     * @return 角色列表
     */
    @PostMapping("/authenticated/post/role/map")
    public Map<Long, RoleDTO> postRoleMap() {
        List<Role> roles = this.roleService.listProductRoles();
        return Optional.ofNullable(roles).orElse(Collections.emptyList()).stream()
                .map(role -> RoleDTO.builder().id(role.getId()).name(role.getName()).icon(role.getIcon()).sort(role.getSort())
                        .enabled(role.getEnabled()).builtin(role.getBuiltin()).remark(role.getRemark()).build())
                .collect(Collectors.toMap(RoleDTO::getId, r -> r, (o, n) -> n));
    }

    /**
     * 获取角色信息
     *
     * @return 角色列表
     */
    @GetMapping("/authenticated/role/list-by-product")
    public List<RoleDTO> listRoles(@RequestParam(required = false) String keyword) {
        // 获取访问端主键
        Terminal terminal = this.terminalService.getByCode(SessionContextHolder.getRequester().getTerminal());
        Assert.isNotFound(terminal, "不存在访问端信息!");
        List<Role> roles = this.roleService.listProductRoles();
        return ObjectUtils.copying(roles, RoleDTO.class);
    }

    /**
     * 获取角色分页信息（内部调用）
     *
     * @param paging 分页参数
     *
     * @return 角色信息
     */
    @GetMapping("/authenticated/role/list-by-product/page")
    public Pagination<RoleDTO> pageRoles(@SpringQueryMap Paging paging) {
        // 获取访问端主键
        Terminal terminal = this.terminalService.getByCode(SessionContextHolder.getRequester().getTerminal());
        Assert.isNotFound(terminal, "不存在访问端信息!");
        Page<Role> page = this.roleService.pageProductRoles(paging);
        return PagingUtils.convert(page, RoleDTO.class);
    }

    /**
     * 获取角色分页信息
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/authenticated/product/role/page")
    public Pagination<RoleVO> pages(@Valid RoleQueryParam param) {
        // 获取访问端主键
        Terminal terminal = this.terminalService.getByCode(SessionContextHolder.getRequester().getTerminal());
        Assert.isNotFound(terminal, "不存在访问端信息!");
        param.setProductId(terminal.getProductId());
        Page<Role> page = this.roleService.listRoles(param);
        return PagingUtils.convert(page, RoleVO.class);
    }
}
