package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class InspectRepairBatchTransferParam {
    @NotEmpty(message = "待修复记录不能为空")
    public List<Long> ids;
    @NotNull(message = "被分配人不能为空")
    public Long userId;
}
