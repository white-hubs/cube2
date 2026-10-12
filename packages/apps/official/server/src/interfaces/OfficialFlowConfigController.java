package com.guhecloud.cube.srv.gen.official.management.server.interfaces;

import com.guhecloud.cube.srv.gen.official.management.server.enumeration.OfficialApprovalBizEnum;
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
 * 通用接口 接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OfficialFlowConfigController {

    /**
     * 获取审批业务类型
     *
     * @return
     */
    @GetMapping("/official/approval/biz/type")
    public List<CommonEnumVO> getApprovalBizType() {
        return Arrays.stream(OfficialApprovalBizEnum.values()).map(e -> CommonEnumVO.builder()
                .code(e.name()).name(e.getDesc()).build()).collect(Collectors.toList());
    }

    /**
     * 获取业务字段
     *
     * @return
     */
    @GetMapping("/official/approval/biz/form/field")
    public List<FormFieldVO> getFormField(OfficialApprovalBizEnum type) {
        return type.getClazz() != null ? CustBeanUtil.getFormField(type.getClazz()) : Collections.emptyList();
    }
}
