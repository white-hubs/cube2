package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.controller;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.param.CompanyCommandParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.param.CompanyQueryParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.service.IDetectionService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo.CompanyVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 检测公司
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CompanyController {

    private final IDetectionService service;

    /**
     * 获取受检公司分页列表
     *
     * @param queryParam 条件
     *
     * @return 分页列表
     */
    @GetMapping("/company/page")
    public Pagination<CompanyVO> getPage(CompanyQueryParam queryParam) {
        return service.getCompanyPage(queryParam);
    }

    /**
     * 检测公司详情
     *
     * @param tenant 租户
     *
     * @return 详情
     */
    @GetMapping("/company/detail")
    public CompanyVO getDetail(Integer tenant) {
        return service.getCompanyDetail(tenant);
    }

    /**
     * 编辑检测公司
     *
     * @param commandParam 入参
     *
     * @return 租户id
     */
    @PutMapping("/company/edit")
    public Integer edit(@RequestBody CompanyCommandParam commandParam) {
        return service.editCompany(commandParam);
    }

    /**
     * 清除检测公司信息
     *
     * @param tenantId 租户id
     *
     * @return 是否成功
     */
    @DeleteMapping("/company/remove")
    public Boolean cleanCompany(Integer tenantId) {
        return service.cleanCompany(tenantId);
    }

    /**
     * 获取检测天数倒计时
     *
     * @return 是否合格
     */
    @GetMapping("/company/countdown")
    public Integer getCountdown() {
        return service.getCountdown();
    }

}
