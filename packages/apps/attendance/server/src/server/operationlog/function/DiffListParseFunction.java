package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.function;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.google.common.collect.Sets;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.service.IParseFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author wql
 * @describe 列表转换
 * @date 2023/6/19 16:21
 */
public class DiffListParseFunction implements IParseFunction {
    @Override
    public boolean executeBefore() {
        return true;
    }

    @Override
    public String functionName() {
        return "DIFF_LIST";
    }

    @Override
    @SuppressWarnings("unchecked")
    public String apply(Object value) {
        if (ObjectUtils.isEmpty(value)) {
            return "";
        }
        List<String> oldList = (List<String>) LogRecordContext.getVariable("oldList");
        List<String> newList = (List<String>) LogRecordContext.getVariable("newList");
        oldList = oldList == null ? new ArrayList<>() : oldList;
        newList = newList == null ? new ArrayList<>() : newList;
        Set<String> deletedSets = Sets.difference(Sets.newHashSet(oldList), Sets.newHashSet(newList));
        Set<String> addSets = Sets.difference(Sets.newHashSet(newList), Sets.newHashSet(oldList));
        StringBuilder stringBuilder = new StringBuilder();
        if (CollectionUtils.isNotEmpty(addSets)) {
            stringBuilder.append("新增了 <b>").append(value).append("</b>：");
            for (String item : addSets) {
                stringBuilder.append(item).append("，");
            }
        }
        if (CollectionUtils.isNotEmpty(deletedSets)) {
            stringBuilder.append("删除了 <b>").append(value).append("</b>：");
            for (String item : deletedSets) {
                stringBuilder.append(item).append("，");
            }
        }
        return StringUtils.isBlank(stringBuilder) ? null : stringBuilder.substring(0, stringBuilder.length() - 1);
    }
}
