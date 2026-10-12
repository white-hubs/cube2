package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.TerminalConfigMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.Role;
import com.guhecloud.cube.biz.ouaa.server.entity.Terminal;
import com.guhecloud.cube.biz.ouaa.server.entity.TerminalConfig;
import com.guhecloud.cube.biz.ouaa.server.param.TerminalConfigParam;
import com.guhecloud.cube.biz.ouaa.server.param.TerminalConfigQueryParam;
import com.guhecloud.cube.biz.ouaa.server.service.RoleService;
import com.guhecloud.cube.biz.ouaa.server.service.TerminalConfigService;
import com.guhecloud.cube.biz.ouaa.server.service.TerminalService;
import com.guhecloud.cube.biz.ouaa.server.vo.TerminalConfigVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 15561
 * @description 针对表【terminal_config(访问端信息表)】的数据库操作Service实现
 * @createDate 2024-08-13 15:26:21
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TerminalConfigServiceImpl extends ServiceImpl<TerminalConfigMapper, TerminalConfig> implements TerminalConfigService {
    private final TerminalService terminalService;
    private final RoleService roleService;

    @Override
    public Long save(TerminalConfigParam param) {
        TerminalConfig terminalConfig = ObjectUtils.copying(param, TerminalConfig.class);
        terminalConfig.setId(IdWorker.getId());
        this.baseMapper.insert(terminalConfig);
        return terminalConfig.getId();
    }

    @Override
    public void update(Long id, TerminalConfigParam param) {
        TerminalConfig terminalConfig = this.getById(id);
        Assert.isNotFound(terminalConfig, "访问端配置不存在");
        ObjectUtils.copying(param, terminalConfig);
        this.baseMapper.updateById(terminalConfig);
    }

    @Override
    public void remove(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public List<TerminalConfigVO> lists(TerminalConfigQueryParam param) {

        List<Terminal> terminalList = terminalService.lambdaQuery()
                .eq(Terminal::getCode, SessionContextHolder.getRequester().getTerminal())
                .select(Terminal::getProductId)
                .list();
        if (CollectionUtils.isEmpty(terminalList)) {
            return new ArrayList<>();
        }
        List<Long> productIdList = terminalList.stream().map(Terminal::getProductId).collect(Collectors.toList());
        List<Terminal> terminals = terminalService.lambdaQuery().in(Terminal::getProductId, productIdList)
                .and(StringUtils.isNotEmpty(param.getKeyword()), q -> q
                        .like(Terminal::getName, param.getKeyword())
                        .or()
                        .like(Terminal::getCode, param.getKeyword()))
                .list();
        if (CollectionUtils.isEmpty(terminals)) {
            return new ArrayList<>();
        }
        Set<String> terminalCodeSet = terminals.stream().map(Terminal::getCode).collect(Collectors.toSet());

        List<TerminalConfig> terminalConfigs = this.lambdaQuery()
                .in(TerminalConfig::getCode, terminalCodeSet)
                .orderByDesc(TerminalConfig::getCreateTime).list();
        Map<String, TerminalConfig> terminalConfigMap = new HashMap<>(terminalConfigs.size());
        Map<Long, Role> roleMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(terminalConfigs)) {
            terminalConfigMap = terminalConfigs.stream().collect(Collectors.toMap(TerminalConfig::getCode, Function.identity()));
            Set<Long> roleIds = terminalConfigs.stream().filter(t -> Objects.nonNull(t.getDefaultRole()))
                    .map(TerminalConfig::getDefaultRole).collect(Collectors.toSet());
            if (CollectionUtils.isNotEmpty(roleIds)) {
                roleMap = roleService.lambdaQuery()
                        .in(Role::getId, roleIds)
                        .list()
                        .stream().collect(Collectors.toMap(Role::getId, Function.identity()));
            }
        }
        List<TerminalConfigVO> result = new ArrayList<>();
        for (Terminal terminal : terminals) {
            TerminalConfigVO terminalConfigVO = new TerminalConfigVO();
            terminalConfigVO.setTerminalName(terminal.getName());
            terminalConfigVO.setCode(terminal.getCode());
            TerminalConfig orDefault = terminalConfigMap.getOrDefault(terminal.getCode(), new TerminalConfig());
            terminalConfigVO.setAlias(orDefault.getAlias());
            terminalConfigVO.setDefaultRole(orDefault.getDefaultRole());
            terminalConfigVO.setId(orDefault.getId());
            terminalConfigVO.setDefaultRoleName(roleMap.getOrDefault(orDefault.getDefaultRole(), new Role()).getName());
            result.add(terminalConfigVO);
        }
        return result;
    }
}




