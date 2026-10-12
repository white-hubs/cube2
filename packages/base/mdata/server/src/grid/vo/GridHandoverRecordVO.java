package com.guhecloud.cube.biz.mdata.server.grid.vo;

import com.guhecloud.cube.biz.mdata.server.grid.enumeration.GridHandoverStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 网格交接记录列表 VO
 */
@Data
public class GridHandoverRecordVO {
    private Long id;
    private String recordName;
    private String gridAreaId;
    private List<Long> gridAreaIds;
    private String gridAreaName;
    private Long shiftOverUserId;
    private String shiftOverUserName;
    private LocalDateTime shiftOverTime;
    private Long takeOverUserId;
    private String takeOverUserName;
    private LocalDateTime takeOverTime;
    private GridHandoverStatusEnum status;
    private String remarks;
    private Long shiftSignFileId;
    private Long takeSignFileId;
    /**
     * 撤销权限
     */
    private Boolean revoke;
}
