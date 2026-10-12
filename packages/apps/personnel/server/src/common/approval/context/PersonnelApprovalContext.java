package com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.context;

import com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.service.PersonnelApprovalService;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.enumeration.PersonnelApprovalBizEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/22 14:14
 * @Description:
 */
@Component
public class PersonnelApprovalContext {

    private final Map<PersonnelApprovalBizEnum, PersonnelApprovalService<?>> operationServices;

    @Autowired
    public PersonnelApprovalContext(List<PersonnelApprovalService<?>> services) {
        this.operationServices = services.stream()
                .collect(Collectors.toMap(
                        PersonnelApprovalService::getOperationType,
                        Function.identity()
                                         ));
    }

    @SuppressWarnings("unchecked")
    public <T> PersonnelApprovalService<T> dispatch(PersonnelApprovalBizEnum approvalBizEnum) {
        PersonnelApprovalService<T> service = (PersonnelApprovalService<T>) operationServices.get(approvalBizEnum);
        if (service == null) {
            throw new RuntimeException("未找到指定审批流程");
        }
        return service;
    }
}
