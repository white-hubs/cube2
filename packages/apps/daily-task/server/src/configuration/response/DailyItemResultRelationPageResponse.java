package com.guhecloud.cube.biz.daily.task.configuration.response;

import com.guhecloud.cube.biz.daily.task.commons.dto.DailyResultLabelDTO;
import com.guhecloud.cube.biz.daily.task.enums.TaskTargetTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 执行项的结果关联响应
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyItemResultRelationPageResponse {

    /**
     * 执行项ID
     */
    private Long itemId;

    /**
     * 执行项名称
     */
    private String itemName;

    /**
     * 执行项内容
     */
    private String itemContent;

    /**
     * 操作指引
     */
    private String operationGuide;

    /**
     * 适配对象类型列表（支持多个类型）
     */
    private List<TaskTargetTypeEnum> targetTypes;

    /**
     * 执行结果ID
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

    /**
     * 结果项列表字符串
     */
    private String resultLabelStr;
    /**
     * 适用对象类型列表字符串
     */
    private String targetTypeStr;

    /**
     * 任务类型
     */
    private String dailyType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

