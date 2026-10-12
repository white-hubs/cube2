package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.TerminalMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.TenantApplication;
import com.guhecloud.cube.biz.ouaa.server.entity.Terminal;
import com.guhecloud.cube.biz.ouaa.server.entity.TerminalApplication;
import com.guhecloud.cube.biz.ouaa.server.param.TerminalModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.TerminalQueryParam;
import com.guhecloud.cube.biz.ouaa.server.service.TenantApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.TerminalApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.TerminalService;
import com.guhecloud.cube.biz.ouaa.server.vo.TerminalVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class TerminalServiceImpl extends ServiceImpl<TerminalMapper, Terminal> implements TerminalService {

    private final TerminalApplicationService terminalApplicationService;
    private final TenantApplicationService tenantApplicationService;

    @Override
    public Long save(@NonNull TerminalModifyParam param) {
        Terminal terminal = ObjectUtils.copying(param, Terminal.class);
        this.baseMapper.insert(terminal);
        return terminal.getId();
    }

    @Override
    public void update(@NonNull Long id, @NonNull TerminalModifyParam param) {
        Terminal terminal = this.getById(id);
        Assert.isNotFound(terminal, "访问端不存在");
        ObjectUtils.copying(param, terminal);
        this.baseMapper.updateById(terminal);
    }

    @Override
    public void remove(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
        // 删除访问端应用关联
        terminalApplicationService.removeByTerminal(id);

    }

    @Override
    public Terminal getById(Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public Terminal getByCode(String code) {
        return this.baseMapper.selectOne(Wrappers.<Terminal>lambdaQuery().eq(Terminal::getCode, code));
    }

    @Override
    public List<Terminal> listByIds(@NonNull Collection<? extends Serializable> idList) {
        return idList.isEmpty() ? Collections.emptyList() : this.baseMapper.selectBatchIds(idList);
    }

    @Override
    public Page<Terminal> listByPage(TerminalQueryParam param) {
        return this.lambdaQuery()
                .and(StringUtils.isNotEmpty(param.getKeyword()), q -> q
                        .like(Terminal::getName, param.getKeyword())
                        .or()
                        .like(Terminal::getCode, param.getKeyword()))
                .in(!CollectionUtils.isEmpty(param.getTerminalIds()), Terminal::getId, param.getTerminalIds())
                .eq(Objects.nonNull(param.getEnabled()), Terminal::getEnabled, param.getEnabled())
                .orderByDesc(Terminal::getCreateTime).page(PagingUtils.convert(param));
    }

    @Override
    public List<Terminal> listProductTerminals(@NonNull Long productId) {
        return this.listProductTerminals(Collections.singleton(productId));
    }

    @Override
    public List<Terminal> listProductTerminals(@NonNull Collection<Long> productIds) {
        return productIds.isEmpty() ? Collections.emptyList() :
               this.lambdaQuery().in(Terminal::getProductId, productIds).eq(Terminal::getEnabled, true)
                       .orderByAsc(Terminal::getName).orderByDesc(Terminal::getId).list();
    }

    @Override
    public List<String> getTenantTerminal() {
        List<TerminalVO> terminalVOS = this.listTenantTerminals();
        return terminalVOS.stream().map(TerminalVO::getCode).distinct().collect(Collectors.toList());
    }

    @Override
    public List<TerminalVO> listTenantTerminals() {
        List<Long> tenantTerminalId = this.listTenantTerminalId();
        if (CollectionUtils.isEmpty(tenantTerminalId)) {
            return Collections.emptyList();
        }
        List<Terminal> terminals = this.lambdaQuery()
                .in(Terminal::getId, tenantTerminalId)
                .eq(Terminal::getEnabled, true)
                .list();
        return ObjectUtils.copying(terminals, TerminalVO.class);
    }

    @Override
    public List<TerminalVO> listTerminals() {
        List<Terminal> terminals = this.lambdaQuery()
                .eq(Terminal::getEnabled, true)
                .list();
        return ObjectUtils.copying(terminals, TerminalVO.class);
    }

    @Override
    public Page<Terminal> lisTenantByPage(TerminalQueryParam param) {
        List<Long> tenantTerminalId = this.listTenantTerminalId();
        if (CollectionUtils.isEmpty(tenantTerminalId)) {
            return new Page<>();
        }
        param.setTerminalIds(tenantTerminalId);
        return this.listByPage(param);
    }

    /**
     * 获取租户下访问端
     *
     * @return
     */
    private List<Long> listTenantTerminalId() {
        List<TenantApplication> tenantApplications = tenantApplicationService.listByTenant(SessionContextHolder
                                                                                                   .getRequester().getTenant());
        if (CollectionUtils.isEmpty(tenantApplications)) {
            return Collections.emptyList();
        }
        Set<Long> applicationIds = tenantApplications.stream().map(TenantApplication::getApplicationId).collect(Collectors.toSet());
        List<TerminalApplication> terminalApplications = terminalApplicationService.lambdaQuery()
                .in(TerminalApplication::getApplicationId, applicationIds)
                .list();
        if (CollectionUtils.isEmpty(terminalApplications)) {
            return Collections.emptyList();
        }
        return terminalApplications.stream().map(TerminalApplication::getTerminalId).distinct().collect(Collectors.toList());
    }
}
