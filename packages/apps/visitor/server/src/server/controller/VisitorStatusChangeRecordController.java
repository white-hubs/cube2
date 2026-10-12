package com.guhecloud.cube.srv.gen.visitor.management.server.controller;

import com.guhecloud.cube.srv.gen.visitor.management.server.service.VisitorStatusChangeRecordService;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.VisitorStatusChangeRecordParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.VisitorStatusChangeRecordResult;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 访客状态变更记录
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VisitorStatusChangeRecordController {
    private final VisitorStatusChangeRecordService visitorStatusChangeRecordService;

    /**
     * 访客通行趋势
     *
     * @param param 条件
     *
     * @return 趋势
     */
    @GetMapping("/visitor/statusChange/passTrend")
    public List<VisitorStatusChangeRecordResult> visitorPassTrend(VisitorStatusChangeRecordParam param) {
        if (param == null || param.getPeriod() == null || param.getPeriod().getBegin() == null || param.getPeriod().getEnd() == null) {
            throw new ServiceException("时段不能为空");
        }
        return visitorStatusChangeRecordService.visitorPassTrend(param);
    }
}
