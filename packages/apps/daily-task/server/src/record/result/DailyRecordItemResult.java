package com.guhecloud.cube.biz.daily.task.record.result;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyRecordItemResult {
    private Long taskId;
    private String taskName;
    private Long record;
    private Long standardId;

    private Long itemId;
    private String itemName;
    private Long resultId;
    private String resultName;
    /**
     * 是否异常结果
     */
    private Boolean errorFlag;
    private Long fileRefKey;
    private List<FileVO> fileList;
    private String remark;
}
