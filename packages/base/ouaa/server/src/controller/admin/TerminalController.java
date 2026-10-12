package com.guhecloud.cube.biz.ouaa.server.controller.admin;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.ouaa.server.entity.Application;
import com.guhecloud.cube.biz.ouaa.server.entity.Terminal;
import com.guhecloud.cube.biz.ouaa.server.entity.TerminalApplication;
import com.guhecloud.cube.biz.ouaa.server.param.ApplicationPathModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.TerminalModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.TerminalQueryParam;
import com.guhecloud.cube.biz.ouaa.server.service.ApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.TerminalApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.TerminalService;
import com.guhecloud.cube.biz.ouaa.server.vo.ApplicationPageVO;
import com.guhecloud.cube.biz.ouaa.server.vo.TerminalPageVO;
import com.guhecloud.cube.biz.ouaa.server.vo.TerminalVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 应用端管理接口
 *
 * @author lanchun
 * @date 2021/10/13 19:07
 */
@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@PreAuthorize("isAdmin()")
public class TerminalController {

    private final TerminalService terminalService;
    private final TerminalApplicationService terminalApplicationService;
    private final ApplicationService applicationService;

    @PostMapping("/terminal")
    public Long save(@Valid @RequestBody TerminalModifyParam param) {
        return terminalService.save(param);
    }

    @PutMapping("/terminal/{id}")
    public void update(@PathVariable("id") @Min(value = 1, message = "应用端主键不能小于1") Long id,
                       @Valid @RequestBody TerminalModifyParam param
                      ) {
        terminalService.update(id, param);
    }

    @DeleteMapping("/terminal/{id}")
    public void remove(@PathVariable("id") @Min(value = 1, message = "应用端主键不能小于1") Long id) {
        terminalService.remove(id);
    }

    @GetMapping("/terminals")
    public Pagination<TerminalPageVO> listByPage(TerminalQueryParam param) {
        Page<Terminal> page = this.terminalService.listByPage(param);
        List<TerminalPageVO> pageVOS = ObjectUtils.copying(page.getRecords(), TerminalPageVO.class);
        if (CollectionUtils.isEmpty(pageVOS)) {
            return PagingUtils.convert(page, Collections.emptyList());
        }
        return PagingUtils.convert(page, pageVOS);
    }

    @GetMapping("/terminal/{id}")
    public TerminalVO getById(@PathVariable("id") Long id) {
        Terminal terminal = this.terminalService.getById(id);
        Assert.isNotFound(terminal, "不存在应用端信息");
        return ObjectUtils.copying(terminal, TerminalVO.class);
    }

    @GetMapping("/terminal/{id}/applications")
    public List<ApplicationPageVO> listTerminalApplications(@PathVariable("id") Long terminalId) {
        List<TerminalApplication> terminalApplications = this.terminalApplicationService.lambdaQuery()
                .eq(TerminalApplication::getTerminalId, terminalId).list();
        List<Long> applicationIds = terminalApplications.stream().map(TerminalApplication::getApplicationId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(applicationIds)) {
            return Collections.emptyList();
        }
        List<Application> applications = this.applicationService.listByIds(applicationIds);
        List<ApplicationPageVO> applicationPageVOS = ObjectUtils.copying(applications, ApplicationPageVO.class);
        applicationPageVOS.stream().forEach(applicationPageVO -> {
            TerminalApplication application = terminalApplications.stream()
                    .filter(terminalApplication -> applicationPageVO.getId().equals(terminalApplication.getApplicationId())).findFirst()
                    .get();
            applicationPageVO.setPath(application.getPath());
        });
        return applicationPageVOS;
    }

    @PostMapping("/terminal/{id}/application-mapping")
    public void saveTerminalApplicationMapping(@PathVariable("id") Long terminalId, @RequestParam("applicationId") Long applicationId) {
        terminalApplicationService.save(terminalId, applicationId);
    }

    @PostMapping("/terminal/{id}/application-path-mapping")
    public void saveTerminalApplicationPathMapping(@PathVariable("id") Long terminalId,
                                                   @Valid @RequestBody ApplicationPathModifyParam param
                                                  ) {
        terminalApplicationService.savePath(terminalId, param.getId(), param.getPath());
    }

    @GetMapping("/terminal/{id}/application-path-mapping")
    public ApplicationPageVO getTerminalApplicationPathMapping(@PathVariable("id") Long terminalId,
                                                               @RequestParam("applicationId") Long applicationId
                                                              ) {
        TerminalApplication terminalApplication = terminalApplicationService.getOne(Wrappers.<TerminalApplication>lambdaQuery()
                                                                                            .eq(TerminalApplication::getTerminalId, terminalId)
                                                                                            .eq(TerminalApplication::getApplicationId, applicationId)
                                                                                   );
        if (Objects.isNull(terminalApplication)) {
            return null;
        }
        Application application = this.applicationService.getById(terminalApplication.getApplicationId());
        if (Objects.isNull(application)) {
            return null;
        }
        ApplicationPageVO applicationPageVO = ObjectUtils.copying(application, ApplicationPageVO.class);
        applicationPageVO.setPath(terminalApplication.getPath());
        return applicationPageVO;
    }

    @DeleteMapping("/terminal/{id}/application-mapping")
    public void removeTerminalApplicationMapping(@PathVariable("id") Long terminalId, @RequestParam("applicationId") Long applicationId) {
        terminalApplicationService.remove(terminalId, applicationId);
    }

}
