package com.guhecloud.cube.biz.ouaa.server.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.ouaa.server.entity.ApplicationGroup;
import com.guhecloud.cube.biz.ouaa.server.param.ApplicationGroupModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.ApplicationGroupQueryParam;
import com.guhecloud.cube.biz.ouaa.server.service.ApplicationGroupService;
import com.guhecloud.cube.biz.ouaa.server.vo.ApplicationGroupPageVO;
import com.guhecloud.cube.biz.ouaa.server.vo.ApplicationGroupVO;
import com.guhecloud.cube.biz.ouaa.server.vo.BaseInfo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
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

import java.util.Collections;
import java.util.List;

/**
 * 应用分类管理接口
 */
@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationGroupController {

    private final ApplicationGroupService applicationGroupService;

    /**
     * 应用分类新增
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/application-group")
    public Long save(@Valid @RequestBody ApplicationGroupModifyParam param) {
        return this.applicationGroupService.save(param);
    }

    /**
     * 应用分类修改
     *
     * @param id
     * @param param
     */
    @PutMapping("/application-group/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody ApplicationGroupModifyParam param) {
        this.applicationGroupService.update(id, param);
    }

    /**
     * 应用分类删除
     *
     * @param id
     */
    @DeleteMapping("/application-group/{id}")
    public void remove(@PathVariable("id") Long id) {
        this.applicationGroupService.remove(id);
    }

    /**
     * 应用分类分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/application-groups")
    public Pagination<ApplicationGroupPageVO> listByPage(@Valid ApplicationGroupQueryParam param) {
        Page<ApplicationGroup> page = this.applicationGroupService.listByPage(param);
        List<ApplicationGroupPageVO> pageVOS = ObjectUtils.copying(page.getRecords(), ApplicationGroupPageVO.class);
        if (CollectionUtils.isEmpty(pageVOS)) {
            return PagingUtils.convert(page, Collections.emptyList());
        }
        return PagingUtils.convert(page, pageVOS);
    }

    /**
     * 应用分类详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/application-group/{id}")
    public ApplicationGroupVO getById(@PathVariable("id") Long id) {
        ApplicationGroup applicationGroup = this.applicationGroupService.getById(id);
        Assert.isNotFound(applicationGroup, "不存在应用分类信息");
        return ObjectUtils.copying(applicationGroup, ApplicationGroupVO.class);
    }

    /**
     * 应用分类列表 (用于下拉选择)
     */
    @GetMapping("/application-group/list/selection")
    public List<BaseInfo> listForSelection(ApplicationGroupQueryParam param) {
        return this.applicationGroupService.listForSelection(param);
    }

}
