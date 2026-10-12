package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.function;

import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.ArrangeWorkClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.ArrangeWorkClassesPO;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.mzt.logapi.service.IParseFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wql
 * @describe 排班班次id转换
 * @date 2023/6/13 14:33
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ArrangeWorkClassesParseFunction implements IParseFunction {

    private final ArrangeWorkClassesMapper arrangeWorkClassesMapper;

    @Override
    public boolean executeBefore() {
        return true;
    }

    @Override
    public String functionName() {
        return "ARRANGE_WORK_CLASSES";
    }

    @Override
    public String apply(Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return "休息";
        }
        StringBuilder sb = new StringBuilder();
        String[] split = String.valueOf(value).split(",");
        for (String s : split) {
            ArrangeWorkClassesPO classesPO = arrangeWorkClassesMapper.selectById(s);
            if (ObjectUtils.isNotEmpty(classesPO)) {
                sb.append(classesPO.getName()).append(" ").append(classesPO.getStartWorkTime()).append("-")
                        .append(classesPO.getEndWorkTime()).append("、");
            }
        }
        return ObjectUtils.isEmpty(sb) ? "休息" : sb.substring(0, sb.length() - 1);
    }
}
