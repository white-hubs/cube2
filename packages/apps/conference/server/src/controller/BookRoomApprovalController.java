package com.guhecloud.cube.srv.gen.conferencemanage.server.controller;

import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.BookRoomApprovalEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
 * @Date: 2025/4/7 11:40
 * @Description: 会议室预定审批api
 */

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/book-room/approval")
public class BookRoomApprovalController {

    /**
     * 获取审批业务类型
     *
     * @return
     */
    @GetMapping("/biz/type")
    public List<CommonEnumVO> getApprovalBizType() {
        return Arrays.stream(BookRoomApprovalEnum.values()).map(e -> CommonEnumVO.builder()
                        .code(e.name()).name(e.getDesc()).build())
                .collect(Collectors.toList());
    }

    /**
     * 获取业务字段
     *
     * @return
     */
    @GetMapping("/biz/form/field")
    public List<FormFieldVO> getFormField(BookRoomApprovalEnum type) {
        return type.getClazz() != null ? CustBeanUtil.getFormField(type.getClazz()) : Collections.emptyList();
    }
}
