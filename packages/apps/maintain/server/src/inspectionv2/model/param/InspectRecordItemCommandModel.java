package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
public class InspectRecordItemCommandModel {

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
     * 要拍照的结果(NORMAL正常需要拍照，ANORMAL异常需要拍照,多个用逗号隔开)
     */
    private String itemImageResult;

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
     * 要填写备注的结果(NORMAL正常需要填写，ANORMAL异常需要填写,多个用逗号隔开)
     */
    private String remarkResult;

    /**
     * 异常说明
     */
    @Length(max = 100)
    private String errorDesc;

    /**
     * 文件主键
     */
    private List<Long> fileIds;

    /**
     * 巡检项tag
     */
    private String itemTag;

}
