package com.guhecloud.cube.customized.youxianqu.park.management.server.complaint.controller;

import com.guhecloud.cube.customized.youxianqu.park.management.server.common.vo.result.TrendResult;
import com.guhecloud.cube.customized.youxianqu.park.management.server.complaint.param.ComplaintQueryParam;
import com.guhecloud.cube.customized.youxianqu.park.management.server.complaint.result.ComplaintResult;
import com.guhecloud.cube.customized.youxianqu.park.management.server.complaint.service.ComplaintService;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 投诉接口
 *
 * @author lanchun
 * @date 2024/8/28 15:21
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ComplaintController {

    private final ComplaintService complaintService;

    /**
     * 投诉分页列表
     *
     * @param param 条件
     *
     * @return 分页
     */
    @GetMapping("/complaint/page")
    public Pagination<ComplaintResult> getPage(ComplaintQueryParam param) {
        return complaintService.getPage(param);
    }

    /**
     * 获取投诉趋势
     *
     * @param param 条件
     *
     * @return 趋势
     */
    @GetMapping("/complaint/stat/trend")
    public List<TrendResult> getTrend(ComplaintQueryParam param) {
        return complaintService.getTrend(param);
    }
}
