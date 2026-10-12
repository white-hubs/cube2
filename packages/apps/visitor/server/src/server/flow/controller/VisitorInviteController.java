package com.guhecloud.cube.srv.gen.visitor.management.server.flow.controller;

import com.guhecloud.cube.srv.gen.visitor.management.server.flow.enumeration.VisitorFlowEnum;
import com.guhecloud.cube.srv.gen.visitor.management.server.flow.service.VisitorInviteService;
import instance.command.SubmitInitFormCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.CustBeanUtil;
import vo.CommonEnumVO;
import vo.FormFieldVO;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/13 9:27
 * @Description: 访客邀请
 */

@RestController
@RequestMapping("/visitor/approval")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VisitorInviteController {

    private final VisitorInviteService visitorInviteService;

    /**
     * 访客邀请提交
     *
     * @param submitInitFormCommand 入参
     *
     * @return 流程实例id
     */
    @PostMapping("/submit")
    public String visitorInviteSubmit(@RequestBody SubmitInitFormCommand submitInitFormCommand) {
        return visitorInviteService.visitorInviteSubmit(submitInitFormCommand);
    }

    /**
     * 获取审批业务类型
     *
     * @return
     */
    @GetMapping("/biz/type")
    public List<CommonEnumVO> getApprovalBizType() {
        return Arrays.stream(VisitorFlowEnum.values()).map(e -> CommonEnumVO.builder()
                        .code(e.name()).name(e.getDesc()).build())
                .collect(Collectors.toList());
    }

    /**
     * 获取业务字段
     *
     * @return
     */
    @GetMapping("/biz/form/field")
    public List<FormFieldVO> getFormField(VisitorFlowEnum type) {
        return type.getClazz() != null ? CustBeanUtil.getFormField(type.getClazz()) : Collections.emptyList();
    }

}
