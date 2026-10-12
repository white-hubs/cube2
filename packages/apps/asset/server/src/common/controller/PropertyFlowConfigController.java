package com.guhecloud.cube.srv.gen.property.management.server.common.controller;

import com.guhecloud.cube.srv.gen.property.management.server.consumable.enumeration.PropertyApprovalBizEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import util.CustBeanUtil;
import vo.CommonEnumVO;
import vo.FormFieldVO;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 流程配置
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PropertyFlowConfigController {

    /**
     * 获取审批业务类型
     *
     * @return
     */
    @GetMapping("/consumable/approval/biz/type")
    public List<CommonEnumVO> getConsumableApprovalBizType() {
        return Arrays.stream(PropertyApprovalBizEnum.values()).map(e -> CommonEnumVO.builder()
                .code(e.name()).name(e.getDesc()).build()).collect(Collectors.toList());
    }

    /**
     * 获取业务字段
     *
     * @return
     */
    @GetMapping("/property/approval/biz/form/field")
    public List<FormFieldVO> getFormField(PropertyApprovalBizEnum type) {
        if (null == type) {
            return Collections.emptyList();
        }
        return type.getClazz() != null ? CustBeanUtil.getFormField(type.getClazz()) : Collections.emptyList();
    }
}
