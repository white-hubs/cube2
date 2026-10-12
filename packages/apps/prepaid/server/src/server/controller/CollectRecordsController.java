package com.guhecloud.cube.srv.prepaid.server.controller;

import com.guhecloud.cube.srv.prepaid.server.param.CollectRecordsPageParam;
import com.guhecloud.cube.srv.prepaid.server.result.CollectRecordsResult;
import com.guhecloud.cube.srv.prepaid.server.service.CollectRecordsService;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * 抄表记录
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CollectRecordsController {

    private final CollectRecordsService collectRecordsService;

    /**
     * 获取电表抄表记录分页列表
     *
     * @param param 条件对象
     *
     * @return 分页列表
     */
    @GetMapping("/collectRecords/page")
    public Pagination<CollectRecordsResult> getCollectRecordsPage(@Valid CollectRecordsPageParam param) {
        return collectRecordsService.getCollectRecordsPage(param);
    }
}
