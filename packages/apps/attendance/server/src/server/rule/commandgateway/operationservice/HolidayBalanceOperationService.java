package com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.operationservice;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper.HolidayBalanceMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po.HolidayBalancePO;
import com.mzt.logapi.starter.annotation.LogRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wql
 * @describe 假期余额修改
 * @date 2023/6/13 16:31
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HolidayBalanceOperationService extends ServiceImpl<HolidayBalanceMapper, HolidayBalancePO> {

    /**
     * @Author wql
     * @Description 新增假期余额
     * @Date 16:54 2023/6/13
     **/
    @LogRecord(success = "新增:{USERNAME{#po.userId}} {{#po.holidayType.label}} 【剩余余额:{{#po.remainBalance}},占用余额:{{#po.occupyBalance}}】"
            , subType = "HOLIDAY_BALANCE", operationType = "ADD", subTypeName = "假期余额", type = "ATTENDANCE", typeName = "考勤系统",
               bizNo = "{{#po.id}}"
    )
    public void insert(HolidayBalancePO po) {
        log.info("新增假期余额...");
    }

    /**
     * @Author wql
     * @Description 更新假期余额
     * @Date 16:54 2023/6/13
     **/
    @LogRecord(success = "修改:{USERNAME{#newPO.userId}} {{#newPO.holidayType.label}} {_DIFF{#oldPO,#newPO}} <br/>备注【{{#newPO.remark}}】"
            , subType = "HOLIDAY_BALANCE", operationType = "ADD", subTypeName = "假期余额", type = "ATTENDANCE", typeName = "考勤系统",
               bizNo = "{{#newPO.id}}"
    )
    public void updateHolidayBalance(HolidayBalancePO oldPO, HolidayBalancePO newPO) {
        log.info("更新假期余额...");
    }
}
