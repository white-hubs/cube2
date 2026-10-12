package com.guhecloud.cube.biz.daily.task.commons.dto;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 关联的标准返回对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyRecordStandardResultDTO {
    /**
     * 关联的主id
     */
    private Long masterId;
    private Long standardId;
    private String standardName;
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
