package com.guhecloud.cube.srv.gen.energymanagement.server.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.energymanagement.server.converters.RouteProjectConverterForRouteTreeVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.RouteProject;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.RouteProjectParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.RouteProjectQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.RouteProjectService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.RouteProjectVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.RouteTreeVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.base.util.TreeUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lrj
 * @Description 支路分项配置
 * @date 2022-07-04 15:12:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RouteProjectController {
    private final RouteProjectService routeProjectService;

    /**
     * 功能描述： 支路分项配置添加
     */
    @PostMapping(value = "/route-project")
    public Long add(@Valid @RequestBody RouteProjectParam param) {
        return routeProjectService.add(param);
    }

    /**
     * 功能描述： 支路分项配置修改
     */
    @PutMapping(value = "/route-project/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody RouteProjectParam param) {
        routeProjectService.update(id, param);
    }

    /**
     * 功能描述： 支路分项配置删除
     */
    @DeleteMapping(value = "/route-project/{id}")
    public void delete(@PathVariable Long id) {
        routeProjectService.delete(id);
    }

    /**
     * 功能描述： 支路分项配置批量删除
     */
    @DeleteMapping(value = "/route-project/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        routeProjectService.batchDelete(ids);
    }

    /**
     * 功能描述： 支路分项配置分页
     */
    @GetMapping(value = "/route-project/page")
    public Pagination<RouteProjectVO> getPage(RouteProjectQueryParam param) {
        return routeProjectService.getPage(param);
    }

    /**
     * 功能描述： 支路分项配置详情
     */
    @GetMapping(value = "/route-project/{id}")
    public RouteProjectVO getById(@PathVariable Long id) {
        return routeProjectService.getById(id);
    }

    /**
     * 功能描述： 支路分项配置列表
     */
    @GetMapping(value = "/route-project/list")
    public List<RouteProjectVO> list() {
        return ObjectUtils.copying(routeProjectService.list(), RouteProjectVO.class);
    }

    /**
     * 功能支路树
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/route-project/tree")
    public List<RouteTreeVO> getTree(@Valid RouteProjectQueryParam param) {
        var list = routeProjectService.list(Wrappers.<RouteProject>lambdaQuery()
                                                    .like(StringUtils.isNotBlank(param.getRouteName()), RouteProject::getRouteName, param.getRouteName())
                                                    .eq(param.getParentId() !=
                                                        null, RouteProject::getParentId, param.getParentId())
                                                    .eq(RouteProject::getInstrumentType, param.getInstrumentType()));
        if (CollectionUtils.isNotEmpty(list)) {
            var ids = list.stream().flatMap(d -> Arrays.stream(d.getSequence().split("-"))).distinct().map(Long::parseLong)
                    .collect(Collectors.toList());
            var routeProjects = routeProjectService.list(Wrappers.<RouteProject>lambdaQuery()
                                                                 .in(RouteProject::getId, ids));
            List<RouteTreeVO> routeTreeVOS = RouteProjectConverterForRouteTreeVO.toRouteTreeVOS(routeProjects);
            return TreeUtils.assembleTrees(routeTreeVOS);
        }
        return Collections.emptyList();
    }

}