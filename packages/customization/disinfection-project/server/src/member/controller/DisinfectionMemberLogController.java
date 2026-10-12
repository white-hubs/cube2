package com.guhecloud.cube.srv.gen.disinfection.server.member.controller;

import com.guhecloud.cube.srv.gen.disinfection.server.member.param.MemberLogQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberLogService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.MemberLogResult;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DisinfectionMemberLogController {
    private final DisinfectionMemberLogService memberLogService;

    /**
     * 分页根据类型查询不同的类型日志
     */
    @GetMapping("/bus/member/log/page")
    public Pagination<MemberLogResult> getMemberLogPage(@Valid @ModelAttribute MemberLogQueryParam param) {
        return memberLogService.getMemberLogPage(param);
    }
}
