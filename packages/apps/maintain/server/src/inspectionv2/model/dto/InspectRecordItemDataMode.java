package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class InspectRecordItemDataMode {
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
    private Integer tenant;
    private Boolean deleted;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;

    private String businessType;
}
