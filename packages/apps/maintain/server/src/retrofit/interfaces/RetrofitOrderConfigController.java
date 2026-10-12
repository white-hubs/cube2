package com.guhecloud.cube.srv.gen.maintain.server.retrofit.interfaces;

import com.guhecloud.cube.srv.gen.maintain.server.retrofit.enumeration.RetrofitApprovalBizEnum;
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
 * 更新改造2.0配置
 *
 * @author lanchun
 * @date 2024/1/8 15:33
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RetrofitOrderConfigController {

    /**
     * 获取审批业务类型
     *
     * @return
     */
    @GetMapping("/retrofit/approval/biz/type")
    public List<CommonEnumVO> getApprovalBizType() {
        return Arrays.stream(RetrofitApprovalBizEnum.values()).map(e -> CommonEnumVO.builder()
                .code(e.name()).name(e.getDesc()).build()).collect(Collectors.toList());
    }

    /**
     * 获取审批业务表单字段
     *
     * @param type
     *
     * @return
     */
    @GetMapping("/retrofit/approval/biz/form/field")
    public List<FormFieldVO> getFormField(RetrofitApprovalBizEnum type) {
        if (null == type) {
            return Collections.emptyList();
        }
        return type.getClazz() != null ? CustBeanUtil.getFormField(type.getClazz()) : Collections.emptyList();
    }
}
