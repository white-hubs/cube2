package com.guhecloud.cube.biz.daily.task.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyResultDTO {
    private Long id;
    private String name;

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
     * 结果项 List<DailyResultLabelDTO>
     */
    private String resultLabelJson;

    private LocalDateTime createTime;
}
