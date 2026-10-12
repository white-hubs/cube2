package com.guhecloud.cube.biz.mdata.server.area.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.mdata.server.area.entity.AreaStatus;
import com.guhecloud.cube.biz.mdata.server.area.param.AreaSpaceQueryParam;
import com.guhecloud.cube.biz.mdata.server.area.service.AreaStatusService;
import com.guhecloud.cube.biz.mdata.server.area.vo.AreaSpaceVO;
import com.guhecloud.cube.biz.mdata.server.area.vo.KeyValueLongStringVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 区域状态
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AreaStatusController {
    private final AreaStatusService areaStatusService;

    /**
     * 详情
     *
     * @param id id
     *
     * @return 详情
     */
    @GetMapping("/area-status/detail")
    public AreaSpaceVO areaStatusDetail(@RequestParam("id") Long id) {
        AreaStatus areaStatus = areaStatusService.getById(id);
        Assert.isNotFound(areaStatus, "数据不存在！");
        return ObjectUtils.copying(areaStatus, AreaSpaceVO.class);
    }

    /**
     * 分页列表
     *
     * @param pageParam 分页参数
     *
     * @return 分页列表
     */
    @GetMapping("/area-status/page")
    public Pagination<AreaSpaceVO> areaStatusPage(AreaSpaceQueryParam pageParam) {
        Page<AreaStatus> page = areaStatusService.page(PagingUtils.convert(pageParam), Wrappers.<AreaStatus>lambdaQuery()
                                                               .and(
                                                                       StringUtils.isNotEmpty(pageParam.getName()),
                                                                       i -> i.like(AreaStatus::getName, pageParam.getName())
                                                                               .or().like(AreaStatus::getDescribe, pageParam.getName())
                                                                   )
                                                      );
        return PagingUtils.convert(page, AreaSpaceVO.class);
    }

    /**
     * 列表
     *
     * @param name 名称
     *
     * @return 分页列表
     */
    @GetMapping("/area-status/list")
    public List<KeyValueLongStringVO> areaStatusList(@Nullable @RequestParam("name") String name) {
        List<AreaStatus> list = areaStatusService.list(Wrappers.<AreaStatus>lambdaQuery()
                                                               .like(StringUtils.isNotEmpty(name), AreaStatus::getName, name)
                                                      );
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        return list.stream().map(s -> KeyValueLongStringVO.builder().key(s.getId()).value(s.getName()).build())
                .collect(Collectors.toList());
    }

    /**
     * 新增
     *
     * @param areaStatus 新增对象
     *
     * @return 成功
     */
    @PostMapping("/area-status/add")
    public Long areaStatusAdd(@RequestBody AreaStatus areaStatus) {
        Long id = IdWorker.getId();
        areaStatus.setId(id);
        areaStatusService.save(areaStatus);
        return id;
    }

    /**
     * 编辑
     *
     * @param areaStatus 编辑对象
     *
     * @return 成功
     */
    @PutMapping("/area-status/edit")
    public Boolean areaStatusEdit(@RequestBody AreaStatus areaStatus) {
        Assert.isNotFound(areaStatusService.getById(areaStatus.getId()), "数据不存在！");
        areaStatusService.update(Wrappers.<AreaStatus>lambdaUpdate()
                                         .eq(AreaStatus::getId, areaStatus.getId())
                                         .set(AreaStatus::getName, areaStatus.getName())
                                         .set(AreaStatus::getDescribe, areaStatus.getDescribe())
                                );
        return true;
    }

    /**
     * 删除
     *
     * @param id id
     *
     * @return 成功
     */
    @DeleteMapping("/area-status/remove")
    public Boolean areaStatusRemove(@RequestParam("id") Long id) {
        DatabaseContextHolder.softDelete(areaStatusService, id);
        return true;
    }
}
