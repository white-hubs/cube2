package com.guhecloud.cube.srv.gen.business.info.sync.server.hotle.controller;

import com.guhecloud.cube.base.flow.client.param.query.ProcessInstancePageQuery;
import com.guhecloud.cube.srv.gen.business.info.sync.server.hotle.enumeration.HotelReservationApprovalEnum;
import com.guhecloud.cube.srv.gen.business.info.sync.server.hotle.service.HotelReservationOrderService;
import com.guhecloud.cube.srv.gen.business.info.sync.server.hotle.vo.HotelReservationFromVO;
import com.guhecloud.platform.base.model.Pagination;
import instance.command.SubmitInitFormCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 * 酒店预约
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/5/27 17:27
 * @Description:
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/hotel-reservation/approval")
public class HotelReservationOrderController {

    private final HotelReservationOrderService hotelReservationOrderService;

    /**
     * 酒店预定
     */
    @PostMapping("/submit")
    public String reserveHotelSubmit(@RequestBody SubmitInitFormCommand submitInitFormCommand) {
        return hotelReservationOrderService.reserveHotelSubmit(submitInitFormCommand);
    }

    /**
     * 酒店审批记录
     */
    @GetMapping("/approval-record")
    public Pagination<HotelReservationFromVO> getApprovalRecord(ProcessInstancePageQuery processInstancePageQuery) {
        return hotelReservationOrderService.getApprovalRecord(processInstancePageQuery);
    }

    /**
     * 获取审批业务类型
     *
     * @return
     */
    @GetMapping("/biz/type")
    public List<CommonEnumVO> getApprovalBizType() {
        return Arrays.stream(HotelReservationApprovalEnum.values()).map(e -> CommonEnumVO.builder()
                        .code(e.name()).name(e.getDesc()).build())
                .collect(Collectors.toList());
    }

    /**
     * 获取业务字段
     *
     * @return
     */
    @GetMapping("/biz/form/field")
    public List<FormFieldVO> getFormField(HotelReservationApprovalEnum type) {

        return type != null && type.getClazz() != null ? CustBeanUtil.getFormField(type.getClazz()) : Collections.emptyList();
    }
}
