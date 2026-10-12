package com.guhecloud.cube.base.flow.flowable.resolver.enumeration;

import com.guhecloud.cube.base.flow.flowable.resolver.dto.ConditionInfo;
import com.guhecloud.cube.base.flow.flowable.resolver.service.BpmnResolverService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.guhecloud.cube.base.flow.flowable.resolver.WorkFlowConstants.EXPRESSION_CLASS;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/7/12 11:00
 * @Description: 发起人枚举类型
 */

@Getter
public enum ValueTypeEnum implements BpmnResolverService {

    User("用户", 1L) {
        @Override
        public StringBuffer conditionExpression(ConditionInfo conditionInfo) {
            return buildConditionExpression(conditionInfo, User.code);
        }
    },
    Dept("部门", 2L) {
        @Override
        public StringBuffer conditionExpression(ConditionInfo conditionInfo) {
            return buildConditionExpression(conditionInfo, Dept.code);
        }
    },
    Role("角色", 3L) {
        @Override
        public StringBuffer conditionExpression(ConditionInfo conditionInfo) {
            return buildConditionExpression(conditionInfo, Role.code);
        }
    },

    //前端流引擎上显示的是职位
    Post("岗位", 4L) {
        @Override
        public StringBuffer conditionExpression(ConditionInfo conditionInfo) {
            return buildConditionExpression(conditionInfo, Post.code);
        }
    },
    //前端流引擎上显示的是岗位
    APPOINTMENT("职位", 5L) {
        @Override
        public StringBuffer conditionExpression(ConditionInfo conditionInfo) {
            return buildConditionExpression(conditionInfo, APPOINTMENT.code);
        }
    },

    OutSider("外部人员", 6L) {
        @Override
        public StringBuffer conditionExpression(ConditionInfo conditionInfo) {
            StringBuffer result = new StringBuffer();
            result.append(" " + EXPRESSION_CLASS + "isInsider(" + conditionInfo.getId() + "," + OutSider.name() + ") ");
            return result;
        }
    },
    Array("对象集合", 7L) {
        @Override
        public StringBuffer conditionExpression(ConditionInfo conditionInfo) {
            return getStringOrArrayExpression(conditionInfo);
        }
    },

    String("字符串", 8L) {
        @Override
        public StringBuffer conditionExpression(ConditionInfo conditionInfo) {
            return getStringOrArrayExpression(conditionInfo);
        }
    },
    FormDept("表单部门", 9L) {
        @Override
        public StringBuffer conditionExpression(ConditionInfo conditionInfo) {
            return buildConditionExpression(conditionInfo, FormDept.code);
        }
    };

    private final String typeName;
    private final Long code;

    ValueTypeEnum(String typeName, Long code) {
        this.typeName = typeName;
        this.code = code;
    }

    private static StringBuffer getStringOrArrayExpression(ConditionInfo conditionInfo) {
        StringBuffer result = new StringBuffer();
        String compare = conditionInfo.getCompare();
        if ("=".equals(compare)) {
            String str = StringUtils.join(",", conditionInfo.getValue());
            result.append(" " + EXPRESSION_CLASS + "strEquals(")
                    .append(conditionInfo.getId())
                    .append(",'").append(str)
                    .append("') ");
        } else {
            Optional<Object> objectOptional = conditionInfo.getValue().stream().filter(t -> t instanceof String)
                    .findFirst();
            String str;
            if (objectOptional.isPresent()) {
                str = StringUtils.join(",", conditionInfo.getValue());
            } else {
                str = conditionInfo.getValue().stream().map(t -> {
                    Map<String, Object> obj = (Map<String, Object>) t;
                    return java.lang.String.valueOf(obj.get("name"));
                }).collect(Collectors.joining(","));
            }
            result.append(" " + EXPRESSION_CLASS + "strContains(")
                    .append(conditionInfo.getId())
                    .append(",'")
                    .append(str)
                    .append("') ");
        }
        return result;
    }

    public StringBuffer buildConditionExpression(ConditionInfo conditionInfo, Long code) {
        StringBuffer result = new StringBuffer();
        List<String> filterIds = new ArrayList<>();
        for (Object o : conditionInfo.getValue()) {
            Map<String, Object> obj = (Map<String, Object>) o;
            filterIds.add(java.lang.String.valueOf(obj.get("id")));
        }
        String filterId = StringUtils.join(",", filterIds);
        if (CollectionUtils.isNotEmpty(filterIds)) {
            result.append(" " + EXPRESSION_CLASS + "userContains(" + conditionInfo.getId() + "," + code + "," + filterId + ") ");
        } else {
            result.append("empty");
        }
        return result;
    }

    public static Boolean containValueType(String valueType) {
        return Arrays.stream(ValueTypeEnum.values()).map(ValueTypeEnum::name).anyMatch(s -> s.equals(valueType));
    }

    public static String getTypeName(Long valueType) {
        Optional<ValueTypeEnum> first = Arrays.stream(ValueTypeEnum.values()).filter(t -> t.code.equals(valueType)).findFirst();

        return first.map(ValueTypeEnum::name).orElse(null);
    }

    public static List<Long> getContainsValueTypes() {
        List<Long> result = new ArrayList<>();
        result.add(User.getCode());
        result.add(FormDept.getCode());
        return result;
    }
}
