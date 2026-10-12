package com.guhecloud.cube.biz.mdata.server.scene.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.mdata.server.area.entity.Area;
import com.guhecloud.cube.biz.mdata.server.area.service.AreaService;
import com.guhecloud.cube.biz.mdata.server.scene.param.StallSceneQueryParam;
import com.guhecloud.cube.biz.mdata.server.scene.service.StallSceneService;
import com.guhecloud.cube.biz.mdata.server.scene.vo.StallScenePageVO;
import com.guhecloud.cube.biz.mdata.server.stall.entity.Stall;
import com.guhecloud.cube.biz.mdata.server.stall.entity.StallCategory;
import com.guhecloud.cube.biz.mdata.server.stall.service.StallCategoryService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 档位场景接口
 *
 * @author lanchun
 * @date 2021/12/3 11:19
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StallSceneController {

    private final StallSceneService stallSceneService;

    private final AreaService areaService;

    private final StallCategoryService stallCategoryService;

    /**
     * 档位分页列表查询(根据场景获取)
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/stall-scene/stalls")
    public Pagination<StallScenePageVO> getSceneStalls(@Valid StallSceneQueryParam param) {
        Page<Stall> stallPage = stallSceneService.getStalls(param);
        if (CollectionUtils.isEmpty(stallPage.getRecords())) {
            return Pagination.empty();
        }
        List<StallScenePageVO> stallScenePageVOS = extendStallScenePageVOS(stallPage);
        return PagingUtils.convert(stallPage, stallScenePageVOS);
    }

    /**
     * 根据场景获取未绑定的档位分页列表查询
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/stall-scene/not-bind/stalls")
    public Pagination<StallScenePageVO> getSceneNotBindStalls(@Valid StallSceneQueryParam param) {
        Page<Stall> stallPage = this.stallSceneService.getSceneNotBindStalls(param);
        if (CollectionUtils.isEmpty(stallPage.getRecords())) {
            return Pagination.empty();
        }
        List<StallScenePageVO> stallScenePageVOS = extendStallScenePageVOS(stallPage);
        return PagingUtils.convert(stallPage, stallScenePageVOS);
    }

    public List<StallScenePageVO> extendStallScenePageVOS(Page<Stall> stallPage) {
        List<StallScenePageVO> stallScenePageVOS = ObjectUtils.copying(stallPage.getRecords(), StallScenePageVO.class);
        // 扩展区域信息
        List<Area> areas = this.areaService.list();
        Map<Long, List<Area>> areaMap = areas.stream().collect(Collectors.groupingBy(Area::getId));
        // 扩展档位类型名称
        List<StallCategory> stallCategories = this.stallCategoryService.list();
        Map<String, List<StallCategory>> stallCategoryMap = stallCategories.stream()
                .collect(Collectors.groupingBy(stallCategory -> String.valueOf(stallCategory.getId())));
        for (StallScenePageVO pageVO : stallScenePageVOS) {
            // 档位分类信息
            if (MapUtils.isNotEmpty(stallCategoryMap) &&
                Objects.nonNull(stallCategoryMap.get(pageVO.getType()))) {
                StallCategory stallCategory = stallCategoryMap.get(pageVO.getType()).get(0);
                pageVO.setTypeName(stallCategory.getName());
            }
            // 扩展档位区域信息
            if (StringUtils.isNotEmpty(pageVO.getArea())) {
                pageVO.setAreaNames(this.areaService.getAndAppendAreaName(Arrays.stream(pageVO.getArea()
                                                                                                .split(",")).map(Long::parseLong)
                                                                                  .collect(Collectors.toList()),
                                                                          areaMap
                                                                         ));
            }
        }
        return stallScenePageVOS;
    }
}
