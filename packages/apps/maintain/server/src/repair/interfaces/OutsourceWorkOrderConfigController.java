package com.guhecloud.cube.srv.gen.maintain.server.repair.interfaces;

import com.guhecloud.cube.srv.gen.maintain.server.repair.enumeration.OutsourceApprovalBizEnum;
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
 * 委外维修流程配置
 *
 * @author wql
 * @describe 委外维修流程配置
 * @date 2024/1/8 14:11
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OutsourceWorkOrderConfigController {
    /**
     * 获取审批业务类型
     *
     * @Author wql
     * @Description 获取审批业务类型
     * @Date 11:27 2023/11/6
     **/
    @GetMapping("/outsource/approval/biz/type")
    public List<CommonEnumVO> getApprovalBizType() {
        return Arrays.stream(OutsourceApprovalBizEnum.values()).map(e -> CommonEnumVO.builder()
                .code(e.name()).name(e.getDesc()).build()).collect(Collectors.toList());
    }

    /**
     * 获取审批业务表单字段
     *
     * @Author wql
     * @Description 获取审批业务表单字段
     * @Date 11:27 2023/11/6
     **/
    @GetMapping("/outsource/approval/biz/form/field")
    public List<FormFieldVO> getFormField(OutsourceApprovalBizEnum type) {
        if (null == type) {
            return Collections.emptyList();
        }
        return type.getClazz() != null ? CustBeanUtil.getFormField(type.getClazz()) : Collections.emptyList();
    }
}
