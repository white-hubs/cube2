package com.guhecloud.cube.srv.gen.personnel.management.server.common.interfaces;

import com.guhecloud.cube.srv.gen.personnel.management.server.common.commandgateway.FlowFormDataCommandService;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.enumeration.AssessResultEnum;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.enumeration.PersonnelApprovalBizEnum;
import instance.command.SubmitInitFormCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import util.CustBeanUtil;
import vo.CommonEnumVO;
import vo.FormFieldVO;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 流程审批通用接口
 */
@Slf4j
@RestController(value = "personnelCommonController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommonController {

    private final String ENUM_TYPE_ASSESS_RESULT = "assessResult";
    private final FlowFormDataCommandService flowFormDataCommandService;

    /**
     * 发起审批
     */
    @PostMapping("/personnel/submit")
    public String submitApproval(@RequestBody SubmitInitFormCommand submitInitFormCommand) {
        return flowFormDataCommandService.submitApproval(submitInitFormCommand);
    }

    /**
     * 获取审批业务类型
     *
     * @return
     */
    @GetMapping("/approval/biz/type")
    public List<CommonEnumVO> getApprovalBizType() {
        return Arrays.stream(PersonnelApprovalBizEnum.values()).map(e -> CommonEnumVO.builder()
                .code(e.name()).name(e.getDesc()).build()).collect(Collectors.toList());
    }

    /**
     * 获取业务字段
     *
     * @return
     */
    @GetMapping("/approval/biz/form/field")
    public List<FormFieldVO> getFormField(PersonnelApprovalBizEnum type) {
        if (type == null) {
            return Collections.emptyList();
        }
        return type.getClazz() != null ? CustBeanUtil.getFormField(type.getClazz()) : Collections.emptyList();
    }

    /**
     * 获取枚举类业务控件
     *
     * @return
     */
    @GetMapping("/approval/biz/form/control/enum")
    public List<Map<String, String>> getFormField(String type) {
        switch (type) {
            case ENUM_TYPE_ASSESS_RESULT:
                return AssessResultEnum.getList();
        }
        return Collections.emptyList();
    }

}
