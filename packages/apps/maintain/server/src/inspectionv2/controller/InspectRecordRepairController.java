package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.controller;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectRecordRepairCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectRecordQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRepairBatchCommand;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRepairBatchTransferParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRepairQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectTargetRepairVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 巡检修复（巡检记录修复
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectRecordRepairController {
    private final InspectRecordQueryService inspectRecordQueryService;
    private final InspectRecordRepairCommandService inspectRecordRepairCommandService;

    /**
     * 巡检修复分页列表
     *
     * @param param 条件
     *
     * @return 分页
     */
    @GetMapping("/inspect/repair/page")
    @DataPermissionSwitch(dataModel = "inspect:inspectRepair", tables = "inspect_repair_relation")
    public Pagination<InspectTargetRepairVO> getRecordRepairPage(InspectRepairQueryParam param) {
        return inspectRecordQueryService.getRecordRepairPage(param);
    }

    /**
     * 巡检修复详情
     *
     * @param param 条件
     *
     * @return 详情
     */
    @GetMapping("/inspect/repair/detail")
    public InspectTargetRepairVO getRecordRepairDetail(InspectRepairQueryParam param) {
        return inspectRecordQueryService.getRecordRepairDetail(param);
    }

    /**
     * 批量提交巡检记录修复
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    @PostMapping("/inspect/repair/batch-submit")
    public Boolean batchSubmitHandle(@RequestBody InspectRepairBatchCommand param) {
        return inspectRecordRepairCommandService.batchSubmitHandle(param);
    }

    /**
     * 批量取消修复
     *
     * @param param 记录id集合
     *
     * @return 是否成功
     */
    @PostMapping("/inspect/repair/batch-clean")
    public Boolean batchCleanRepair(@RequestBody InspectRepairBatchCommand param) {
        return inspectRecordRepairCommandService.batchCleanRepair(param);
    }

    /**
     * 批量取消修复通过任务ids
     *
     * @param param 记录id集合
     *
     * @return 是否成功
     */
    @PostMapping("/inspect/repair/clean/task-ids")
    public Boolean batchCleanRepairByTaskIds(@RequestBody InspectRepairBatchCommand param) {
        return inspectRecordRepairCommandService.batchCleanRepairByTaskIds(param);
    }

    /**
     * 修复转交
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    @PostMapping("/inspect/repair/transfer")
    public Boolean transferRepair(@RequestBody @Valid InspectRepairBatchTransferParam param) {
        return inspectRecordRepairCommandService.transferRepair(param);
    }
}
