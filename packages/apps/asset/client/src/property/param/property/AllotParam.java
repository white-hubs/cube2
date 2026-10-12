package com.guhecloud.cube.property.management.client.property.param.property;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AllotParam {
    /**
     * 操作类型（调拨：ALLOT_UNIT；派发：ALLOT_PERSONAL）
     */
    @NotEmpty(message = "操作类型不能为空")
    private String type;
    /**
     * 移交部门
     */
    private List<String> overDepId;
    /**
     * 移交部门名称
     */
    private String overDepName;
    /**
     * 接收部门id
     */
    @NotEmpty(message = "部门id不能为空")
    private List<String> receiveDepId;
    /**
     * 接收部门名称
     */
    @NotEmpty(message = "部门不能为空")
    private String receiveDepName;
    /**
     * 接收人
     */
    private List<String> receiveUserId;
    /**
     * 接收日期
     */
    @NotNull(message = "日期不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime receiveTime;
    /**
     * 区域id
     */
    private List<String> areaIds;
    private String areaId;
    /**
     * 出库后的存放地点
     */
    private String place;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 附件
     */
    private String files;
    /**
     * 资产详情
     */
    @Size(min = 1, message = "资产信息不能为空")
    private List<Long> propertyIds;
    private String status;
}
