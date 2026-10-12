package com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces;

import com.guhecloud.cube.srv.gen.maintain.server.fault.enumeration.FaultApprovalBizEnum;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import util.CustBeanUtil;
import vo.CommonEnumVO;
import vo.FormFieldVO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 表单流程配置接口
 *
 * @author wql
 * @describe 表单流程配置接口
 * @date 2023/11/6 11:16
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FaultOrderConfigController {

    /**
     * 获取审批业务类型
     *
     * @Author wql
     * @Description 获取审批业务类型
     * @Date 11:27 2023/11/6
     **/
    @GetMapping("/fault/approval/biz/type")
    public List<CommonEnumVO> getApprovalBizType(String applicationCode) {
        List<CommonEnumVO> list = new ArrayList<>();
        FaultApprovalBizEnum faultApprovalBizEnum =
                ObjectUtils.isEmpty(applicationCode) || FaultApprovalBizEnum.FAULT.name().equals(applicationCode)
                ? FaultApprovalBizEnum.FAULT : FaultApprovalBizEnum.COMPLAINT;
        CommonEnumVO commonEnumVO = CommonEnumVO.builder()
                .code(faultApprovalBizEnum.name())
                .name(faultApprovalBizEnum.getDesc())
                .build();
        list.add(commonEnumVO);
        return list;
    }

    /**
     * 获取审批业务表单字段
     *
     * @Author wql
     * @Description 获取审批业务表单字段
     * @Date 11:27 2023/11/6
     **/
    @GetMapping("/fault/approval/biz/form/field")
    public List<FormFieldVO> getFormField(FaultApprovalBizEnum type) {
        if (null == type) {
            return Collections.emptyList();
        }
        return type.getClazz() != null ? CustBeanUtil.getFormField(type.getClazz()) : Collections.emptyList();
    }
}
