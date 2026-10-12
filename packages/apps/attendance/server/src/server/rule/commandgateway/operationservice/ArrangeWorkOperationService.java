package com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.operationservice;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.ArrangeWorkMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.ArrangeWorkPO;
import com.mzt.logapi.starter.annotation.LogRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author wql
 * @describe 排班班次 操作
 * @date 2023/6/13 14:14
 */
@Slf4j
@Service
public class ArrangeWorkOperationService extends ServiceImpl<ArrangeWorkMapper, ArrangeWorkPO> {

    /**
     * @Author wql
     * @Description 新增人员排班
     * @Date 14:57 2023/6/13
     **/
    @LogRecord(
            success = "新增:{{#arrangeWorkPO.staffName}} {DATE{#arrangeWorkPO.date}} 【{ARRANGE_WORK_CLASSES{#arrangeWorkPO.arrangeWorkClassesIds}}】",
            subType = "ARRANGE_WORK", subTypeName = "人员排班", operationType = "ADD", type = "ATTENDANCE",
            typeName = "考勤系统", bizNo = "{{#arrangeWorkPO.id}}"
    )
    public void insertOperation(ArrangeWorkPO arrangeWorkPO) {
        log.info("新增人员排班...");
    }

    /**
     * @Author wql
     * @Description 修改人员排班
     * @Date 15:01 2023/6/13
     **/
    @LogRecord(success = "修改:{{#newPO.staffName}} {DATE{#newPO.date}}:{_DIFF{#oldPO,#newPO}}", subType = "ARRANGE_WORK",
               subTypeName = "人员排班", operationType = "UPDATE", type = "ATTENDANCE", typeName = "考勤系统", bizNo = "{{#newPO.id}}"
    )
    public void updateArrangeWork(ArrangeWorkPO oldPO, ArrangeWorkPO newPO) {
        log.info("修改人员排班...");
    }
}
