package com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.operationservice;

import com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.vo.HolidayOperation;
import com.mzt.logapi.starter.annotation.LogRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wql
 * @describe 假期类型操作
 * @date 2023/6/14 11:54
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HolidayOperationService {

    @LogRecord(success = "更新:{_DIFF{#oldLog,#newLog}}", subType = "HOLIDAY_TYPE", subTypeName = "假期类型", operationType = "UPDATE",
               typeName = "考勤系统", type = "ATTENDANCE", bizNo = "{{#newLog.id}}"
    )
    public void updateOperation(HolidayOperation oldLog, HolidayOperation newLog) {

    }
}
