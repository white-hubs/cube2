package com.guhecloud.cube.srv.gen.attendance.management.server.rule.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import com.mzt.logapi.starter.annotation.DiffLogField;
import lombok.Data;

/**
 * @author bq
 * @date 2022/11/10
 * @description 补卡规则
 */
@Data
@TableName("supplement_card_rule")
public class SupplementCardRulePO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 名称
     */
    @DiffLogField(name = "名称")
    private String name;

    /**
     * 允许补卡类型
     */
    @DiffLogField(name = "补卡类型", function = "CLOCK_STATUS")
    private String type;

    /**
     * 每月允许补卡次数
     */
    @DiffLogField(name = "每月允许补卡次数")
    private Integer times;

    /**
     * 几日内允许补卡
     */
    @DiffLogField(name = "几日内允许补卡")
    private Integer days;

}