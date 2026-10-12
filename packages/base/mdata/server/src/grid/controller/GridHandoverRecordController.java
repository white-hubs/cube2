package com.guhecloud.cube.biz.mdata.server.grid.controller;

import com.guhecloud.cube.biz.mdata.server.grid.param.GridHandoverAddParam;
import com.guhecloud.cube.biz.mdata.server.grid.param.GridHandoverRecordQueryParam;
import com.guhecloud.cube.biz.mdata.server.grid.param.GridHandoverTakeOverParam;
import com.guhecloud.cube.biz.mdata.server.grid.service.GridHandoverRecordService;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridHandoverRecordDetailVO;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridHandoverRecordVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 网格交接API
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GridHandoverRecordController {
    private final GridHandoverRecordService gridHandoverRecordService;

    /**
     * 发起网格交接
     *
     * @return 交接记录id列表
     */
    @PostMapping("/grid/handover/add")
    public List<Long> addHandoverRecord(@Valid @RequestBody GridHandoverAddParam param) {
        return gridHandoverRecordService.addHandoverRecord(param);
    }

    /**
     * 接班
     */
    @PostMapping("/grid/handover/takeover")
    public void takeOver(@Valid @RequestBody GridHandoverTakeOverParam param) {
        gridHandoverRecordService.takeOver(param);
    }

    /**
     * 撤销交接
     */
    @DeleteMapping("/grid/handover/cancel/{id}")
    public boolean cancel(@PathVariable Long id) {
        return gridHandoverRecordService.cancel(id);
    }

    /**
     * 获取当前用户交接班记录列表
     */
    @GetMapping("/grid/handover/list")
    public Pagination<GridHandoverRecordVO> getHandoverRecord(GridHandoverRecordQueryParam param) {
        return gridHandoverRecordService.getHandoverRecord(param);
    }

    /**
     * 获取交接班记录详情
     */
    @GetMapping("/grid/handover/detail")
    public GridHandoverRecordDetailVO getHandoverRecordDetail(@RequestParam("id") Long id) {
        return gridHandoverRecordService.getHandoverRecordDetail(id);
    }
}
