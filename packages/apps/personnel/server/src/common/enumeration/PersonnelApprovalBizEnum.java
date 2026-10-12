package com.guhecloud.cube.srv.gen.personnel.management.server.common.enumeration;

import com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.po.AssessPO;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.po.InductionPO;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.po.LeaveOfficePO;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.po.LeaveOfficeProceduresPO;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.po.RecruitPO;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.po.RegularWorkerPO;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.po.RotationPostPO;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 审批业务类型枚举
 *
 * @author bq
 * @date 2023/02/22
 */
@Getter
public enum PersonnelApprovalBizEnum {
    PERSONNEL_RECRUIT("招聘申请", RecruitPO.class),
    PERSONNEL_INDUCTION("入职审批", InductionPO.class),
    PERSONNEL_PROBATION_ASSESS("试用考核", AssessPO.class),
    PERSONNEL_REGULAR_WORKER("转正申请", RegularWorkerPO.class),
    PERSONNEL_TRAIN("培训申请", null),
    PERSONNEL_LEAVE_OFFICE("离职审批", LeaveOfficePO.class),
    PERSONNEL_LEAVE_OFFICE_PROCEDURES("离职手续", LeaveOfficeProceduresPO.class),
    PERSONNEL_ROTATION_POST("轮岗申请", RotationPostPO.class);

    final String desc;
    final Class clazz;

    PersonnelApprovalBizEnum(String desc, Class clazz) {
        this.desc = desc;
        this.clazz = clazz;
    }

    public Class<?> getRequestType() {
        return clazz;
    }

    /**
     * 获取审批业务类型
     */
    public static PersonnelApprovalBizEnum getApprovalBizEnum(String businessType) {
        Optional<PersonnelApprovalBizEnum> approvalEnum = Arrays.stream(values())
                .filter(t -> t.name().equals(businessType))
                .findFirst();
        Assert.isNotFound(approvalEnum.isEmpty(), "未找到指定审批流程");
        return approvalEnum.get();
    }
}
