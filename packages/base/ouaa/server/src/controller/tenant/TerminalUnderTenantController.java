package com.guhecloud.cube.biz.ouaa.server.controller.tenant;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.ouaa.server.entity.Application;
import com.guhecloud.cube.biz.ouaa.server.entity.TenantApplication;
import com.guhecloud.cube.biz.ouaa.server.entity.Terminal;
import com.guhecloud.cube.biz.ouaa.server.entity.TerminalApplication;
import com.guhecloud.cube.biz.ouaa.server.param.TerminalQueryParam;
import com.guhecloud.cube.biz.ouaa.server.service.ApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.TenantApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.TerminalApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.TerminalService;
import com.guhecloud.cube.biz.ouaa.server.utils.CustomConfigUtil;
import com.guhecloud.cube.biz.ouaa.server.vo.ApplicationPageVO;
import com.guhecloud.cube.biz.ouaa.server.vo.TerminalVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TerminalUnderTenantController {

    private final TerminalService terminalService;
    private final TerminalApplicationService terminalApplicationService;
    private final ApplicationService applicationService;
    private final TenantApplicationService tenantApplicationService;
    private final CustomConfigUtil customConfigUtil;

    @GetMapping("/terminal-under-tenant/page")
    public Pagination<TerminalVO> listByPage(TerminalQueryParam param) {
        Page<Terminal> page = this.terminalService.listByPage(param);
        List<TerminalVO> pageVOS = ObjectUtils.copying(page.getRecords(), TerminalVO.class);
        if (CollectionUtils.isEmpty(pageVOS)) {
            return PagingUtils.convert(page, Collections.emptyList());
        }
        customConfigUtil.terminalCustomConfigResolving(pageVOS);
        return PagingUtils.convert(page, pageVOS);
    }

    @GetMapping("/terminal-under-tenant/{id}/applications")
    public List<ApplicationPageVO> listTerminalApplications(@PathVariable("id") Long terminalId) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        Terminal terminal = this.terminalService.getById(terminalId);
        // 获取租户产品下的应用
        List<TenantApplication> tenantApplications = this.tenantApplicationService.listByTenantProduct(tenant);
        //租户下的应用
        Set<Long> applicationIds = tenantApplications.stream().map(TenantApplication::getApplicationId).collect(Collectors.toSet());
        // 获取访问端下的应用
        List<TerminalApplication> terminalApplications = this.terminalApplicationService.lambdaQuery()
                .eq(TerminalApplication::getTerminalId, terminalId)
                .in(TerminalApplication::getApplicationId, applicationIds)
                .list();
        if (CollectionUtils.isEmpty(terminalApplications)) {
            return Collections.emptyList();
        }
        List<Long> terminalApplicationIds = terminalApplications.stream().map(ta -> ta.getApplicationId()).collect(Collectors.toList());
        List<Application> applications = this.applicationService.listByIds(terminalApplicationIds);
        List<ApplicationPageVO> copying = ObjectUtils.copying(applications, ApplicationPageVO.class);
        customConfigUtil.applicationCustomConfigResolving(copying, true);
        return copying;
    }

}
