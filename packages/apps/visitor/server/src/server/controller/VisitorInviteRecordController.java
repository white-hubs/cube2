package com.guhecloud.cube.srv.gen.visitor.management.server.controller;

import com.guhecloud.cube.srv.gen.visitor.management.server.service.VisitorInviteRecordService;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.VisitorInviteRecordParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.VisitorInviteRecordResult;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/24 16:58
 * @Description: 访客邀约记录Api
 */

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/visitor/invite/record")
public class VisitorInviteRecordController {

    private final VisitorInviteRecordService visitorInviteRecordService;

    /**
     * 邀约记录分页列表
     *
     * @param param 入参
     *
     * @return 预约记录
     */
    @GetMapping("/page")
    public Pagination<VisitorInviteRecordResult> visitorInviteRecordPage(VisitorInviteRecordParam param) {
        return visitorInviteRecordService.visitorInviteRecordPage(param);
    }

}
