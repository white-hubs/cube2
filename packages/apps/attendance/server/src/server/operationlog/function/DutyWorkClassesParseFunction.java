package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.function;

import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.DutyWorkClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.DutyWorkClassesPO;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.mzt.logapi.service.IParseFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wql
 * @describe 值班班次转换
 * @date 2023/8/22 15:20
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DutyWorkClassesParseFunction implements IParseFunction {

    private final DutyWorkClassesMapper dutyWorkClassesMapper;

    @Override
    public boolean executeBefore() {
        return true;
    }

    @Override
    public String functionName() {
        return "DUTY_WORK_CLASSES";
    }

    @Override
    public String apply(Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return "休息";
        }
        StringBuilder sb = new StringBuilder();
        String[] split = String.valueOf(value).split(",");
        for (String s : split) {
            DutyWorkClassesPO classesPO = dutyWorkClassesMapper.selectById(s);
            if (ObjectUtils.isNotEmpty(classesPO)) {
                sb.append(classesPO.getName()).append(" ").append(classesPO.getStartWorkTime()).append("-")
                        .append(classesPO.getEndWorkTime()).append("、");
            }
        }
        return ObjectUtils.isEmpty(sb) ? "休息" : sb.substring(0, sb.length() - 1);
    }
}
