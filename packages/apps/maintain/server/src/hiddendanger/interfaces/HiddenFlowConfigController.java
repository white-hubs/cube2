package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces;

import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.enumeration.HiddenApprovalBizEnum;
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
 * 隐患单流程实例配置
 *
 * @author wql
 * @describe 隐患单流程实例配置
 * @date 2023/11/9 19:26
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HiddenFlowConfigController {

    /**
     * 获取审批业务类型
     *
     * @Author wql
     * @Description 获取审批业务类型
     * @Date 11:27 2023/11/6
     **/
    @GetMapping("/hidden/approval/biz/type")
    public List<CommonEnumVO> getApprovalBizType() {
        return Arrays.stream(HiddenApprovalBizEnum.values()).map(e -> CommonEnumVO.builder()
                .code(e.name()).name(e.getDesc()).build()).collect(Collectors.toList());
    }

    /**
     * 获取审批业务表单字段
     *
     * @Author wql
     * @Description 获取审批业务表单字段
     * @Date 11:27 2023/11/6
     **/
    @GetMapping("/hidden/approval/biz/form/field")
    public List<FormFieldVO> getFormField(HiddenApprovalBizEnum type) {
        if (null == type) {
            return Collections.emptyList();
        }
        return type.getClazz() != null ? CustBeanUtil.getFormField(type.getClazz()) : Collections.emptyList();
    }
}
