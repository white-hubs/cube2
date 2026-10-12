package com.guhecloud.cube.biz.daily.task.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 执行项完整对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyItemResultDTO {
    private Long id;
    private String name;
    private String content;
    private String operationGuide;

    /**
     * 标签
     */
    private String tag;
    private List<DailyResultDTO> resultList;

    private LocalDateTime createTime;

    /**
     * 执行结果
     */
    private Object executeResult;
}
