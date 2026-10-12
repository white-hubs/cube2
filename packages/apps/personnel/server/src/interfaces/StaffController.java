package com.guhecloud.cube.srv.gen.personnel.management.server.interfaces;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.StaffCommandService;
import com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.command.ChangeWorkingStatusCommand;
import com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.command.SubmitStaffCommand;
import com.guhecloud.cube.srv.gen.personnel.management.server.dao.StaffMapper;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.params.BaseDateStatisticsParam;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.params.StaffNotInParam;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo.HomePageStatisticsVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo.KeyValueVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo.StaffChangeStatVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo.StaffChangeVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo.StaffDetailAttendVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo.StaffDetailVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo.StaffExchangeStatisticsVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo.StaffStateVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo.StaffStructureStatisticsVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.po.StaffPO;
import com.guhecloud.cube.srv.gen.personnel.management.server.querygateway.StaffQueryService;
import com.guhecloud.cube.srv.gen.personnel.management.server.querygateway.query.StaffQueryParam;
import com.guhecloud.cube.srv.gen.personnel.management.server.tool.ExcelBeanUtil;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StaffController {

    private final StaffCommandService staffCommandService;
    private final StaffQueryService staffQueryService;
    private final StaffMapper staffMapper;

    @PostMapping("/staff/getStaffPageNotIn")
    Pagination<StaffPO> getStaffPageNotIn(@RequestBody StaffNotInParam param) {
        return staffQueryService.getStaffPageNotIn(param);
    }

    /**
     * 根据员工id集合查询员工信息
     * 考勤使用字段不全
     *
     * @param ids id集合
     *
     * @return 员工列表
     */
    @PostMapping("/staff/getStaffByIds")
    List<StaffPO> getStaffByIds(@RequestBody List<Long> ids) {
        return staffQueryService.getStaffByIds(ids);
    }

    @GetMapping("/staff/getStaffVOById")
    public StaffDetailVO getStaffVOById(@RequestParam("staffId") Long staffId) {
        if (staffId == null) {
            return null;
        }
        return staffQueryService.getStaffVOById(staffId);
    }

    @GetMapping("/staff/{id}")
    public StaffDetailVO getById(@PathVariable("id") Long id) {
        return staffQueryService.get(id);
    }

    @GetMapping("/staff/user/{userId}")
    public StaffDetailVO getByUserId(@PathVariable("userId") Long userId) {
        StaffPO staffPO = staffQueryService.getOne(
                Wrappers.<StaffPO>lambdaQuery()
                        .eq(StaffPO::getUserId, userId)
                                                  );
        if (Objects.isNull(staffPO)) {
            return null;
        }
        return staffQueryService.get(staffPO.getId());
    }

    @GetMapping("/staff/mobile")
    public StaffDetailVO getByMobile(@RequestParam("mobile") String mobile) {
        return staffQueryService.getByMobile(mobile);
    }

    @GetMapping("/staff/change")
    public Pagination<StaffChangeVO> getStaffChange(StaffQueryParam staffQueryParam) {
        return staffQueryService.getStaffChange(staffQueryParam);
    }

    @GetMapping("/staff/change/stat")
    public Pagination<StaffChangeStatVO> getStaffChangeStat(StaffQueryParam staffQueryParam) {
        return staffQueryService.getStaffChangeStat(staffQueryParam);
    }

    @PostMapping("/staff/change/stat/export")
    public void getStaffChangeStatExport(@RequestBody StaffQueryParam staffQueryParam, HttpServletRequest request,
                                         HttpServletResponse response
                                        ) {
        staffQueryService.getStaffChangeStatExport(staffQueryParam, request, response);
    }

    @GetMapping("/staff/list")
    public List<StaffDetailVO> getList(StaffQueryParam staffQueryParam) {
        return staffQueryService.getList(staffQueryParam);
    }

    @GetMapping("/staff/list/attend")
    public List<StaffDetailAttendVO> getListByAttend(StaffQueryParam staffQueryParam) {
        return staffQueryService.getListByAttend(staffQueryParam);
    }

    @GetMapping("/staff/page")
    public Pagination<StaffDetailVO> getPage(StaffQueryParam staffQueryParam) {
        return PagingUtils.convert(staffQueryService.getPage(staffQueryParam));
    }

    @GetMapping("/staff/state/page")
    public Pagination<StaffStateVO> getStatePage(StaffQueryParam staffQueryParam) {
        return staffQueryService.getStatePage(staffQueryParam);
    }

    @GetMapping("/staff/list/abc")
    public List<StaffStateVO> getListByABC(StaffQueryParam staffQueryParam) {
        return staffQueryService.getListByABC(staffQueryParam);
    }

    /**
     * 查询在岗、离岗人数
     *
     * @return 在岗、离岗人数
     */
    @GetMapping("/staff/getStateTotal")
    public List<KeyValueVO> getStateTotal() {
        return staffQueryService.getStateTotal();
    }

    @GetMapping("/staff/num")
    public Long getNum() {
        return staffQueryService.getNum();
    }

    @GetMapping("/staff/enum/list")
    public List<Map<String, String>> getEnumList(@RequestParam("type") String type) {
        return staffQueryService.getEnumList(type);
    }

    @GetMapping("/staff/info")
    public Map<String, Object> getInfoByStaffId(@RequestParam("id") String id, @RequestParam("attr") String attr) {
        return staffQueryService.getInfoByStaffId(id, attr);
    }

    @GetMapping("/staff/filter/working-year/list")
    public Set<Long> getListWithWorkingYear(@RequestParam("workingYear") Integer workingYear) {
        return staffMapper.selectListWithWorkingYear(workingYear);
    }

    @GetMapping("/staff/filter/time-of-induction/list")
    public Set<Long> getListTimeOfInduction(@RequestParam("timeOfInduction") Integer timeOfInduction) {
        return staffMapper.selectListTimeOfInduction(timeOfInduction);
    }

    @GetMapping("/staff/stru/statistics")
    public StaffStructureStatisticsVO getStrucStatistics() {
        return staffQueryService.getStrucStatistics();
    }

    @GetMapping("/staff/exchange/statistics")
    public StaffExchangeStatisticsVO getExchangeStatistics(StaffQueryParam param) {
        return staffQueryService.getExchangeStatistics(param);
    }

    @PostMapping("/staff")
    public Long saveOrUpdate(@Valid @RequestBody SubmitStaffCommand submitStaffCommand) {
        return staffCommandService.saveOrUpdate(submitStaffCommand);
    }

    @PostMapping("/staff/export")
    public void exportData(StaffQueryParam param, HttpServletRequest request, HttpServletResponse response) {
        try {
            staffQueryService.exportData(param, request, response);
        } catch (Exception e) {
            log.error("员工花名册导出失败", e);
            throw new ServiceException("花名册导出失败，请联系管理员");
        }
    }

    @PostMapping("/staff/template/export")
    public void exportTemplate(HttpServletRequest request, HttpServletResponse response) {
        try {
            staffQueryService.exportTemplate(request, response);
        } catch (Exception e) {
            log.error("员工花名册导出失败", e);
            throw new ServiceException("花名册导出失败，请联系管理员");
        }
    }

    @PostMapping("/staff/template/import")
    public List<ExcelBeanUtil.ExcelMsg> importTemplate(@RequestParam("file") MultipartFile file) throws Exception {
        return staffCommandService.importTemplate(file);
    }

    @PutMapping("/staff/status/working/change")
    public void changeWorkingStatus(@RequestBody List<ChangeWorkingStatusCommand> changeWorkingStatusCommands) {
        if (CollectionUtils.isNotEmpty(changeWorkingStatusCommands)) {
            List<StaffPO> staffs = new ArrayList<>();
            Map<Long, StaffPO> staffMap = staffQueryService
                    .listByIds(changeWorkingStatusCommands.stream().map(ChangeWorkingStatusCommand::getEmployeeId)
                                       .collect(Collectors.toSet()))
                    .stream().collect(Collectors.toMap(StaffPO::getId, staffPO -> staffPO));
            changeWorkingStatusCommands.forEach(changeWorkingStatusCommand -> {
                                                    StaffPO staffPO = staffMap.get(changeWorkingStatusCommand.getEmployeeId());
                                                    if (Objects.isNull(staffPO)) {
                                                        return;
                                                    }
                                                    // 条件：原始状态为空时才更新
                                                    if (changeWorkingStatusCommand.isCondition()) {
                                                        if (changeWorkingStatusCommand.isOriginalStatusIsNull() && StringUtils.isEmpty(staffPO.getState())) {
                                                            staffPO.setState(Objects.isNull(changeWorkingStatusCommand.getWorkingStatus()) ? "" : changeWorkingStatusCommand
                                                                    .getWorkingStatus().name());
                                                            staffs.add(staffPO);
                                                        }
                                                        if (CollectionUtils.isNotEmpty(changeWorkingStatusCommand.getOriginalInWorkingStatusList()) &&
                                                            changeWorkingStatusCommand.getOriginalInWorkingStatusList().contains(staffPO.getState())) {
                                                            staffPO.setState(Objects.isNull(changeWorkingStatusCommand.getWorkingStatus()) ? "" : changeWorkingStatusCommand
                                                                    .getWorkingStatus().name());
                                                            staffs.add(staffPO);
                                                        }
                                                    } else {
                                                        staffPO.setState(Objects.isNull(changeWorkingStatusCommand.getWorkingStatus()) ? "" : changeWorkingStatusCommand
                                                                .getWorkingStatus().name());
                                                        staffs.add(staffPO);
                                                    }
                                                }
                                               );

            staffCommandService.updateBatchById(staffs);
        }
    }

    /**
     * 人事管理-数据统计
     *
     * @return
     */
    @GetMapping("/staff/home-page/statistics")
    @DataPermissionSwitch(dataModel = "personnel:staff")
    public HomePageStatisticsVO getHomePageStatistics(BaseDateStatisticsParam param) {
        return staffQueryService.getHomePageStatistics(param);
    }

}
