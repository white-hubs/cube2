package com.guhecloud.cube.biz.ouaa.server.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.server.entity.AppVersion;
import com.guhecloud.cube.biz.ouaa.server.param.AppVersionModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.AppVersionQueryParam;
import com.guhecloud.cube.biz.ouaa.server.service.AppVersionService;
import com.guhecloud.cube.biz.ouaa.server.vo.AppVersionVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@PreAuthorize("isAdmin()")
public class AppVersionController {

    private final AppVersionService appVersionService;

    private final FileClient fileClient;

    /**
     * 版本新增
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/app-version")
    public Long save(@Valid @RequestBody AppVersionModifyParam param) {
        return this.appVersionService.save(param);
    }

    /**
     * 版本修改
     *
     * @param id
     * @param param
     */
    @PutMapping("/app-version/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody AppVersionModifyParam param) {
        this.appVersionService.update(id, param);
    }

    /**
     * 版本详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/app-version/{id}")
    public AppVersionVO getAppVersion(@PathVariable("id") Long id) {
        AppVersion appVersion = this.appVersionService.getById(id);
        Assert.isNotFound(appVersion, "版本记录不存在");
        AppVersionVO appVersionVO = ObjectUtils.copying(appVersion, AppVersionVO.class);
        List<FileVO> fileVOList = this.fileClient.getByBusinessKey(appVersionVO.getId());
        if (!CollectionUtils.isEmpty(fileVOList)) {
            appVersionVO.setFileVO(fileVOList.get(0));
        }
        return appVersionVO;
    }

    /**
     * 版本列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/app-versions")
    public Pagination<AppVersionVO> getAppVersionPage(AppVersionQueryParam param) {
        Page<AppVersion> page = this.appVersionService.getPage(param);
        List<AppVersionVO> appVersionVOS = ObjectUtils.copying(page.getRecords(), AppVersionVO.class);
        if (CollectionUtils.isEmpty(appVersionVOS)) {
            return Pagination.empty();
        }
        return PagingUtils.convert(page, appVersionVOS);
    }

    /**
     * 版本删除
     *
     * @param id
     */
    @DeleteMapping("/app-version/{id}")
    public void removeById(@PathVariable("id") Long id) {
        this.appVersionService.removeById(id);
    }
}
