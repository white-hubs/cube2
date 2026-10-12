package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 巡检记录项
 */
@Data
public class InspectRecordItemListVO {
    private Long id;
    private Long recordId;
    private Long standardId;
    private String standardName;
    private Long itemId;
    private String itemName;
    private Long itemResultId;
    private String itemResultValue;
    private String itemResultName;
    private Boolean errorValueFlag;
    private String itemImageResult;
    private String remarkResult;
    private String errorDesc;
    private LocalDateTime createTime;
    /**
     * 巡检项tag
     */
    private String itemTag;
}
