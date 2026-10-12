package com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.operationservice;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.DutyWorkMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.DutyWorkPO;
import com.mzt.logapi.starter.annotation.LogRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wql
 * @describe 人员值班操作
 * @date 2023/6/14 11:32
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DutyWorkOperationService extends ServiceImpl<DutyWorkMapper, DutyWorkPO> {
    /**
     * @Author wql
     * @Description 人员值班新增操作
     * @Date 11:34 2023/6/14
     **/
    @LogRecord(success = "新增:{{#po.staffName}} {DATE{#po.date}} 【{ARRANGE_WORK_CLASSES{#po.dutyWorkClassesIds}}】", subType = "BE_ON_DUTY",
               subTypeName = "值班", operationType = "ADD", type = "ATTENDANCE", typeName = "考勤系统", bizNo = "{{#po.id}}"
    )
    public void insertOperation(DutyWorkPO po) {
        log.info("人员值班新增操作...");
    }

    /**
     * @Author wql
     * @Description 值班更新操作
     * @Date 11:38 2023/6/14
     **/
    @LogRecord(success = "修改:{{#newPO.staffName}} {DATE{#newPO.date}}:{_DIFF{#oldPO,#newPO}}", subType = "BE_ON_DUTY",
               subTypeName = "值班", operationType = "UPDATE", type = "ATTENDANCE", typeName = "考勤系统", bizNo = "{{#newPO.id}}"
    )
    public void updateOperation(DutyWorkPO oldPO, DutyWorkPO newPO) {
        log.info("人员值班更新操作...");
    }
}
