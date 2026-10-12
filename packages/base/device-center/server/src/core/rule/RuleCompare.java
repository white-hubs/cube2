package com.guhecloud.cube.base.device.center.core.rule;

import com.guhecloud.cube.base.device.center.core.model.BizDevice;
import com.guhecloud.cube.base.device.center.core.rule.entity.Rule;
import com.guhecloud.cube.base.device.center.core.rule.entity.RuleField;
import com.guhecloud.cube.base.device.center.core.rule.entity.RuleGroup;
import com.guhecloud.cube.base.device.center.core.rule.tag.enumeration.DeviceConditionFieldEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 规则条件对比工具类
 *
 * @author lanchun
 * @date 2025/4/9 20:00
 */
@Slf4j
public class RuleCompare {

    public static boolean hasRuleChanged(RuleGroup oldRuleGroup, RuleGroup newRuleGroup) {
        if (oldRuleGroup == null && newRuleGroup == null) {
            return false;
        }
        if (oldRuleGroup == null || newRuleGroup == null) {
            return true;
        }
        return areRulesEqual(oldRuleGroup.getRules(), newRuleGroup.getRules());
    }

    private static boolean areRulesEqual(List<Rule> rules1, List<Rule> rules2) {
        if (rules1.size() != rules2.size()) {
            return true;
        }
        for (int i = 0; i < rules1.size(); i++) {
            if (areRuleFieldsEqual(rules1.get(i).getRuleFields(), rules2.get(i).getRuleFields())) {
                return true;
            }
        }
        return false;
    }

    private static boolean areRuleFieldsEqual(List<RuleField> fields1, List<RuleField> fields2) {
        List<RuleField> sortedFields1 = fields1.stream()
                .sorted(Comparator.comparing(RuleField::getField))
                .collect(Collectors.toList());
        List<RuleField> sortedFields2 = fields2.stream()
                .sorted(Comparator.comparing(RuleField::getField))
                .collect(Collectors.toList());

        if (sortedFields1.size() != sortedFields2.size()) {
            return true;
        }

        for (int i = 0; i < sortedFields1.size(); i++) {
            RuleField field1 = sortedFields1.get(i);
            RuleField field2 = sortedFields2.get(i);

            if (!field1.getField().equals(field2.getField()) ||
                !field1.getOperator().equals(field2.getOperator()) ||
                !areValuesEqual(field1.getValue(), field2.getValue())) {
                return true;
            }
        }
        return false;
    }

    private static boolean areValuesEqual(Object value1, Object value2) {
        if (value1 == null && value2 == null) {
            return true;
        }
        if (value1 == null || value2 == null) {
            return false;
        }
        if (value1.getClass().isArray() && value2.getClass().isArray()) {
            return Arrays.deepEquals((Object[]) value1, (Object[]) value2);
        } else {
            return Objects.equals(value1, value2);
        }
    }

    public static boolean areDeviceMatch(BizDevice bizDevice, RuleGroup ruleGroup) {
        boolean matched = false;
        for (Rule rule : ruleGroup.getRules()) {
            matched = areaDeviceFieldMatch(bizDevice, rule.getRuleFields());
        }
        return matched;
    }

    public static boolean areaDeviceFieldMatch(BizDevice bizDevice, List<RuleField> ruleFields) {
        boolean matched = false;
        for (RuleField field : ruleFields) {
            Object value = field.getValue();
            Object deviceValue = getFieldValueFromDevice(bizDevice, field.getField());
            if (deviceValue == null || value == null) {
                matched = false;
                break;
            }
            switch (field.getOperator()) {
                case GREATER_THAN:
                    matched = ((Comparable) deviceValue).compareTo(value) > 0;
                    break;
                case GREATER_THAN_OR_EQUAL:
                    matched = ((Comparable) deviceValue).compareTo(value) >= 0;
                    break;
                case LESS_THAN:
                    matched = ((Comparable) deviceValue).compareTo(value) < 0;
                    break;
                case LESS_THAN_OR_EQUAL:
                    matched = ((Comparable) deviceValue).compareTo(value) <= 0;
                    break;
                case IN:
                    matched = field.getValue() instanceof List && ((List<?>) value).contains(deviceValue.toString());
                    break;
                case LIKE:
                    matched = deviceValue.toString().contains(value.toString());
                    break;
                case NOT_IN:
                    matched = value instanceof List && !((List<?>) value).contains(deviceValue);
                    break;
                case EQ:
                    matched = deviceValue.equals(value);
                    break;
                default:
                    matched = false;
                    break;
            }
            if (!matched) {
                break;
            }
        }
        return matched;
    }

    private static Object getFieldValueFromDevice(BizDevice bizDevice, DeviceConditionFieldEnum field) {
        switch (field) {
            case PRODUCT:
                return bizDevice.getDeviceIotProdId();
            case AREA:
                return bizDevice.getAreaId();
            case DEVICE:
                return bizDevice.getId();
            case DEVICE_NAME:
                return bizDevice.getDeviceName();
            default:
                return null;
        }
    }
}
