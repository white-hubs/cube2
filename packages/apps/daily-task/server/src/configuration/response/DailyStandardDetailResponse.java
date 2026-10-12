package com.guhecloud.cube.biz.daily.task.configuration.response;

import com.guhecloud.cube.biz.daily.task.commons.dto.DailyResultLabelDTO;
import com.guhecloud.cube.biz.daily.task.configuration.param.TargetTypeRelationParam;
import lombok.Data;

import java.util.List;

@Data
public class DailyStandardDetailResponse {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 标准名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 任务种类ID（多个ID用逗号分隔，如：1,2,3）
     */
    private String categoryIds;

    /**
     * 任务种类名称（多个名称用逗号分隔，如：类型1,类型2,类型3）
     */
    private String categoryNames;

    /**
     * 是否启用
     */
    private Boolean enable;

    /**
     * 任务类型
     */
    private String dailyType;

    /**
     * 关联对象列表
     */
    private List<TargetTypeRelationParam> targetTypeRelations;

    /**
     * 标准项信息列表
     */
    private List<ItemInfo> items;

    /**
     * 标准项信息
     */
    @Data
    public static class ItemInfo {
        /**
         * 标准项ID
         */
        private Long itemId;

        /**
         * 标准项名称
         */
        private String itemName;

        /**
         * 标准项内容
         */
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
         * 执行结果ID（标准项与标准结果的关联）
         */
        private Long resultId;

        /**
         * 执行结果名称
         */
        private String resultName;

        /**
         * 结果项列表
         */
        private List<DailyResultLabelDTO> resultLabelList;

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
}
