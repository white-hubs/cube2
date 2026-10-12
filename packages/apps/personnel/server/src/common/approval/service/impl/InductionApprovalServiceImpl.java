package com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.service.impl;

import com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.po.InductionPO;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.service.PersonnelApprovalService;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.enumeration.PersonnelApprovalBizEnum;
import com.guhecloud.cube.srv.gen.personnel.management.server.po.StaffPO;
import com.guhecloud.cube.srv.gen.personnel.management.server.querygateway.StaffQueryService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/22 13:54
 * @Description: 入职审批实现
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InductionApprovalServiceImpl implements PersonnelApprovalService<InductionPO> {

    private final StaffQueryService staffQueryService;

    @Override
    public PersonnelApprovalBizEnum getOperationType() {
        return PersonnelApprovalBizEnum.PERSONNEL_INDUCTION;
    }

    @Override
    public void preBusinessHandle(InductionPO inductionPO) {
        Assert.isParameterError(Objects.isNull(inductionPO), "入离职信息不能为空");
        Assert.isParameterError(StringUtils.isBlank(inductionPO.getMobile()), "电话号码不能为空");
        List<StaffPO> staffPOList = staffQueryService.lambdaQuery().eq(StaffPO::getMobile, inductionPO.getMobile())
                .list();
        Assert.isConflict(!CollectionUtils.isEmpty(staffPOList), "人员信息已存在,无法入职");
        if (StringUtils.isNotBlank(inductionPO.getIdCard())) {
            List<StaffPO> existStaff = staffQueryService.lambdaQuery().eq(StaffPO::getIdCard, inductionPO.getIdCard())
                    .list();
            Assert.isConflict(!CollectionUtils.isEmpty(existStaff), "身份证号已存在");
        }
    }

    @Override
    public void postBusinessHandle(InductionPO entity) {

    }
}
