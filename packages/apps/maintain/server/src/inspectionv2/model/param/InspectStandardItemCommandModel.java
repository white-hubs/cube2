package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class InspectStandardItemCommandModel {
    private Long id;

    /**
     * 巡检项名称
     */
    @Length(max = 60)
    private String name;

    /**
     * 巡检结果Id
     */
    private Long resultId;

    /**
     * 巡检结果默认值
     */
    private String resultItemValue;

    /**
     * 要填写备注的结果(NORMAL正常需要填写，ANORMAL异常需要填写,多个用逗号隔开)
     */
    private String remarkResult;

    /**
     * 要拍照的结果(NORMAL正常需要拍照，ANORMAL异常需要拍照,多个用逗号隔开)
     */
    private String imageResult;

    private String alias;

    private String referType;

    /**
     * 巡检项tag
     */
    private String itemTag;

    /**
     * 是否为结果巡检项
     */
    private Boolean isResultItem;
}
