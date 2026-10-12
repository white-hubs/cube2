package com.guhecloud.cube.deliver.project.merchant.dto.store;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.ConvertionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallSafetyMarkLogs;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author benhuabao
 * @date 2021/12/28
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StallSafetyMarkLogsDTO extends ConvertionDTO<StallSafetyMarkLogsDTO, StallSafetyMarkLogs> {

    public StallSafetyMarkLogsDTO() {
    }

    public StallSafetyMarkLogsDTO(StallSafetyMarkLogs logs) {
        super();
        map(logs);
    }

    public StallSafetyMarkLogsDTO(List<StallSafetyMarkLogs> list) {
        super();
        map(list);
    }

    /**
     * 主键
     */
    private String id;

    /**
     * 档位ID
     */
    private String stallId;

    /**
     * 调整分数（加|减）
     */
    private Integer point;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 删除标志（0存在，1已删除）
     */
    private Boolean deleted;
}
