package com.guhecloud.cube.srv.electron.guide.controller;

import com.guhecloud.cube.srv.electron.guide.param.GuideDeviceManageModifyParam;
import com.guhecloud.cube.srv.electron.guide.param.query.GuideDeviceManagePageQueryParam;
import com.guhecloud.cube.srv.electron.guide.service.GuideDeviceManageService;
import com.guhecloud.cube.srv.electron.guide.vo.GuideDeviceManageDetailVO;
import com.guhecloud.cube.srv.electron.guide.vo.GuideDeviceManagePageVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 设备管理
 *
 * @author lanchun
 * @date 2024/9/12 14:16
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GuideDeviceManageController {
    private final GuideDeviceManageService guideDeviceManageService;

    /**
     * 导览设备新增
     *
     * @param param
     */
    @PostMapping("/guide/device")
    public Long save(@Valid @RequestBody GuideDeviceManageModifyParam param) {
        return this.guideDeviceManageService.save(param);
    }

    /**
     * 导览设备修改
     *
     * @param id
     * @param param
     */
    @PutMapping("/guide/device/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody GuideDeviceManageModifyParam param) {
        this.guideDeviceManageService.update(id, param);
    }

    /**
     * 导览设备删除
     *
     * @param id
     *
     * @return
     */
    @DeleteMapping("/guide/device/{id}")
    public void remove(@PathVariable("id") Long id) {
        this.guideDeviceManageService.delete(id);
    }

    /**
     * 导览设备详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/guide/device/{id}")
    public GuideDeviceManageDetailVO getDetail(@PathVariable("id") Long id) {
        return this.guideDeviceManageService.getDetail(id);
    }

    /**
     * 导览设备分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/guide/device/page")
    public Pagination<GuideDeviceManagePageVO> getPage(@Valid GuideDeviceManagePageQueryParam param) {
        return this.guideDeviceManageService.getPage(param);
    }

    /**
     * 导览设备分页列表(对外暴露方法)
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/public/guide/device/page")
    public Pagination<GuideDeviceManagePageVO> getPublicPage(@Valid GuideDeviceManagePageQueryParam param) {
        return getPage(param);
    }

    /**
     * 导览设备详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/public/guide/device/{id}")
    public GuideDeviceManageDetailVO getPublicDetail(@PathVariable("id") Long id) {
        return getDetail(id);
    }
}
