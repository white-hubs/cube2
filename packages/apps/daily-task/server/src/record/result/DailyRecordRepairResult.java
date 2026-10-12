package com.guhecloud.cube.biz.daily.task.record.result;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DailyRecordRepairResult {
    private Long id;
    private String executionResult;
    private Long handlerId;
    private String handlerName;
    private String remark;
    private LocalDateTime createTime;
    private List<FileVO> fileList;
}
