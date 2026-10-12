
package com.guhecloud.cube.srv.gen.appraisals.server.common.util;

import com.guhecloud.cube.srv.gen.appraisals.server.rule.dto.ContentCommand;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.dto.ItemCommand;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.dto.RangeConfigDTO;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.param.FormAppraisalRuleCommand;
import com.guhecloud.platform.base.standard.exception.Assert;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public final class AppraisalRuleValidator {
    private AppraisalRuleValidator() {
    }

    public static void validate(FormAppraisalRuleCommand command) {
        Assert.isParameterError(command == null, "规则参数不能为空");
        Assert.isParameterError(command.getName() == null || command.getName().isBlank(), "规则名称不能为空");
        Assert.isParameterError(command.getPeriodType() == null, "周期类型不能为空");
        Assert.isParameterError(
                command.getResponsibleDepartmentIds() == null || command.getResponsibleDepartmentIds().isEmpty(),
                "责任部门不能为空"
        );
        Assert.isParameterError(
                command.getTotalScore() == null || command.getSpotCheckScore() == null || command.getInspectionScore() == null,
                "分值不能为空"
        );
        Assert.isParameterError(
                command.getSpotCheckScore().add(command.getInspectionScore()).compareTo(command.getTotalScore()) != 0,
                "抽查分与检查分之和必须等于总分"
        );
        Assert.isParameterError(command.getContents() == null || command.getContents().isEmpty(), "规则内容不能为空");
        for (ContentCommand content : command.getContents()) {
            Assert.isParameterError(content.getItems() == null || content.getItems().isEmpty(), "规则项不能为空");
            for (ItemCommand item : content.getItems()) {
                validateRanges(item.getRangeConfig());
            }
        }
    }

    private static void validateRanges(List<RangeConfigDTO> ranges) {
        Assert.isParameterError(ranges == null || ranges.isEmpty(), "扣分区间不能为空");
        List<RangeConfigDTO> sorted = ranges.stream().sorted(Comparator.comparing(RangeConfigDTO::getStartValue)).toList();
        BigDecimal previousEnd = null;
        for (RangeConfigDTO range : sorted) {
            Assert.isParameterError(
                    range.getStartValue() == null || range.getEndValue() == null || range.getDeductScore() == null,
                    "扣分区间字段不能为空"
            );
            Assert.isParameterError(range.getStartValue().compareTo(range.getEndValue()) > 0, "扣分区间开始值不能大于结束值");
            Assert.isParameterError(range.getDeductScore().compareTo(BigDecimal.ZERO) < 0, "扣分不能小于0");
            Assert.isParameterError(
                    Objects.nonNull(previousEnd) && range.getStartValue().compareTo(previousEnd) < 0,
                    "扣分区间不能重叠"
            );
            previousEnd = range.getEndValue();
        }
    }
}
