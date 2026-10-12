package com.guhecloud.cube.biz.daily.task.configuration.response;

import com.guhecloud.cube.biz.daily.task.commons.dto.DailyResultLabelDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 执行结果项响应
 */
@Data
public class DailyResultResponse {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 是否启用
     */
    private Boolean enable;
    /**
     * 结果项 List<DailyResultLabelDTO>
     */
    private List<DailyResultLabelDTO> resultLabelList;

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

