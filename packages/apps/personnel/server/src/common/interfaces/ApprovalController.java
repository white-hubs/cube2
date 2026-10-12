package com.guhecloud.cube.srv.gen.personnel.management.server.common.interfaces;

import com.guhecloud.cube.srv.gen.personnel.management.server.common.interfaces.vo.ApprovalDataVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.querygateway.FlowFormDataQueryService;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.querygateway.query.FlowFormDataQueryParam;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 审批统计相关公共接口
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApprovalController {

    private final FlowFormDataQueryService flowFormDataQueryService;

    @GetMapping("/statistics/recruit")
    public Pagination<ApprovalDataVO> getStatisticsRecruitPage(FlowFormDataQueryParam param) {
        return flowFormDataQueryService.getStatisticsRecruitPage(param);
    }

    @GetMapping("/statistics/recruit/detail/{id}")
    public Pagination<ApprovalDataVO> getStatisticsRecruitDetail(@PathVariable("id") String id, FlowFormDataQueryParam param) {
        return flowFormDataQueryService.getStatisticsRecruitDetail(id, param);
    }

    @PostMapping("/statistics/recruit/export")
    public void getStatisticsRecruitExport(@RequestBody FlowFormDataQueryParam param, HttpServletRequest request,
                                           HttpServletResponse response
                                          ) {
        flowFormDataQueryService.getStatisticsRecruitExport(param, request, response);
    }

    @GetMapping("/statistics/induction")
    public Pagination<ApprovalDataVO> getStatisticsInductionPage(FlowFormDataQueryParam param) {
        return flowFormDataQueryService.getStatisticsInductionPage(param);
    }

    @GetMapping("/statistics/induction/detail/{id}")
    public Pagination<ApprovalDataVO> getStatisticsInductionDetail(@PathVariable("id") String id, FlowFormDataQueryParam param) {
        return flowFormDataQueryService.getStatisticsInductionDetail(id, param);
    }

    @PostMapping("/statistics/induction/export")
    public void getStatisticsInductionExport(@RequestBody FlowFormDataQueryParam param, HttpServletRequest request,
                                             HttpServletResponse response
                                            ) {
        flowFormDataQueryService.getStatisticsInductionExport(param, request, response);
    }

    @GetMapping("/statistics/probation-assess")
    public Pagination<ApprovalDataVO> getStatisticsAssessPage(FlowFormDataQueryParam param) {
        return flowFormDataQueryService.getStatisticsAssessPage(param);
    }

    @GetMapping("/statistics/probation-assess/detail/{id}")
    public Pagination<ApprovalDataVO> getStatisticsAssessDetail(@PathVariable("id") String id, FlowFormDataQueryParam param) {
        return flowFormDataQueryService.getStatisticsAssessDetail(id, param);
    }

    @PostMapping("/statistics/probation-assess/export")
    public void getStatisticsAssessExport(@RequestBody FlowFormDataQueryParam param, HttpServletRequest request,
                                          HttpServletResponse response
                                         ) {
        flowFormDataQueryService.getStatisticsAssessExport(param, request, response);
    }

    @GetMapping("/statistics/regular-worker")
    public Pagination<ApprovalDataVO> getStatisticsRegularWorkerPage(FlowFormDataQueryParam param) {
        return flowFormDataQueryService.getStatisticsRegularWorkerPage(param);
    }

    @GetMapping("/statistics/regular-worker/detail/{id}")
    public Pagination<ApprovalDataVO> getStatisticsRegularWorkerDetail(@PathVariable("id") String id, FlowFormDataQueryParam param) {
        return flowFormDataQueryService.getStatisticsRegularWorkerDetail(id, param);
    }

    @PostMapping("/statistics/regular-worker/export")
    public void getStatisticsRegularWorkerExport(@RequestBody FlowFormDataQueryParam param, HttpServletRequest request,
                                                 HttpServletResponse response
                                                ) {
        flowFormDataQueryService.getStatisticsRegularWorkerExport(param, request, response);
    }

    @GetMapping("/statistics/leave-office")
    public Pagination<ApprovalDataVO> getStatisticsLeaveOfficePage(FlowFormDataQueryParam param) {
        return flowFormDataQueryService.getStatisticsLeaveOfficePage(param);
    }

    @GetMapping("/statistics/leave-office/detail/{id}")
    public Pagination<ApprovalDataVO> getStatisticsLeaveOfficeDetail(@PathVariable("id") String id, FlowFormDataQueryParam param) {
        return flowFormDataQueryService.getStatisticsLeaveOfficeDetail(id, param);
    }

    @PostMapping("/statistics/leave-office/export")
    public void getStatisticsLeaveOfficeExport(@RequestBody FlowFormDataQueryParam param, HttpServletRequest request,
                                               HttpServletResponse response
                                              ) {
        flowFormDataQueryService.getStatisticsLeaveOfficeExport(param, request, response);
    }

    @GetMapping("/statistics/rotation-post")
    public Pagination<ApprovalDataVO> getStatisticsRotationPostPage(FlowFormDataQueryParam param) {
        return flowFormDataQueryService.getStatisticsRotationPostPage(param);
    }

    @GetMapping("/statistics/rotation-post/detail/{id}")
    public Pagination<ApprovalDataVO> getStatisticsRotationPostDetail(@PathVariable("id") String id, FlowFormDataQueryParam param) {
        return flowFormDataQueryService.getStatisticsRotationPostDetail(id, param);
    }

    @PostMapping("/statistics/rotation-post/export")
    public void getStatisticsRotationPostExport(@RequestBody FlowFormDataQueryParam param, HttpServletRequest request,
                                                HttpServletResponse response
                                               ) {
        flowFormDataQueryService.getStatisticsRotationPostExport(param, request, response);
    }

}
