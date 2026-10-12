package com.guhecloud.cube.srv.gen.visitor.management.server.controller;

import com.guhecloud.cube.srv.gen.visitor.management.server.service.VisitAreaService;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.KeyWordPageParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.VisitAreaDetailParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.VisitAreaParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.AreaSelectResult;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.VisitAreaResult;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 访问区域
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VisitAreaController {

    private final VisitAreaService visitAreaService;

    /**
     * 区域下拉列表
     *
     * @param param 入参
     *
     * @return 列表
     */
    @GetMapping("/visitor/thirdArea/list")
    public List<AreaSelectResult> getAreaList(KeyWordPageParam param) {
        return visitAreaService.getAreaList(param);
    }

    /**
     * 访问时段列表
     *
     * @param param 入参
     *
     * @return 列表
     */
    @GetMapping("/visitor/area/page")
    public Pagination<VisitAreaResult> periodPage(KeyWordPageParam param) {
        return visitAreaService.periodPage(param);
    }

    /**
     * 访问时段详情
     *
     * @param param 条件
     *
     * @return 详情
     */
    @GetMapping("/visitor/area/detail")
    public VisitAreaResult periodDetail(VisitAreaDetailParam param) {
        return visitAreaService.periodDetail(param);
    }

    /**
     * 添加区域时段
     *
     * @param param 入参
     */
    @PostMapping("/visitor/area/add")
    public void addVisitPeriod(@RequestBody VisitAreaParam param) {
        visitAreaService.addVisitPeriod(param);
    }

    /**
     * 编辑访问区域
     *
     * @param param 入参
     */
    @PostMapping("/visitor/area/edit")
    public void editPeriods(@RequestBody VisitAreaParam param) {
        visitAreaService.editPeriods(param);
    }

    /**
     * 移除访问时段
     *
     * @param areaId areaId
     */
    @DeleteMapping("/visitor/area/remove")
    public void removePeriod(Long areaId) {
        visitAreaService.removePeriod(areaId);
    }
}
