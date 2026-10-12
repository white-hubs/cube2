package com.guhecloud.cube.biz.daily.task.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 执行标准完整对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyStandardResultDTO {
    private Long id;
    private String name;
    private String description;
    private List<DailyItemResultDTO> itemList;

    private LocalDateTime createTime;
}
