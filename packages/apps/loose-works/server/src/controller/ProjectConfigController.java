package com.guhecloud.cube.srv.gen.loose.works.controller;

import com.guhecloud.cube.srv.gen.loose.works.enumeration.ProjectApprovalBizEnum;
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
 * 表单流程配置接口
 *
 * @author wql
 * @describe 表单流程配置接口
 * @date 2023/9/18 15:39
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectConfigController {

    /**
     * 获取审批业务类型
     *
     * @Author wql
     * @Description 获取审批业务类型
     * @Date 16:01 2023/9/18
     **/
    @GetMapping("/project/approval/biz/type")
    public List<CommonEnumVO> getApprovalBizType() {
        return Arrays.stream(ProjectApprovalBizEnum.values()).map(e -> CommonEnumVO.builder()
                .code(e.name()).name(e.getDesc()).build()).collect(Collectors.toList());
    }

    /**
     * 获取审批业务表单字段
     *
     * @Author wql
     * @Description 获取审批业务表单字段
     * @Date 16:02 2023/9/18
     **/
    @GetMapping("/project/approval/biz/form/field")
    public List<FormFieldVO> getFormField(ProjectApprovalBizEnum type) {
        if (type == null) {
            return Collections.emptyList();
        }
        return type.getClazz() != null ? CustBeanUtil.getFormField(type.getClazz()) : Collections.emptyList();
    }

}
