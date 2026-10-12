package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces;

import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttModuleConfigCheck;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.commandgateway.AttSchedulingCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.querygateway.AttSchedulingQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param.ArrangeWorkParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param.AttHandSchedulingParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param.AttSchedulingPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param.AttWorkStaffPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param.AttWorkingStaffParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.result.AttSchedulingPageResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.result.AttWorkingResult;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 排班入口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttSchedulingController {

    private final AttSchedulingCommandService attSchedulingCommandService;
    private final AttSchedulingQueryService attSchedulingQueryService;
    private final AttModuleConfigCheck attModuleConfigCheck;

    /**
     * 手动排班
     *
     * @param param 排班入参
     */
    @PostMapping("/att/scheduling/hand/action")
    public void handScheduling(@RequestBody @Valid AttHandSchedulingParam param) {
        this.checkArrangeModule();
        attSchedulingCommandService.handScheduling(param);

    }

    /**
     * 排班分页列表
     */
    @GetMapping("/att/scheduling/hand/page")
    public Pagination<AttSchedulingPageResult> pageAttScheduling(@Valid AttSchedulingPageParam param) {
        this.checkArrangeModule();
        long start = System.currentTimeMillis();
        Pagination<AttSchedulingPageResult> resultPagination = attSchedulingQueryService.pageAttScheduling(param);
        log.info("排班分页列表接口耗时(ms)：" + (System.currentTimeMillis() - start));
        return resultPagination;
    }

    /**
     * 导出排班
     *
     * @param param    条件
     * @param request  请求
     * @param response 返回
     */
    @PostMapping("/att/scheduling/export")
    public void exportAttScheduling(@RequestBody AttSchedulingPageParam param, HttpServletRequest request, HttpServletResponse response) {
        this.checkArrangeModule();
        attSchedulingQueryService.exportAttScheduling(param, request, response);
    }

    /**
     * 下载排班模板
     *
     * @param param    条件
     * @param request  请求对象
     * @param response 返回对象
     */
    @PostMapping("/att/scheduling/template/download")
    public void downloadTemplate(@RequestBody AttSchedulingPageParam param, HttpServletRequest request, HttpServletResponse response) {
        this.checkArrangeModule();
        attSchedulingQueryService.downloadTemplate(param, request, response);
    }

    /**
     * 排班模板导入
     * 导入excel排班
     *
     * @param yearMonth 月份 格式：yyyy-MM
     * @param file      文件
     */
    @PostMapping("/att/scheduling/template/import")
    public void importTemplateScheduling(@RequestParam("yearMonth") String yearMonth, @RequestParam("file") MultipartFile file) {
        this.checkArrangeModule();
        attSchedulingCommandService.importTemplateScheduling(yearMonth, file);
    }

    /**
     * 获取人员工作状态统计
     *
     * @param param 条件
     *
     * @return 工作人员
     */
    @GetMapping("/att/scheduling/workingStaff")
    public AttWorkingResult workingStaff(AttWorkingStaffParam param) {

        return attSchedulingQueryService.workingStaff(param);
    }

    /**
     * 获取在岗/或不在岗人员分页列表
     *
     * @param param 条件
     *
     * @return 分页列表
     */
    @GetMapping("/att/scheduling/workStaffPage")
    public Pagination<UserDTO> getWorkStatusStaffPage(AttWorkStaffPageParam param) {
        return attSchedulingQueryService.getWorkStatusStaffPage(param);
    }

    /**
     * 校验是否有排班模块
     */
    private void checkArrangeModule() {
        if (!attModuleConfigCheck.checkModuleHave(AttConstants.arrangeCode)) {
            throw new ServiceException("无此模块");
        }
    }

    @PostMapping("/att/getOnDutyPersonnel")
    public List<Long> getOnDutyPersonnel(@RequestBody ArrangeWorkParam param) {
        return attSchedulingQueryService.getStaffStartTimeScheduling(param.getUserIds(), param.getStartDate());
    }

    /**
     * 筛选当班人员，参数传人员ID数组，筛选出当前时间有班次的人员
     *
     * @param param 需要筛选的人员ID数组 和时间
     *
     * @return 在岗人员ID列表
     */
    @PostMapping("/att/postOnDutyPersonnel")
    List<Long> postOnDutyPersonnel(@RequestBody ArrangeWorkParam param) {
        return attSchedulingQueryService.getStaffStartTimeScheduling(param.getUserIds(), param.getStartDate());
    }

}
