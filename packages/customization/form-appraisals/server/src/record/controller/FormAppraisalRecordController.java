package com.guhecloud.cube.srv.gen.appraisals.server.record.controller;

import com.guhecloud.cube.srv.gen.appraisals.server.record.param.FormAppraisalRecordDetailAdjustCommand;
import com.guhecloud.cube.srv.gen.appraisals.server.record.param.FormAppraisalRecordPageParam;
import com.guhecloud.cube.srv.gen.appraisals.server.record.param.FormAppraisalRecordScoreCommand;
import com.guhecloud.cube.srv.gen.appraisals.server.record.param.FormAppraisalRecordStatisticsParam;
import com.guhecloud.cube.srv.gen.appraisals.server.record.service.FormAppraisalPreviewRecordGenerateService;
import com.guhecloud.cube.srv.gen.appraisals.server.record.service.FormAppraisalRecordQueryService;
import com.guhecloud.cube.srv.gen.appraisals.server.record.service.FormAppraisalRecordService;
import com.guhecloud.cube.srv.gen.appraisals.server.record.vo.FormAppraisalRecordDetailVO;
import com.guhecloud.cube.srv.gen.appraisals.server.record.vo.FormAppraisalRecordGroupVO;
import com.guhecloud.cube.srv.gen.appraisals.server.record.vo.FormAppraisalStatisticsVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 考核记录控制器
 * 提供考核记录的查询、评分调整、明细调整、确认等操作接口
 */
@RestController
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormAppraisalRecordController {
    private final FormAppraisalRecordService recordService;
    private final FormAppraisalRecordQueryService recordQueryService;
    private final FormAppraisalPreviewRecordGenerateService previewRecordGenerateService;

    /**
     * 根据 ID 获取考核记录详情
     *
     * @param id 记录 ID
     *
     * @return 考核记录详情 VO
     */
    @GetMapping("/form-appraisal-record/{id}")
    public FormAppraisalRecordDetailVO getById(@PathVariable Long id) {
        return recordQueryService.getById(id);
    }

    /**
     * 分页获取考核记录列表
     *
     * @param param 分页查询参数
     *
     * @return 考核记录分页数据
     */
    @GetMapping("/form-appraisal-record/page")
    public Pagination<FormAppraisalRecordGroupVO> getPage(FormAppraisalRecordPageParam param) {
        return recordQueryService.getPage(param);
    }

    /**
     * 获取考核统计看板
     *
     * @param param 统计参数
     *
     * @return 统计结果
     */
    @GetMapping("/form-appraisal-record/statistics")
    public FormAppraisalStatisticsVO statistics(@Valid FormAppraisalRecordStatisticsParam param) {
        return recordQueryService.statistics(param);
    }

    /**
     * 生成或刷新当前周期的考核预览数据
     */
    @PostMapping("/form-appraisal-record/preview/generate")
    public Boolean generatePreview() {
        previewRecordGenerateService.generateCurrentPreviews();
        return true;
    }

    /**
     * 考核记录导出
     *
     * @param param    查询条件
     * @param request  请求
     * @param response 响应
     */
    @GetMapping("/form-appraisal-record/export")
    public void export(FormAppraisalRecordPageParam param, HttpServletRequest request, HttpServletResponse response) {
        recordQueryService.export(param, request, response);
    }

    /**
     * 调整考核记录分数
     * 用于手动修改考核记录的最终得分
     *
     * @param id      记录 ID
     * @param command 分数调整命令
     */
    @PutMapping("/form-appraisal-record/{id}/score")
    public void adjustScore(@PathVariable Long id, @RequestBody @Valid FormAppraisalRecordScoreCommand command) {
        recordService.adjustScore(id, command);
    }

    /**
     * 调整考核记录明细
     * 用于修改考核记录的明细项及最终得分
     *
     * @param id      记录 ID
     * @param command 明细调整命令
     */
    @PutMapping("/form-appraisal-record/{id}/details")
    public void adjustDetails(@PathVariable Long id, @RequestBody @Valid FormAppraisalRecordDetailAdjustCommand command) {
        command.setRecordId(id);
        recordService.adjustDetails(id, command);
    }

    /**
     * 确认考核记录
     * 将考核记录状态变更为已确认
     *
     * @param id 记录 ID
     */
    @PostMapping("/form-appraisal-record/{id}/confirm")
    public void confirm(@PathVariable Long id) {
        recordService.confirm(id);
    }
}
