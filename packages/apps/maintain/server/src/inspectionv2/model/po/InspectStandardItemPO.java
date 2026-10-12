package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 巡检标准中的巡检项
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
@TableName("inspect_standard_item")
public class InspectStandardItemPO {

    private Long id;

    private String alias;

    private String referType;

    /**
     * 巡检标准ID
     */
    private Long standardId;

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

    /**
     * 要填写备注的结果(NORMAL正常需要填写，ANORMAL异常需要填写,多个用逗号隔开)
     */
    private String remarkResult;

    /**
     * 要拍照的结果(NORMAL正常需要拍照，ANORMAL异常需要拍照,多个用逗号隔开)
     */
    private String imageResult;

    /**
     * 巡检项tag
     */
    private String itemTag;

    /**
     * 是否为结果巡检项
     */
    private Boolean isResultItem;
}
