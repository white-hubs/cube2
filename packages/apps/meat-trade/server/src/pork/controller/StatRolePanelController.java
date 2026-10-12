package com.guhecloud.cube.biz.meattrade.server.pork.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.StatRolePanel;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.StatRolePanelEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.param.StatRolePanelModifyParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.StatRolePanelQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.service.StatRolePanelService;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.StatRolePanelVO;
import com.guhecloud.cube.biz.ouaa.client.RoleClient;
import com.guhecloud.cube.biz.ouaa.client.dto.RoleDTO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 猪肉滑轨数据看板权限接口
 *
 * @author lzy
 * @date 2021/11/22
 */
@Slf4j
@RestController("meattrade-statRolePanelController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StatRolePanelController {

    private final StatRolePanelService statRolePanelService;
    private final RoleClient roleClient;

    /**
     * 面板角色权限新增
     *
     * @param param 新增参数
     */
    @PreAuthorize("hasAuthority('meattrade:role-panel:save')")
    @PostMapping("/stat/role-panel")
    public void save(@Valid @RequestBody StatRolePanelModifyParam param) {
        this.statRolePanelService.save(param);
    }

    /**
     * 面板角色权限编辑
     *
     * @param param 编辑参数
     */
    @PreAuthorize("hasAuthority('meattrade:role-panel:update')")
    @PutMapping("/stat/role-panel")
    public void update(@Valid @RequestBody StatRolePanelModifyParam param) {
        this.statRolePanelService.update(param);
    }

    /**
     * 猪肉滑轨看板数据权限分页信息
     *
     * @param queryParam 查询参数
     *
     * @return 分页信息
     */
    @PreAuthorize("hasAuthority('meattrade:role-panel:page')")
    @GetMapping("/stat/role-panel/page")
    public Pagination<StatRolePanelVO> page(@Valid StatRolePanelQueryParam queryParam) {
        // 获取角色列表
        List<RoleDTO> roleDTOS = this.roleClient.listRoles();
        if (CollectionUtils.isEmpty(roleDTOS)) {
            return Pagination.empty();
        }
        List<Long> roleIds = roleDTOS.stream().map(RoleDTO::getId).collect(Collectors.toList());
        Map<Long, RoleDTO> roleDTOMap = roleDTOS.stream().collect(Collectors.toMap(RoleDTO::getId, roleDTO -> roleDTO, (o, n) -> n));
        queryParam.setRoleIds(roleIds);
        Page<StatRolePanel> statRolePanelPage = this.statRolePanelService.listByPage(queryParam);
        if (CollectionUtils.isEmpty(statRolePanelPage.getRecords())) {
            return Pagination.empty();
        }
        Map<Long, List<StatRolePanel>> statRolePanelMap = this.statRolePanelService.listByRoles(roleIds)
                .stream().collect(Collectors.groupingBy(StatRolePanel::getRoleId));
        List<StatRolePanelVO> statRolePanelVOS = statRolePanelPage.getRecords().stream().map(statRolePanel -> {
            StatRolePanelVO statRolePanelVO = new StatRolePanelVO();
            statRolePanelVO.setRoleId(statRolePanel.getRoleId());
            if (roleDTOMap.containsKey(statRolePanel.getRoleId())) {
                RoleDTO roleDTO = roleDTOMap.get(statRolePanel.getRoleId());
                statRolePanelVO.setRoleName(roleDTO.getName());
            }
            if (statRolePanelMap.containsKey(statRolePanel.getRoleId())) {
                List<StatRolePanel> statRolePanels = statRolePanelMap.get(statRolePanel.getRoleId());
                statRolePanelVO.setEnabled(statRolePanels.stream().anyMatch(StatRolePanel::getEnabled));
                statRolePanelVO.setPanelTypes(statRolePanels.stream().map(StatRolePanel::getPanelType).collect(Collectors.toList()));
            }
            return statRolePanelVO;
        }).collect(Collectors.toList());
        return PagingUtils.convert(statRolePanelPage, statRolePanelVOS);
    }

    /**
     * 根据角色主键获取面板类型
     *
     * @param roleId 角色主键
     *
     * @return 面板数据类型信息
     */
    @GetMapping("/stat/role-panel/{roleId}/role")
    public StatRolePanelVO listByRole(@NotNull(message = "角色主键不能为空") @Min(value = 1, message = "角色主键不能小于1")
                                      @PathVariable("roleId") Long roleId
                                     ) {
        List<StatRolePanel> statRolePanels = this.statRolePanelService.listByRole(roleId);
        StatRolePanelVO rolePanelVO = new StatRolePanelVO();
        if (CollectionUtils.isEmpty(statRolePanels)) {
            return rolePanelVO;
        }
        Map<Long, List<StatRolePanel>> rolePanelMap = statRolePanels.stream().collect(Collectors.groupingBy(StatRolePanel::getRoleId));
        if (rolePanelMap.containsKey(roleId)) {
            List<StatRolePanel> panels = rolePanelMap.get(roleId);
            rolePanelVO.setRoleId(roleId);
            rolePanelVO.setPanelTypes(panels.stream().map(StatRolePanel::getPanelType).collect(Collectors.toList()));
        }
        return rolePanelVO;
    }

    /**
     * 根据角色主键集合获取面板类型
     *
     * @return 面板数据类型信息
     */
    @GetMapping("/stat/role-panel/roles")
    public StatRolePanelVO listByRoles(@Valid StatRolePanelQueryParam queryParam) {
        boolean isTenantAdmin = SecurityContextHolder.isTenantAdmin();
        if (isTenantAdmin) {
            List<StatRolePanelEnum> enums = Stream
                    .of(StatRolePanelEnum.TRADE_SUMMARY, StatRolePanelEnum.PORK_TRADE_CAPACITY, StatRolePanelEnum.PORK_TRADE_AVERAGE,
                        StatRolePanelEnum.PORK_SUPPLY_CAPACITY, StatRolePanelEnum.PORK_SPOT_CHECK, StatRolePanelEnum.PORK_ARRANGE_AUDIT,
                        StatRolePanelEnum.PORK_REPORT, StatRolePanelEnum.PORK_RISK_LEVEL, StatRolePanelEnum.PORK_SUPPLY_TRADE_RANK,
                        StatRolePanelEnum.PORK_ABATTOIR, StatRolePanelEnum.PORK_ARRANGE_DETAIL).collect(Collectors.toList());
            StatRolePanelVO record = new StatRolePanelVO();
            record.setPanelTypes(enums);
            return record;
        }
        List<StatRolePanel> statRolePanels = this.statRolePanelService.listByRoles(queryParam.getRoleIds());
        StatRolePanelVO rolePanelVO = new StatRolePanelVO();
        if (CollectionUtils.isEmpty(statRolePanels)) {
            return rolePanelVO;
        }
        Set<StatRolePanelEnum> types = statRolePanels.stream().map(StatRolePanel::getPanelType).collect(Collectors.toSet());
        rolePanelVO.setPanelTypes(types);
        return rolePanelVO;
    }

    /**
     * 获取看板角色列表
     *
     * @return 角色信息
     */
    @GetMapping("/stat/roles")
    public List<RoleDTO> getStatRoles() {
        return this.roleClient.listRoles();
    }
}
