package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

@Data
public class InspectStandardItemVO {

    private String id;

    /**
     * 巡检标准ID
     */
    private Long standardId;

    private String alias;

    private String referType;
    /**
     * 巡检项编码
     */
    private String code;

    /**
     * 巡检项名称
     */
    private String name;

    /**
     * 巡检结果Id
     */
    private Long resultId;

    /**
     * 巡检结果默认值
     */
    private String resultItemValue;
    //默认值描述
    private String resultItemName;

    /**
     * 要填写备注的结果(NORMAL正常需要填写，ANORMAL异常需要填写,多个用逗号隔开)
     */
    private String remarkResult;
    private String remarkResultNames;

    /**
     * 要拍照的结果(NORMAL正常需要拍照，ANORMAL异常需要拍照,多个用逗号隔开)
     */
    private String imageResult;
    private String imageResultNames;

    private String resultNames;

    private String referTypeNames;

    private InspectResultSimpleVO resultVO;

    /**
     * 巡检项tag
     */
    private String itemTag;
    /**
     * 是否为结果巡检项
     */
    private Boolean isResultItem;
}
