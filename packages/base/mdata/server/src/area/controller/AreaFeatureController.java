package com.guhecloud.cube.biz.mdata.server.area.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.mdata.server.area.entity.AreaFeature;
import com.guhecloud.cube.biz.mdata.server.area.param.AreaSpaceQueryParam;
import com.guhecloud.cube.biz.mdata.server.area.service.AreaFeatureService;
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
 * 区域特征
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AreaFeatureController {
    private final AreaFeatureService areaFeatureService;

    /**
     * 详情
     *
     * @param id id
     *
     * @return 详情
     */
    @GetMapping("/area-feature/detail")
    public AreaSpaceVO areaFeatureDetail(@RequestParam("id") Long id) {
        AreaFeature feature = areaFeatureService.getById(id);
        Assert.isNotFound(feature, "数据不存在！");
        return ObjectUtils.copying(feature, AreaSpaceVO.class);
    }

    /**
     * 分页列表
     *
     * @param pageParam 分页参数
     *
     * @return 分页列表
     */
    @GetMapping("/area-feature/page")
    public Pagination<AreaSpaceVO> areaFeaturePage(AreaSpaceQueryParam pageParam) {
        Page<AreaFeature> page = areaFeatureService.page(PagingUtils.convert(pageParam), Wrappers.<AreaFeature>lambdaQuery()
                                                                 .and(StringUtils.isNotEmpty(pageParam.getName()),
                                                                      i -> i.like(AreaFeature::getName, pageParam.getName())
                                                                              .or().like(AreaFeature::getDescribe, pageParam.getName())
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
    @GetMapping("/area-feature/list")
    public List<KeyValueLongStringVO> areaFeatureList(@Nullable @RequestParam("name") String name) {
        List<AreaFeature> list = areaFeatureService.list(Wrappers.<AreaFeature>lambdaQuery()
                                                                 .like(StringUtils.isNotEmpty(name), AreaFeature::getName, name)
                                                        );
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        return list.stream().map(f -> KeyValueLongStringVO.builder().key(f.getId()).value(f.getName()).build())
                .collect(Collectors.toList());
    }

    /**
     * 新增
     *
     * @param areaFeature 新增对象
     *
     * @return 成功
     */
    @PostMapping("/area-feature/add")
    public Long areaFeatureAdd(@RequestBody AreaFeature areaFeature) {
        Long id = IdWorker.getId();
        areaFeature.setId(id);
        areaFeatureService.save(areaFeature);
        return id;
    }

    /**
     * 编辑
     *
     * @param areaFeature 编辑对象
     *
     * @return 成功
     */
    @PutMapping("/area-feature/edit")
    public Boolean areaFeatureEdit(@RequestBody AreaFeature areaFeature) {
        Assert.isNotFound(areaFeatureService.getById(areaFeature.getId()), "数据不存在！");
        areaFeatureService.update(Wrappers.<AreaFeature>lambdaUpdate()
                                          .eq(AreaFeature::getId, areaFeature.getId())
                                          .set(AreaFeature::getName, areaFeature.getName())
                                          .set(AreaFeature::getDescribe, areaFeature.getDescribe())
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
    @DeleteMapping("/area-feature/remove")
    public Boolean areaFeatureRemove(@RequestParam("id") Long id) {
        DatabaseContextHolder.softDelete(areaFeatureService, id);
        return true;
    }

}
