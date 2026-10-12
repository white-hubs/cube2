package com.guhecloud.cube.biz.ouaa.server.controller.context;

import com.guhecloud.cube.biz.ouaa.server.dto.ApplicationDTO;
import com.guhecloud.cube.biz.ouaa.server.param.HomeApplicationModifyParam;
import com.guhecloud.cube.biz.ouaa.server.service.ApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.HomeApplicationService;
import com.guhecloud.cube.biz.ouaa.server.service.TerminalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 首页应用接口管理
 *
 * @author lanchun
 * @date 2022/5/13 14:12
 */
@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@PreAuthorize("isAuthenticated()")
public class HomeApplicationController {

    private final HomeApplicationService homeApplicationService;

    private final ApplicationService applicationService;

    private final TerminalService terminalService;

    /***
     * 首页应用保存
     * @param params
     */
    @PostMapping("/home-application")
    public void save(@Valid @RequestBody List<HomeApplicationModifyParam> params) {
        this.homeApplicationService.save(params);
    }

    /**
     * 首页应用删除
     *
     * @param applicationId
     */
    @DeleteMapping("/home-application/{applicationId}")
    public void removeByApplicationId(@PathVariable("applicationId") Long applicationId) {
        this.homeApplicationService.removeByApplicationId(applicationId);
    }

    /**
     * @Description 通过code获取应用
     * @Author lrj
     * @Param
     * @Return
     * @Exception
     */
    @GetMapping("/applicationByCode")
    public ApplicationDTO getApplicationByCode(@RequestParam("code") String code) {

        return applicationService.getApplicationByCode(code);
    }

    /**
     * @Description 通过codes获取应用
     * @Author lrj
     * @Param
     * @Return
     * @Exception
     */
    @PostMapping("/applicationsByCodes")
    public List<ApplicationDTO> getApplicationsByCodes(@RequestBody List<String> codes) {

        return applicationService.getApplicationsByCodes(codes);
    }

    @GetMapping("/inner/applications")
    public List<ApplicationDTO> getApplications() {
        return applicationService.getApplications();
    }

    /**
     * @Description 获取租户下的terminal
     * @Author lrj
     */
    @GetMapping("/getTenantTerminal")
    public List<String> getTenantTerminal(@RequestParam("tenantTerminal") String tenantTerminal) {

        return terminalService.getTenantTerminal();
    }
}
