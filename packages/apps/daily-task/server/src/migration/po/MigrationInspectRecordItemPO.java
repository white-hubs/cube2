package com.guhecloud.cube.biz.daily.task.migration.po;

import com.guhecloud.platform.database.entity.BasicTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 巡检记录-结果项
 * </p>
 *
 * @author zwb
 * @since 2022-04-26
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
//@TableName("inspect_record_item")
public class MigrationInspectRecordItemPO extends BasicTenantEntity<Long> {

    private Long id;

    /**
     * 巡检记录ID
     */
    private Long recordId;

    /**
     * 是否临时添加标准
     */
    private Boolean tempAddFlag;

    /**
     * 巡检标准ID
     */
    private Long standardId;

    /**
     * 巡检标准名称
     */
    private String standardName;

    /**
     * 巡检项ID
     */
    private Long itemId;

    /**
     * 巡检项名称
     */
    private String itemName;

    /**
     * 是否强制拍照 0 否 1是
     */
    private String itemImageResult;

    /**
     * 要填写备注的结果(NORMAL正常需要填写，ANORMAL异常需要填写,多个用逗号隔开)
     */
    private String remarkResult;

    /**
     * 巡检结果Id
     */
    private Long itemResultId;

    /**
     * 巡检项结果值
     */
    private String itemResultValue;

    /**
     * 巡检项结果名称
     */
    private String itemResultName;

    /**
     * 0 不是  1 是
     */
    private Boolean errorValueFlag;

    /**
     * 异常说明
     */
    private String errorDesc;

    /**
     * 巡检项tag
     */
    private String itemTag;
    /**
     * 业务类型
     */
    private String businessType;
}
