package com.guhecloud.cube.biz.daily.task.record.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DailyGroupingUtils {
    /**
     * 按多个字段对列表进行分组
     *
     * @param list      待分组的数据列表
     * @param groupKeys 多个字段提取函数，例如：po -> po.getTenantId(), po -> po.getStatus()
     * @param <T>       数据类型（如 DailyRecordPO）
     *
     * @return 分组结果：Map<List<Object>, List<T>>
     */
    @SafeVarargs
    public static <T> Map<List<Object>, List<T>> groupByFields(
            List<T> list,
            Function<T, ?>... groupKeys
                                                              ) {

        if (list == null || list.isEmpty()) {
            return Collections.emptyMap();
        }

        return list.stream()
                .collect(Collectors.groupingBy(item ->
                                                       Arrays.stream(groupKeys)
                                                               .map(keyFunc -> keyFunc.apply(item))
                                                               .collect(Collectors.toList())
                                              ));
    }
}
