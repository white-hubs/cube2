package com.guhecloud.cube.biz.ouaa.server.controller.admin;

import com.guhecloud.cube.biz.ouaa.server.dto.ApplicationMarketDTO;
import com.guhecloud.cube.biz.ouaa.server.param.ApplicationBatchModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.ApplicationModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.ApplicationQueryParam;
import com.guhecloud.cube.biz.ouaa.server.service.ApplicationService;
import com.guhecloud.cube.biz.ouaa.server.vo.ApplicationPageVO;
import com.guhecloud.cube.biz.ouaa.server.vo.ApplicationVO;
import com.guhecloud.cube.biz.ouaa.server.vo.BaseInfo;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 应用配置接口
 */

@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationController {

    private final ApplicationService applicationService;

    /**
     * 新增应用
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/application")
    public Long save(@Valid @RequestBody ApplicationModifyParam param) {
        return applicationService.save(param);
    }

    /**
     * 修改应用
     *
     * @param id
     * @param param
     */
    @PutMapping("/application/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody ApplicationModifyParam param) {
        applicationService.update(id, param);
    }

    /**
     * 删除应用
     *
     * @param id
     */
    @DeleteMapping("/application/{id}")
    public void remove(@PathVariable("id") Long id) {
        applicationService.remove(id);
    }

    /**
     * 获取应用列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/applications")
    public Pagination<ApplicationPageVO> listByPage(@Valid ApplicationQueryParam param) {
        return applicationService.getApplicationPage(param);
    }

    /**
     * 获取应用详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/application/{id}")
    public ApplicationVO getById(@PathVariable("id") Long id) {
        return applicationService.getApplicationDetailById(id);
    }

    /**
     * 获取应用列表(用于下拉选择)
     */
    @GetMapping("/application/list/selection")
    public List<BaseInfo> list(ApplicationQueryParam param) {
        return applicationService.getList(param);
    }

    /**
     * 应用市场列表
     */
    @GetMapping("/app-market-list")
    public List<ApplicationMarketDTO> getApplicationMarketList(ApplicationQueryParam param) {
        return applicationService.getApplicationMarketList(param);
    }

    /**
     * 批量修改应用
     */
    @PutMapping("/application/batch")
    public void batchUpdate(@RequestBody ApplicationBatchModifyParam param) {
        applicationService.batchUpdate(param);
    }

    /**
     * 获取推荐应用
     */
    @GetMapping("/application/recommend")
    public List<ApplicationMarketDTO> getRecommendApplication(ApplicationQueryParam param) {
        return applicationService.getRecommendApplication(param);
    }

    /**
     * 我的应用
     */
    @GetMapping("/application/my")
    public Pagination<ApplicationPageVO> getMyApplication(ApplicationQueryParam param) {
        return applicationService.getMyApplication(param);
    }

    /**
     * 应用市场列表-官网
     */
    @GetMapping("/public/app-market-list")
    public List<ApplicationMarketDTO> getApplicationMarketListForWebSite(ApplicationQueryParam param) {
        param.setPublicAccess(true);
        return applicationService.getApplicationMarketList(param);
    }

    /**
     * 获取推荐应用-官网
     */
    @GetMapping("/public/application/recommend")
    public List<ApplicationMarketDTO> getRecommendApplicationForWebSite(ApplicationQueryParam param) {
        param.setPublicAccess(true);
        return applicationService.getRecommendApplication(param);
    }

}
