
package com.guhecloud.cube.srv.gen.appraisals.server.rule.vo;

import com.guhecloud.cube.srv.gen.appraisals.server.common.enums.AppraisalCalcTypeEnum;
import com.guhecloud.cube.srv.gen.appraisals.server.rule.dto.RangeConfigDTO;
import lombok.Data;

import java.util.List;

/**
 * 考核规则项 VO
 * 用于返回考核规则的评分项信息
 */
@Data
public class FormAppraisalRuleItemVO {
    /**
     * 规则项 ID
     */
    private Long id;
    /**
     * 规则项名称
     */
    private String itemName;
    /**
     * 字段标识
     */
    private String fieldKey;
    /**
     * 计算方式
     */
    private AppraisalCalcTypeEnum calcType;
    /**
     * 排序号
     */
    private Integer sort;
    /**
     * 扣分区间配置列表
     */
    private List<RangeConfigDTO> rangeConfig;
}
