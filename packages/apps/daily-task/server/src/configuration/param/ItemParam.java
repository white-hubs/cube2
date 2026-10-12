package com.guhecloud.cube.biz.daily.task.configuration.param;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 新增标准项参数
 */
@Data
public class ItemParam {

    /**
     * 主键ID,没有id默认为新增
     */
    private Long itemId;
    /**
     * 标准项名称
     */
    @NotBlank(message = "标准项名称不能为空")
    private String itemName;

    /**
     * 标准项内容
     */
    @NotBlank(message = "标准项内容不能为空")
    private String itemContent;

    /**
     * 操作指引
     */
    private String operationGuide;

    /**
     * 标签
     */
    private String tag;

    /**
     * 适配对象类型列表（支持多个类型）
     */
    private List<String> targetTypes;

    /**
     * 执行结果ID（标准项与标准结果的关联）
     */
    private Long resultId;

    /**
     * 默认结果项
     */
    private String defaultResultItem;

    /**
     * 必须拍照的结果项
     */
    private String mustTakePhotoResultItems;

    /**
     * 必须备注的结果项
     */
    private String mustRemarkResultItems;
}