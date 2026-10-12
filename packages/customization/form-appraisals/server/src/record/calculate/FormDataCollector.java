package com.guhecloud.cube.srv.gen.appraisals.server.record.calculate;

import com.guhecloud.cube.base.flow.client.FormClient;
import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalSourceTypeEnum;
import com.guhecloud.cube.srv.gen.appraisals.server.integration.FormAppraisalFormIntegrationService;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.entity.FormAppraisalRuleContent;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.entity.FormAppraisalRuleItem;
import com.guhecloud.platform.base.model.Period;
import instance.command.ProcessStartedMessageAttrCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

/**
 * 表单数据采集器
 * 从表单系统中采集数据进行计算
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormDataCollector implements AppraisalDataCollector {
    private final FormAppraisalFormIntegrationService formIntegrationService;
    private final FormClient formClient;

    /**
     * 判断是否支持表单数据源
     *
     * @param sourceType 数据来源类型
     *
     * @return true-支持
     */
    @Override
    public boolean supports(AppraisalSourceTypeEnum sourceType) {
        // 仅当来源类型为 FORM 时支持
        return sourceType == AppraisalSourceTypeEnum.FORM;
    }

    /**
     * 采集表单数据
     * 根据计算类型返回不同的统计值（数量、求和、平均值、单个值）
     *
     * @param content 规则内容
     * @param item    规则项
     * @param period  考核周期
     *
     * @return 采集结果
     */
    @Override
    public AppraisalCollectResult collect(FormAppraisalRuleContent content, FormAppraisalRuleItem item, Period period) {
        var formFlowDetailVO = formClient.get(Long.valueOf(content.getSourceRefId()));
        if (formFlowDetailVO != null) {
            content.setSourceRefName(formFlowDetailVO.getName());
            String fieldName = formFlowDetailVO.getFormFieldList().stream()
                    .filter(field -> Objects.equals(field.getId(), item.getFieldKey()))
                    .map(vo.FormFieldVO::getName)
                    .findFirst()
                    .orElse(null);
            item.setFieldName(fieldName);
        }

        // 从表单集成服务获取指定表单在考核周期内的所有实例
        List<ProcessStartedMessageAttrCommand> instances = formIntegrationService.listFormInstances(content.getSourceRefCode(), period);
        // 根据计算类型计算不同的统计值
        BigDecimal value = switch (item.getCalcType()) {
            // 表单数量：直接返回实例列表的大小
            case FORM_COUNT -> BigDecimal.valueOf(instances.size());
            // 字段求和：提取所有实例的指定字段值并求和
            case FIELD_SUM -> instances.stream()
                    .map(instance -> formIntegrationService.extractNumericValue(instance, item.getFieldKey()))
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            // 字段平均值：调用专门的平均值计算方法
            case FIELD_AVG -> average(instances, item.getFieldKey());
            // 字段单值：取第一个非空的字段值
            case FIELD_VALUE -> instances.stream()
                    .map(instance -> formIntegrationService.extractNumericValue(instance, item.getFieldKey()))
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(BigDecimal.ZERO);
            // 默认情况返回 0
            default -> BigDecimal.ZERO;
        };
        // 返回采集结果，包含数值化结果和字符串形式的原始值
        return new AppraisalCollectResult(value, value.stripTrailingZeros().toPlainString());
    }

    /**
     * 计算平均值
     * 遍历所有实例，累加指定字段的数值后除以有效数量
     *
     * @param instances 表单实例列表
     * @param fieldKey  字段标识
     *
     * @return 平均值
     */
    private BigDecimal average(List<ProcessStartedMessageAttrCommand> instances, String fieldKey) {
        // 初始化总和为 0
        BigDecimal total = BigDecimal.ZERO;
        // 初始化有效计数器为 0
        int count = 0;
        // 遍历所有表单实例
        for (ProcessStartedMessageAttrCommand instance : instances) {
            // 从实例中提取指定字段的数值
            BigDecimal value = formIntegrationService.extractNumericValue(instance, fieldKey);
            // 只处理非空值
            if (value != null) {
                // 累加到总和
                total = total.add(value);
                // 有效数量加 1
                count++;
            }
        }
        // 如果没有有效数据，返回 0
        if (count == 0) {
            return BigDecimal.ZERO;
        }
        // 计算平均值：总和除以数量，保留 4 位小数，四舍五入
        return total.divide(BigDecimal.valueOf(count), 4, RoundingMode.HALF_UP);
    }
}
