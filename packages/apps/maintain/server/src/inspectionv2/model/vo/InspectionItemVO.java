package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wql
 * @describe 巡检项
 * @date 2023/4/21 11:53
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectionItemVO {
    /**
     * 是否临时添加巡检项
     */
    private Integer isRawItem;
    /**
     * 巡检项id
     */
    private Long id;
    /**
     * 巡检项名称
     */
    private String name;
    /**
     * 巡检项内容
     */
    private String content;
    /**
     * 巡检项默认值
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
    /**
     * 巡检结果id
     */
    private Long resultId;
    /**
     * 巡检标准id
     */
    private Long standardId;
    /**
     * 别名
     */
    private String alias;
    /**
     * 适用类型
     */
    private String referType;
    /**
     * 巡检结果
     */
    private InspectResultVO resultVO;

    /**
     * 巡检项tag
     */
    private String itemTag;

    /**
     * 是否为结果巡检项
     */
    private Boolean isResultItem;
}
