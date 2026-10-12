package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRepairBatchCommand;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRepairBatchTransferParam;
import org.springframework.web.bind.annotation.RequestBody;

public interface InspectRecordRepairCommandService {
    /**
     * 批量提交巡检记录修复
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    Boolean batchSubmitHandle(@RequestBody InspectRepairBatchCommand param);

    Boolean batchCleanRepair(@RequestBody InspectRepairBatchCommand param);

    Boolean batchCleanRepairByTaskIds(@RequestBody InspectRepairBatchCommand param);

    Boolean transferRepair(@RequestBody InspectRepairBatchTransferParam param);
}
