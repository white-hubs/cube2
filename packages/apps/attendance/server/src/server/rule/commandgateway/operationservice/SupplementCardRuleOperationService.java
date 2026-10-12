package com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.operationservice;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.SupplementCardRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.SupplementCardRulePO;
import com.mzt.logapi.starter.annotation.LogRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wql
 * @describe 补卡规则操作
 * @date 2023/6/13 17:52
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SupplementCardRuleOperationService extends ServiceImpl<SupplementCardRuleMapper, SupplementCardRulePO> {

    /**
     * @Author wql
     * @Description 新增补卡规则操作
     * @Date 17:55 2023/6/13
     **/
    @LogRecord(success = "新增:【{{#command.name}}】", subType = "SUPPLEMENT_CARD_RULE", subTypeName = "补卡规则", operationType = "ADD",
               type = "ATTENDANCE", typeName = "考勤系统", bizNo = "{{#command.id}}"
    )
    public void insertOperation(SupplementCardRulePO command) {}

    /**
     * @Author wql
     * @Description 更新补卡规则操作
     * @Date 18:02 2023/6/13
     **/
    @LogRecord(success = "更新:【{{#newPO.name}}】,{_DIFF{#oldPO,#newPO}}", subType = "SUPPLEMENT_CARD_RULE", operationType = "UPDATE",
               subTypeName = "补卡规则", type = "ATTENDANCE", typeName = "考勤系统", bizNo = "{{#newPO.id}}"
    )
    public void updateOperation(SupplementCardRulePO oldPO, SupplementCardRulePO newPO) {

    }

    /**
     * @Author wql
     * @Description 删除补卡规则操作
     * @Date 18:04 2023/6/13
     **/
    @LogRecord(success = "删除:【{{#command.name}}】", subType = "SUPPLEMENT_CARD_RULE", operationType = "DELETE",
               subTypeName = "补卡规则", type = "ATTENDANCE", typeName = "考勤系统", bizNo = "{{#command.id}}"
    )
    public void deleteOperation(SupplementCardRulePO command) {

    }
}
