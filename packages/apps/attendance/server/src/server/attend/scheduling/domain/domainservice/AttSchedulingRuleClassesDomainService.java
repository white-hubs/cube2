package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.domain.domainservice;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param.AttSchedulingRuleClassesParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.result.AttSchedulingRuleClassesResult;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 排班规则班次领域服务
 */
public class AttSchedulingRuleClassesDomainService {

    /**
     * 比较两个规则的排班是否相等
     *
     * @param paramList 参数
     * @param poList    对比对象
     *
     * @return true相等，false不相等
     */
    public static boolean equalsClassesList(List<AttSchedulingRuleClassesParam> paramList, List<AttSchedulingRuleClassesResult> poList) {
        int paramSize = paramList == null ? 0 : paramList.size();
        int poSize = poList == null ? 0 : poList.size();
        if (paramSize != poSize) {
            return false;
        }
        if (paramSize == 0) {
            return true;
        }
        Map<String, AttSchedulingRuleClassesParam> paramMap = paramList.stream()
                .collect(Collectors.toMap(c -> c.getId() + c.getWeek(), p -> p));
        AtomicBoolean rsBool = new AtomicBoolean(true);
        poList.forEach(po -> {
            AttSchedulingRuleClassesParam param = paramMap.get(po.getId() + po.getWeek());
            if (param == null || !Objects.equals(param.getWeek(), po.getWeek())) {
                rsBool.set(false);
            }
        });
        return rsBool.get();
    }
}
