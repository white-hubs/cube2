package com.guhecloud.cube.biz.metric.server.metric.library.controller;

import com.guhecloud.cube.biz.metric.server.metric.library.command.ChangeMetricLibraryCommand;
import com.guhecloud.cube.biz.metric.server.metric.library.command.DelMetricLibraryCommand;
import com.guhecloud.cube.biz.metric.server.metric.library.command.IncreaseMetricLibraryCommand;
import com.guhecloud.cube.biz.metric.server.metric.library.query.MetricLibraryQueryParam;
import com.guhecloud.cube.biz.metric.server.metric.library.service.command.MetricLibraryCommandService;
import com.guhecloud.cube.biz.metric.server.metric.library.service.query.MetricLibraryQueryService;
import com.guhecloud.cube.biz.metric.server.metric.library.vo.MetricLibraryVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MetricLibraryController {

    private final MetricLibraryCommandService metricLibraryCommandService;

    private final MetricLibraryQueryService metricLibraryQueryService;

    public MetricLibraryController(MetricLibraryCommandService metricLibraryCommandService,
                                   MetricLibraryQueryService metricLibraryQueryService
                                  ) {
        this.metricLibraryCommandService = metricLibraryCommandService;
        this.metricLibraryQueryService = metricLibraryQueryService;
    }

    /**
     * 指标库新增
     *
     * @param command
     *
     * @return
     */
    @PostMapping("/metric-libraries")
    public Long increaseMetricLibrary(@RequestBody @Valid IncreaseMetricLibraryCommand command) {
        return metricLibraryCommandService.increaseMetricLibrary(command);
    }

    /**
     * 指标库修改
     *
     * @param command
     *
     * @return
     */
    @PutMapping("/metric-libraries")
    public Long changeMetricLibrary(@RequestBody @Valid ChangeMetricLibraryCommand command) {
        return metricLibraryCommandService.changeMetricLibrary(command);
    }

    /**
     * 指标库分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/metric-libraries")
    public Pagination<MetricLibraryVO> getPage(MetricLibraryQueryParam param) {
        return metricLibraryQueryService.getMetricLibraryPage(param);
    }

    /**
     * 指标库列表
     *
     * @return
     */
    @GetMapping("/metric-libraries/all")
    public List<MetricLibraryVO> getMetricLibraries() {
        return metricLibraryQueryService.getMetricLibraries();
    }

    /**
     * 指标库详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/metric-libraries/{id}")
    public MetricLibraryVO getMetricLibrary(@PathVariable("id") Long id) {
        return metricLibraryQueryService.getMetricLibraryDetail(id);
    }

    /**
     * 指标库删除
     *
     * @param id
     */
    @DeleteMapping("/metric-libraries/{id}")
    public void delMetricLibrary(@PathVariable("id") Long id) {
        DelMetricLibraryCommand command = new DelMetricLibraryCommand();
        command.setId(id);
        metricLibraryCommandService.deleteMetricLibrary(command);
    }

    /**
     * 指标初始化
     */
    @GetMapping("/metric-libraries/init")
    public void initMetricLibrary(@RequestParam("type") String type) {
        metricLibraryCommandService.initMetricLibrary(type);
    }
}
