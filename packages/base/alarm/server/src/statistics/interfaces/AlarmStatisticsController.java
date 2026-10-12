package com.guhecloud.cube.base.alarm.server.statistics.interfaces;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmAreaAnalysisStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmBasicAnalysisStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmCurrencyStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmDeviceAnalysisStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmEventAnalysisStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmEventStatusStatisticsExportVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmEventStatusStatisticsPageVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmHandlerAnalysisStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmLevelHandlerEfficiencyStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmLevelHandlerUserEfficiencyStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmLevelStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmLevelTrendStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmStatusStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmStatusTotalByUserExportVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmStatusTotalByUserPageVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmTrendAnalysisStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmTrendStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmTypeStatisticsVO;
import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmUserHandlerEfficiencyStatisticsVO;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmBimStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmBusinessKeyStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmDeviceStatusTotalQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmStatusTotalQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmStatusUserTotalQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmTargetTypeStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.AlarmTrendStatisticsQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.param.ColdWaterAlarmRecordQueryParam;
import com.guhecloud.cube.base.alarm.server.statistics.service.AlarmBimEventStatisticsService;
import com.guhecloud.cube.base.alarm.server.statistics.service.AlarmStatisticsService;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmApplicationStatisticsVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmAvgHandlerEfficiencyVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmBimStatisticsHandlerVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmDeviceEventRecordVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmDeviceStatusStatisticsPageVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmDeviceStatusStatisticsTrendVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmLevelTopStatisticsVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmRecordReportVO1;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmTargetTypeStatisticsVO;
import com.guhecloud.cube.base.alarm.server.statistics.vo.AlarmUserHandlerRateTopVO;
import com.guhecloud.cube.base.alarm.server.util.MathUtils;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 报警统计接口
 *
 * @author lanchun
 * @date 2022/6/7 13:58
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmStatisticsController {

    private final AlarmStatisticsService alarmStatisticsService;

    private final AlarmBimEventStatisticsService alarmBimEventStatisticsService;

    private final AreaClient areaClient;

    private final UserClient userClient;

    /**
     * 报警统计
     *
     * @param param
     *
     * @return
     */
    @DataPermissionSwitch(dataModel = "alarmCenter:alarmRecordHandlerUser")
    @GetMapping("/alarm-statistics")
    public AlarmStatisticsVO getAlarmStatistics(AlarmStatisticsQueryParam param) {
        AlarmStatisticsVO alarmStatisticsVO = AlarmStatisticsVO
                .builder()
                .alarmHandlerNum(this.alarmStatisticsService.getAlarmHandlerTotal(param))
                .alarmStatusStatisticsVOS(this.alarmStatisticsService.getAlarmStatusStatistics(param))
                .alarmResultStatisticsVOS(this.alarmStatisticsService.getAlarmResultStatistics(param))
                .build();
        AlarmBasicAnalysisStatisticsVO alarmBasicAnalysisRingStatistics = alarmStatisticsService.getAlarmBasicAnalysisRingStatistics(param);
        alarmStatisticsVO.setAlarmNum(alarmBasicAnalysisRingStatistics.getAlarmNum());
        alarmStatisticsVO.setAlarmRingRatio(alarmBasicAnalysisRingStatistics.getRingRatio());
        return alarmStatisticsVO;
    }

    /**
     * 报警状态分组统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/status-group")
    public List<AlarmStatusStatisticsVO> getAlarmStatusStatistics(AlarmStatisticsQueryParam param) {
        return this.alarmStatisticsService.getAlarmStatusStatistics(param);
    }

    /**
     * 报警趋势统计
     *
     * @param param
     *
     * @return
     */
    @DataPermissionSwitch(dataModel = "alarmCenter:alarmRecordHandlerUser")
    @GetMapping("/alarm-statistics/trend")
    public List<AlarmTrendStatisticsVO> getAlarmTrendStatistics(AlarmTrendStatisticsQueryParam param) {
        return this.alarmStatisticsService.getAlarmTrendStatistics(param);
    }

    /**
     * 已完成报警趋势统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/confirmed-trend")
    public List<AlarmTrendStatisticsVO> getAlarmConfirmedTrendStatistics(AlarmTrendStatisticsQueryParam param) {
        return this.alarmStatisticsService.getAlarmConfirmedTrendStatistics(param);
    }

    /**
     * 报警类型排名统计
     *
     * @param param
     *
     * @return
     */
    @DataPermissionSwitch(dataModel = "alarmCenter:alarmRecordHandlerUser")
    @GetMapping("/alarm-statistics/type-rank")
    public List<AlarmTypeStatisticsVO> getAlarmTypeRankStatistics(AlarmStatisticsQueryParam param) {
        return this.alarmStatisticsService.getAlarmTypeRankStatistics(param);
    }

    /**
     * 按报警对象类型统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/target-type")
    public List<AlarmTypeStatisticsVO> getAlarmTargetTypeStatistics(AlarmStatisticsQueryParam param) {
        return this.alarmStatisticsService.getAlarmTargetTypeStatistics(param);
    }

    /**
     * 按报警级别统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/level")
    public List<AlarmLevelStatisticsVO> getAlarmLevelStatistics(AlarmStatisticsQueryParam param) {
        List<AlarmLevelStatisticsVO> alarmLevelStatisticsVOS = this.alarmStatisticsService.getAlarmLevelStatistics(param);
        Long totalNum = alarmLevelStatisticsVOS.stream().mapToLong(AlarmLevelStatisticsVO::getAlarmNum).sum();
        alarmLevelStatisticsVOS.parallelStream().forEach(alarmLevelStatisticsVO -> {
            String ratio = "0.00%";
            if (totalNum > 0) {
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                //计算占比
                ratio = decimalFormat.format(new BigDecimal(alarmLevelStatisticsVO.getAlarmNum())
                                                     .divide(new BigDecimal(totalNum), 4, BigDecimal.ROUND_HALF_UP)
                                                     .multiply(new BigDecimal(100)).doubleValue()) + "%";
            }
            alarmLevelStatisticsVO.setRatio(ratio);
        });
        return alarmLevelStatisticsVOS;
    }

    /**
     * 按报警级别趋势统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/level-trend")
    public List<AlarmLevelTrendStatisticsVO> getAlarmLevelTrendStatistics(AlarmTrendStatisticsQueryParam param) {
        return this.alarmStatisticsService.getAlarmLevelTrendStatistics(param);
    }

    /**
     * 业务主键获取实际报警数量
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/business/total")
    public Long getBusinessKeyActualTotal(AlarmBusinessKeyStatisticsQueryParam param) {
        return this.alarmStatisticsService.getBusinessKeyActualTotal(param);
    }

    /**
     * 报警事件状态分组统计分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/event-status-page")
    public Pagination<AlarmEventStatusStatisticsPageVO> getAlarmStatusTotalPage(@Valid AlarmStatusTotalQueryParam param) {
        Page<AlarmEventStatusStatisticsPageVO> alarmStatusTotalPageVOPage = this.alarmStatisticsService.getAlarmStatusTotalPage(param);
        return PagingUtils.convert(alarmStatusTotalPageVOPage);
    }

    /**
     * 报警事件状态分组列表导出
     *
     * @param param
     *
     * @throws IOException
     */
    @PostMapping("/alarm-statistics/event-status/list/export")
    public void exportAlarmStatusTotalList(@Valid @RequestBody AlarmStatusTotalQueryParam param) throws IOException {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attr.getResponse();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        String excelName = URLEncoder.encode("报警统计", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
        List<AlarmEventStatusStatisticsPageVO> alarmEventStatusStatisticsPageVOS = this.alarmStatisticsService
                .getAlarmStatusTotalList(param);
        List<AlarmEventStatusStatisticsExportVO> alarmEventStatusStatisticsExportVOS = ObjectUtils
                .copying(alarmEventStatusStatisticsPageVOS, AlarmEventStatusStatisticsExportVO.class);
        EasyExcel.write(response.getOutputStream(), AlarmEventStatusStatisticsExportVO.class).sheet("报警统计")
                .doWrite(alarmEventStatusStatisticsExportVOS);
    }

    /**
     * 报警设备状态分组统计分页列表
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/alarm-statistics/device-status-page")
    public Pagination<AlarmDeviceStatusStatisticsPageVO> getDeviceAlarmStatusTotalPage(
            @RequestBody AlarmDeviceStatusTotalQueryParam param
                                                                                      ) {
        Page<AlarmDeviceStatusStatisticsPageVO> deviceAlarmStatusTotalPage = this.alarmStatisticsService
                .getDeviceAlarmStatusTotalPage(param);
        return PagingUtils.convert(deviceAlarmStatusTotalPage);
    }

    /**
     * 报警设备状态分组统计列表
     *
     * @param param 查询条件
     *
     * @return 设备报警状态统计
     */
    @PostMapping("/alarm-statistics/device-status/list")
    public List<AlarmDeviceStatusStatisticsPageVO> getDeviceAlarmStatusTotalList(
            @RequestBody AlarmDeviceStatusTotalQueryParam param
                                                                                ) {
        return this.alarmStatisticsService.getDeviceAlarmStatusTotalList(param);
    }

    /**
     * 报警设备状态分组列表导出
     *
     * @param param
     *
     * @throws IOException
     */
    @PostMapping("/alarm-statistics/device-status/list/export")
    public List<AlarmDeviceStatusStatisticsPageVO> exportDeviceAlarmStatusTotalList(
            @RequestBody AlarmDeviceStatusTotalQueryParam param
                                                                                   ) {
        List<AlarmDeviceStatusStatisticsPageVO> deviceAlarmStatusTotalList = this.alarmStatisticsService
                .getDeviceAlarmStatusTotalList(param);
        return deviceAlarmStatusTotalList;
    }

    /**
     * 按用户分组统计报警状态分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/user-status-page")
    public Pagination<AlarmStatusTotalByUserPageVO> getAlarmStatusTotalByUserPage(@Valid AlarmStatusUserTotalQueryParam param) {
        if (Objects.nonNull(param.getDepartmentId())) {
            List<UserDTO> userDTOS = this.userClient.organizationUsers(param.getDepartmentId());
            Set<Long> userIds = userDTOS.stream().map(userDTO -> userDTO.getId()).collect(Collectors.toSet());
            if (CollectionUtils.isEmpty(userIds)) {
                return Pagination.empty();
            }
            param.setUserIds(userIds);
        }
        Page<AlarmStatusTotalByUserPageVO> alarmStatusTotalByUserPageVOPage = this.alarmStatisticsService
                .getAlarmStatusTotalByUserPage(param);
        if (CollectionUtils.isEmpty(alarmStatusTotalByUserPageVOPage.getRecords())) {
            return Pagination.empty();
        }
        return PagingUtils.convert(alarmStatusTotalByUserPageVOPage);
    }

    /**
     * 按用户分组统计报警状态列表导出
     *
     * @param param
     *
     * @throws IOException
     */
    @PostMapping("/alarm-statistics/user-status/list/export")
    public void exportAlarmStatusTotalByUserList(@Valid @RequestBody AlarmStatusUserTotalQueryParam param) throws IOException {
        if (Objects.nonNull(param.getDepartmentId())) {
            List<UserDTO> userDTOS = this.userClient.organizationUsers(param.getDepartmentId());
            param.setUserIds(userDTOS.stream().map(userDTO -> userDTO.getId()).collect(Collectors.toSet()));
        }
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attr.getResponse();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        String excelName = URLEncoder.encode("报警处理统计", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + excelName + ExcelTypeEnum.XLSX.getValue());
        List<AlarmStatusTotalByUserPageVO> alarmStatusTotalByUserList = this.alarmStatisticsService.getAlarmStatusTotalByUserList(param);
        if (!CollectionUtils.isEmpty(alarmStatusTotalByUserList)) {
            //查询用户部门
            Set<Long> userIds = alarmStatusTotalByUserList.stream()
                    .map(alarmStatusTotalByUserPageVO -> alarmStatusTotalByUserPageVO.getUserId())
                    .collect(Collectors.toSet());
            Map<Long, UserDTO> userDTOMap = this.userClient.listUserByIds(userIds).stream()
                    .collect(Collectors.toMap(UserDTO::getId, v -> v));
            alarmStatusTotalByUserList.parallelStream().forEach(alarmStatusTotalByUser -> {
                //查询用户信息
                UserDTO userDTO = userDTOMap.get(alarmStatusTotalByUser.getUserId());
                if (Objects.nonNull(userDTO)) {
                    alarmStatusTotalByUser.setDepartmentId(
                            Objects.nonNull(userDTO.getOrganization()) ? userDTO.getOrganization().getId() : null);
                    alarmStatusTotalByUser.setDepartmentName(
                            Objects.nonNull(userDTO.getOrganization()) ? userDTO.getOrganization().getName() : null);
                }
            });
        }
        List<AlarmStatusTotalByUserExportVO> alarmStatusTotalByUserExportVOS = ObjectUtils
                .copying(alarmStatusTotalByUserList, AlarmStatusTotalByUserExportVO.class);
        EasyExcel.write(response.getOutputStream(), AlarmStatusTotalByUserExportVO.class).sheet("报警处理统计")
                .doWrite(alarmStatusTotalByUserExportVOS);
    }

    /**
     * 报警事件报警分析
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/event-analysis")
    public AlarmEventAnalysisStatisticsVO getAlarmEventAnalysisStatistics(@Valid AlarmStatisticsQueryParam param) {
        List<AlarmCurrencyStatisticsVO> alarmCurrencyStatisticsVOS = this.alarmStatisticsService.getAlarmEventGroupStatisticsTopTen(param);
        if (CollectionUtils.isEmpty(alarmCurrencyStatisticsVOS)) {
            return AlarmEventAnalysisStatisticsVO.builder()
                    .alarmCurrencyStatisticsVOS(Collections.emptyList())
                    .build();
        }
        //前三条报警数量最高的事件
        List<AlarmCurrencyStatisticsVO> topThree = alarmCurrencyStatisticsVOS.stream().sorted(Comparator
                                                                                                      .comparing(AlarmCurrencyStatisticsVO::getTotalNum)
                                                                                                      .reversed()).limit(3)
                .collect(Collectors.toList());
        AlarmEventAnalysisStatisticsVO alarmEventAnalysisStatisticsVO = AlarmEventAnalysisStatisticsVO.builder()
                .alarmTopThreeEventNames(topThree
                                                 .stream()
                                                 .map(alarmCurrencyStatisticsVO -> alarmCurrencyStatisticsVO.getName())
                                                 .collect(Collectors.joining("、")))
                .alarmCurrencyStatisticsVOS(alarmCurrencyStatisticsVOS)
                .build();
        //数量最高的事件
        Optional<AlarmCurrencyStatisticsVO> optionalAlarmCurrencyStatisticsVO = topThree.stream()
                .max(Comparator.comparing(AlarmCurrencyStatisticsVO::getTotalNum));
        optionalAlarmCurrencyStatisticsVO.ifPresent(topAlarmCurrencyStatisticsVO -> {
            alarmEventAnalysisStatisticsVO.setAlarmTopEventName(topAlarmCurrencyStatisticsVO.getName());
            alarmEventAnalysisStatisticsVO.setAlarmTopNum(topAlarmCurrencyStatisticsVO.getTotalNum());
        });
        return alarmEventAnalysisStatisticsVO;
    }

    /**
     * 报警事件分组统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/event-group")
    public List<AlarmCurrencyStatisticsVO> getAlarmEventGroupStatistics(@Valid AlarmStatisticsQueryParam param) {
        return this.alarmStatisticsService.getAlarmEventGroupStatistics(param);
    }

    /**
     * 报警区域报警分析
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/area-analysis")
    public AlarmAreaAnalysisStatisticsVO getAlarmAreaAnalysisStatistics(@Valid AlarmStatisticsQueryParam param) {
        List<AreaDTO> areaDTOS = areaClient.getAreaByLevelOne(1);
        List<AlarmCurrencyStatisticsVO> alarmCurrencyStatisticsVOS = this.alarmStatisticsService.getAlarmAreaGroupStatistics(param);
        if (CollectionUtils.isEmpty(areaDTOS) || CollectionUtils.isEmpty(alarmCurrencyStatisticsVOS)) {
            return AlarmAreaAnalysisStatisticsVO.builder()
                    .alarmCurrencyStatisticsVOS(Collections.emptyList())
                    .build();
        }
        //二级区域的分析统计
        List<AlarmCurrencyStatisticsVO> twoAreaAlarmCurrencyStatisticsVOS = Collections.synchronizedList(new ArrayList<>(areaDTOS.size()));
        areaDTOS.parallelStream().forEach(areaDTO -> {
            Integer twoAlarmTotalCount = alarmCurrencyStatisticsVOS.stream()
                    .filter(alarmCurrencyStatisticsVO -> (
                            alarmCurrencyStatisticsVO.getId() +
                            "-").startsWith(areaDTO.getSequence()))
                    .mapToInt(alarmCurrencyStatisticsVO -> alarmCurrencyStatisticsVO.getTotalNum())
                    .sum();
            twoAreaAlarmCurrencyStatisticsVOS.add(AlarmCurrencyStatisticsVO
                                                          .builder()
                                                          .id(String.valueOf(areaDTO.getId()))
                                                          .name(areaDTO.getName())
                                                          .totalNum(twoAlarmTotalCount)
                                                          .build());
        });
        //前三条报警数量最高的二级区域
        List<AlarmCurrencyStatisticsVO> topThree = twoAreaAlarmCurrencyStatisticsVOS.stream().sorted(Comparator
                                                                                                             .comparing(AlarmCurrencyStatisticsVO::getTotalNum)
                                                                                                             .reversed()).limit(3)
                .collect(Collectors.toList());
        AlarmAreaAnalysisStatisticsVO alarmAreaAnalysisStatisticsVO = AlarmAreaAnalysisStatisticsVO.builder()
                .alarmTopThreeAreaNames(topThree
                                                .stream()
                                                .map(alarmCurrencyStatisticsVO -> alarmCurrencyStatisticsVO.getName())
                                                .collect(Collectors.joining("、")))
                .alarmCurrencyStatisticsVOS(alarmCurrencyStatisticsVOS)
                .build();
        //数量最高的区域
        Optional<AlarmCurrencyStatisticsVO> optionalAlarmCurrencyStatisticsVO = topThree.stream()
                .max(Comparator.comparing(AlarmCurrencyStatisticsVO::getTotalNum));
        optionalAlarmCurrencyStatisticsVO.ifPresent(topAlarmCurrencyStatisticsVO -> {
            alarmAreaAnalysisStatisticsVO.setAlarmTopAreaName(topAlarmCurrencyStatisticsVO.getName());
            alarmAreaAnalysisStatisticsVO.setAlarmTopNum(topAlarmCurrencyStatisticsVO.getTotalNum());
        });
        return alarmAreaAnalysisStatisticsVO;
    }

    /**
     * 报警区域分组统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/area-group")
    public List<AlarmCurrencyStatisticsVO> getAlarmAreaGroupStatistics(@Valid AlarmStatisticsQueryParam param) {
        List<AreaDTO> areaDTOS = areaClient.getAreaByLevelOne(1);
        if (CollectionUtils.isEmpty(areaDTOS)) {
            return Collections.emptyList();
        }
        List<AlarmCurrencyStatisticsVO> alarmCurrencyStatisticsVOS = this.alarmStatisticsService.getAlarmAreaGroupStatistics(param);
        List<AlarmCurrencyStatisticsVO> alarmCurrencyStatisticsResVOS = Collections.synchronizedList(new ArrayList<>(areaDTOS.size()));
        areaDTOS.parallelStream().forEach(areaDTO -> {
            Integer twoAlarmTotalCount = alarmCurrencyStatisticsVOS.stream()
                    .filter(alarmCurrencyStatisticsVO -> (
                            alarmCurrencyStatisticsVO.getId() +
                            "-").startsWith(areaDTO.getSequence()))
                    .mapToInt(alarmCurrencyStatisticsVO -> alarmCurrencyStatisticsVO.getTotalNum())
                    .sum();
            alarmCurrencyStatisticsResVOS.add(AlarmCurrencyStatisticsVO
                                                      .builder()
                                                      .id(String.valueOf(areaDTO.getId()))
                                                      .name(areaDTO.getName())
                                                      .totalNum(twoAlarmTotalCount)
                                                      .build());
        });
        Integer totalNum = alarmCurrencyStatisticsResVOS.stream().mapToInt(AlarmCurrencyStatisticsVO::getTotalNum).sum();
        alarmCurrencyStatisticsResVOS.parallelStream().forEach(areaDTO -> {
            if (totalNum > 0) {
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                //计算占比
                areaDTO.setRatio(
                        decimalFormat.format(
                                new BigDecimal(areaDTO.getTotalNum())
                                        .divide(new BigDecimal(totalNum), 4, BigDecimal.ROUND_HALF_UP)
                                        .multiply(new BigDecimal(100)).doubleValue()) + "%");
            } else {
                areaDTO.setRatio("0.00%");
            }
        });
        return alarmCurrencyStatisticsResVOS;
    }

    /**
     * 报警区域TOP10
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/area-top-ten")
    public List<AlarmCurrencyStatisticsVO> getAlarmAreaTopTenStatistics(@Valid AlarmStatisticsQueryParam param) {
        List<AlarmCurrencyStatisticsVO> alarmCurrencyStatisticsVOS = this.alarmStatisticsService.getAlarmAreaGroupStatistics(param);
        if (CollectionUtils.isEmpty(alarmCurrencyStatisticsVOS)) {
            return Collections.emptyList();
        }
        List<AreaDTO> areaDTOS = areaClient.getAreaByLevelOne(1);
        List<AlarmCurrencyStatisticsVO> alarmCurrencyStatisticsResVOS = Collections.synchronizedList(new ArrayList<>(areaDTOS.size()));
        areaDTOS.parallelStream().forEach(areaDTO -> {
            Integer twoAlarmTotalCount = alarmCurrencyStatisticsVOS.stream()
                    .filter(alarmCurrencyStatisticsVO -> (
                            alarmCurrencyStatisticsVO.getId() +
                            "-").startsWith(areaDTO.getSequence()))
                    .mapToInt(alarmCurrencyStatisticsVO -> alarmCurrencyStatisticsVO.getTotalNum())
                    .sum();
            alarmCurrencyStatisticsResVOS.add(AlarmCurrencyStatisticsVO
                                                      .builder()
                                                      .id(String.valueOf(areaDTO.getId()))
                                                      .name(areaDTO.getName())
                                                      .totalNum(twoAlarmTotalCount)
                                                      .build());
        });
        Integer totalNum = alarmCurrencyStatisticsResVOS.stream().mapToInt(AlarmCurrencyStatisticsVO::getTotalNum).sum();
        alarmCurrencyStatisticsResVOS.parallelStream().forEach(areaDTO -> {
            if (totalNum > 0) {
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                //计算占比
                areaDTO.setRatio(
                        decimalFormat.format(
                                new BigDecimal(areaDTO.getTotalNum())
                                        .divide(new BigDecimal(totalNum), 4, BigDecimal.ROUND_HALF_UP)
                                        .multiply(new BigDecimal(100)).doubleValue()) + "%");
            } else {
                areaDTO.setRatio("0.00%");
            }
        });
        return alarmCurrencyStatisticsResVOS.stream()
                .sorted(Comparator.comparing(AlarmCurrencyStatisticsVO::getTotalNum).reversed())
                .limit(10).collect(Collectors.toList());
    }

    /**
     * 报警设备分析
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/device-analysis")
    public AlarmDeviceAnalysisStatisticsVO getAlarmDeviceAnalysisTopTenStatistics(@Valid AlarmStatisticsQueryParam param) {
        List<AlarmCurrencyStatisticsVO> alarmCurrencyStatisticsVOS = this.alarmStatisticsService.getAlarmDeviceGroupTopTenStatistics(param);
        if (CollectionUtils.isEmpty(alarmCurrencyStatisticsVOS)) {
            return AlarmDeviceAnalysisStatisticsVO.builder()
                    .alarmCurrencyStatisticsVOS(Collections.emptyList())
                    .build();
        }
        //前三条报警数量最高的设备
        List<AlarmCurrencyStatisticsVO> topThree = alarmCurrencyStatisticsVOS.stream().sorted(Comparator
                                                                                                      .comparing(AlarmCurrencyStatisticsVO::getTotalNum)
                                                                                                      .reversed()).limit(3)
                .collect(Collectors.toList());
        AlarmDeviceAnalysisStatisticsVO alarmDeviceAnalysisStatisticsVO = AlarmDeviceAnalysisStatisticsVO.builder()
                .alarmTopThreeDeviceNames(topThree
                                                  .stream()
                                                  .map(alarmCurrencyStatisticsVO -> alarmCurrencyStatisticsVO.getName())
                                                  .collect(Collectors.joining("、")))
                .alarmCurrencyStatisticsVOS(alarmCurrencyStatisticsVOS)
                .build();
        //数量最高的设备
        Optional<AlarmCurrencyStatisticsVO> optionalAlarmCurrencyStatisticsVO = topThree.stream()
                .max(Comparator.comparing(AlarmCurrencyStatisticsVO::getTotalNum));
        optionalAlarmCurrencyStatisticsVO.ifPresent(topAlarmCurrencyStatisticsVO -> {
            alarmDeviceAnalysisStatisticsVO.setAlarmTopDeviceName(topAlarmCurrencyStatisticsVO.getName());
            alarmDeviceAnalysisStatisticsVO.setAlarmTopNum(topAlarmCurrencyStatisticsVO.getTotalNum());
        });
        return alarmDeviceAnalysisStatisticsVO;
    }

    /**
     * 报警设备top10
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/device-top-ten")
    public List<AlarmCurrencyStatisticsVO> getAlarmDeviceGroupTopTenStatistics(@Valid AlarmStatisticsQueryParam param) {
        List<AlarmCurrencyStatisticsVO> alarmCurrencyStatisticsVOS = this.alarmStatisticsService.getAlarmDeviceGroupTopTenStatistics(param);
        if (CollectionUtils.isEmpty(alarmCurrencyStatisticsVOS)) {
            return Collections.emptyList();
        }
        Long totalAlarmNum = this.alarmStatisticsService.getAlarmTotal(param);
        alarmCurrencyStatisticsVOS.stream().forEach(alarmCurrencyStatisticsVO -> {
            double ringRatio = new BigDecimal(alarmCurrencyStatisticsVO.getTotalNum())
                    .divide(new BigDecimal(totalAlarmNum), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal(100)).doubleValue();
            DecimalFormat b = new DecimalFormat("0.00");
            String format = b.format(ringRatio);
            alarmCurrencyStatisticsVO.setRatio(format);
        });
        return alarmCurrencyStatisticsVOS;
    }

    /**
     * 报警处理分析
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/user-handler-analysis")
    public AlarmHandlerAnalysisStatisticsVO getAlarmHandlerAnalysisStatistics(AlarmStatisticsQueryParam param) {
        return this.alarmStatisticsService.getAlarmHandlerAnalysisStatistics(param);
    }

    /**
     * 报警级别处理效率统计
     *
     * @return
     */
    @GetMapping("/alarm-statistics/level-handler-efficiency-analysis")
    public List<AlarmLevelHandlerEfficiencyStatisticsVO> getAlarmLevelHandlerEfficiencyStatistics(AlarmStatisticsQueryParam param
                                                                                                 ) {
        return this.alarmStatisticsService.getAlarmLevelHandlerEfficiencyStatistics(param);
    }

    /**
     * 用户报警处理效率排名前10
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/user-handler-efficiency-top-ten-analysis")
    public List<AlarmLevelHandlerUserEfficiencyStatisticsVO> getAlarmUserHandlerEfficiencyTopTenStatistics(
            AlarmStatisticsQueryParam param
                                                                                                          ) {
        List<AlarmLevelHandlerUserEfficiencyStatisticsVO> alarmLevelHandlerUserEfficiencyStatisticsVOS = this.alarmStatisticsService
                .getAlarmUserHandlerEfficiencyTopTenStatistics(param);
        alarmLevelHandlerUserEfficiencyStatisticsVOS.stream().forEach(alarmLevelHandlerUserEfficiencyStatisticsVO ->
                                                                              alarmLevelHandlerUserEfficiencyStatisticsVO
                                                                                      .setAverageProcessing(MathUtils
                                                                                                                    .dateConvertRemark(alarmLevelHandlerUserEfficiencyStatisticsVO
                                                                                                                                               .getAvgProcessSeconds()))
                                                                     );
        return alarmLevelHandlerUserEfficiencyStatisticsVOS;
    }

    /**
     * 用户报警处理效率排名倒数前10
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/user-handler-efficiency-last-top-ten-analysis")
    public List<AlarmLevelHandlerUserEfficiencyStatisticsVO> getAlarmUserHandlerEfficiencyLastTopTenStatistics(
            @Valid AlarmStatisticsQueryParam param
                                                                                                              ) {
        List<AlarmLevelHandlerUserEfficiencyStatisticsVO> alarmLevelHandlerUserEfficiencyStatisticsVOS = this.alarmStatisticsService
                .getAlarmUserHandlerEfficiencyLastTopTenStatistics(param);
        alarmLevelHandlerUserEfficiencyStatisticsVOS.stream().forEach(alarmLevelHandlerUserEfficiencyStatisticsVO ->
                                                                              alarmLevelHandlerUserEfficiencyStatisticsVO
                                                                                      .setAverageProcessing(MathUtils
                                                                                                                    .dateConvertRemark(alarmLevelHandlerUserEfficiencyStatisticsVO
                                                                                                                                               .getAvgProcessSeconds()))
                                                                     );
        return alarmLevelHandlerUserEfficiencyStatisticsVOS;
    }

    /**
     * 报警总数环比率统计
     *
     * @param param
     *
     * @return
     */
    @DataPermissionSwitch(dataModel = "alarmCenter:alarmRecordHandlerUser")
    @GetMapping("/alarm-statistics/ring-ratio-analysis")
    public AlarmBasicAnalysisStatisticsVO getAlarmBasicAnalysisRingStatistics(@SpringQueryMap AlarmStatisticsQueryParam param) {
        return this.alarmStatisticsService.getAlarmBasicAnalysisRingStatistics(param);
    }

    /**
     * 报警趋势分析
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/trend-analysis")
    public AlarmTrendAnalysisStatisticsVO getAlarmTrendAnalysisStatistics(@Valid AlarmStatisticsQueryParam param) {
        return this.alarmStatisticsService.getAlarmTrendAnalysisStatistics(param);
    }

    /**
     * 报警对象类型数量统计
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/alarm-statistics/target-type/statistics")
    public List<AlarmTargetTypeStatisticsVO> getAlarmTargetTypeStatistics(@RequestBody AlarmTargetTypeStatisticsQueryParam param
                                                                         ) {
        return this.alarmStatisticsService.getAlarmTargetTypeStatistics(param);
    }

    /**
     * 应用分组数量统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/application-group-statistics")
    public List<AlarmApplicationStatisticsVO> getApplicationGroupStatistics(AlarmBimStatisticsQueryParam param) {
        return this.alarmStatisticsService.getApplicationGroupStatistics(param);
    }

    /**
     * 冷东水报警趋势分析
     *
     * @param param
     *
     * @return
     */
    @GetMapping("cold-water/alarm-statistics/trend-analysis")
    public List<AlarmDeviceStatusStatisticsTrendVO> getColdWaterAlarmTrendAnalysisStatistics(
            @Valid AlarmStatisticsQueryParam param
                                                                                            ) {
        return this.alarmStatisticsService.getColdWaterAlarmTrendAnalysisStatistics(param);
    }

    /**
     * 冷东水报警事件记录
     *
     * @param param
     *
     * @return
     */
    @GetMapping("cold-water/alarm/event-record")
    public Pagination<AlarmDeviceEventRecordVO> getColdWaterAlarmEventRecord(@Valid ColdWaterAlarmRecordQueryParam param) {
        return this.alarmStatisticsService.getColdWaterAlarmEventRecord(param);
    }

    /**
     * 获取指定应用报警数
     * 可传多个应用编码
     *
     * @param param
     *
     * @return
     */
    @GetMapping("alarm/record/application")
    public AlarmRecordReportVO1 getAlarmRecordByApplication(AlarmStatisticsQueryParam param) {
        return this.alarmStatisticsService.getAlarmRecordByApplication(param);
    }

    /**
     * 安全报表-报警统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("alarm/record/safety-report")
    public Pagination<AlarmStatusTotalByUserPageVO> getAlarmRecordForSafetyReport(AlarmStatusUserTotalQueryParam param) {
        Page<AlarmStatusTotalByUserPageVO> alarmStatusTotalByUserPageVOPage = this.alarmStatisticsService
                .getAlarmRecordForSafetyReport(param);
        if (CollectionUtils.isEmpty(alarmStatusTotalByUserPageVOPage.getRecords())) {
            return Pagination.empty();
        }
        return PagingUtils.convert(alarmStatusTotalByUserPageVOPage);
    }

    /**
     * 安全报表-报警统计-导出
     *
     * @param param
     *
     * @return
     */
    @PostMapping("alarm/record/safety-report/export")
    public void getAlarmRecordForSafetyReportExport(@Valid @RequestBody AlarmStatusUserTotalQueryParam param) {
        this.alarmStatisticsService.getAlarmRecordForSafetyReportExport(param);
    }

    /**
     * 安全报表-部门统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("alarm/record/safety-report/department")
    public Pagination<AlarmStatusTotalByUserPageVO> getAlarmRecordForSafetyReportByDepartment(
            AlarmStatusUserTotalQueryParam param
                                                                                             ) {
        Page<AlarmStatusTotalByUserPageVO> alarmStatusTotalByUserPageVOPage = this.alarmStatisticsService
                .getAlarmRecordForSafetyReportByDepartment(param);
        if (CollectionUtils.isEmpty(alarmStatusTotalByUserPageVOPage.getRecords())) {
            return Pagination.empty();
        }
        return PagingUtils.convert(alarmStatusTotalByUserPageVOPage);
    }

    /**
     * 安全报表-部门统计-导出
     *
     * @param param
     *
     * @return
     */
    @PostMapping("alarm/record/safety-report/department/export")
    public void getAlarmRecordForSafetyReportByDepartmentExport(@Valid @RequestBody AlarmStatusUserTotalQueryParam param) {
        this.alarmStatisticsService.getAlarmRecordForSafetyReportByDepartmentExport(param);
    }

    /**
     * 报警事件分类列表总数统计
     *
     * @param param
     *
     * @return
     */
    @DataPermissionSwitch(dataModel = "alarmCenter:alarmRecordHandlerUser")
    @PostMapping("/alarm/event-type/statistics")
    public List<AlarmBimStatisticsHandlerVO> getEventTypeHandlerListStatistics(@RequestBody AlarmStatisticsQueryParam param) {
        return alarmBimEventStatisticsService.getEventTypeListStatistics(param);
    }

    /**
     * 报警级别处理率统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/level/handler-rate/statistics")
    public List<AlarmLevelStatisticsVO> getAlarmLevelHandlerRateStatistics(AlarmStatisticsQueryParam param) {
        return alarmStatisticsService.getAlarmLevelHandlerRateStatistics(param);
    }

    /**
     * 报警处理效率
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/avg-handler-efficiency")
    public AlarmAvgHandlerEfficiencyVO getAlarmAvgHandlerEfficiency(AlarmStatisticsQueryParam param) {
        return this.alarmStatisticsService.getAlarmAvgHandlerEfficiency(param);
    }

    /**
     * 报警级别处理效率统计
     *
     * @return
     */
    @GetMapping("/alarm-statistics/level/avg-handler-efficiency")
    public List<AlarmLevelHandlerEfficiencyStatisticsVO> getAlarmLevelHandlerEfficiency(AlarmStatisticsQueryParam param) {
        return this.alarmStatisticsService.getAlarmLevelHandlerEfficiency(param);
    }

    /**
     * 用户报警处理率排名
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/user/handler-rate/top")
    public List<AlarmUserHandlerRateTopVO> getAlarmUserHandlerRateTopStatistics(AlarmStatisticsQueryParam param) {
        return this.alarmStatisticsService.getAlarmUserHandlerRateTopStatistics(param);
    }

    /**
     * 用户报警处理率倒数排名
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/user/handler-rate/last-top")
    public List<AlarmUserHandlerRateTopVO> getAlarmUserHandlerRateLastTopStatistics(AlarmStatisticsQueryParam param) {
        return this.alarmStatisticsService.getAlarmUserHandlerRateLastTopStatistics(param);
    }

    /**
     * 用户报警处理效率排名
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/user-handler-efficiency-top")
    public List<AlarmUserHandlerEfficiencyStatisticsVO> getAlarmUserHandlerEfficiencyTopStatistics(AlarmStatisticsQueryParam param) {
        return this.alarmStatisticsService.getAlarmUserHandlerEfficiencyTopStatistics(param);
    }

    /**
     * 用户报警处理效率排名
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/user-handler-efficiency-last-top")
    public List<AlarmUserHandlerEfficiencyStatisticsVO> getAlarmUserHandlerEfficiencyLastTopStatistics(AlarmStatisticsQueryParam param) {
        return this.alarmStatisticsService.getAlarmUserHandlerEfficiencyLastTopStatistics(param);
    }

    /**
     * 报警区域分组统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/area")
    public List<AlarmCurrencyStatisticsVO> getAlarmAreaStatistics(@Valid AlarmStatisticsQueryParam param) {
        return this.alarmStatisticsService.getAlarmAreaGroupStatistics(param);
    }

    /**
     * 报警总数和处理数
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/alarm-statistics/alarm-number-analysis")
    public AlarmBasicAnalysisStatisticsVO getAlarmBasicAnalysisNumberStatistics(AlarmStatisticsQueryParam param) {
        return AlarmBasicAnalysisStatisticsVO.builder()
                .alarmNum(alarmStatisticsService.getAlarmTotal(param))
                .handlerNum(alarmStatisticsService.getAlarmHandlerTotal(param))
                .build();
    }

    /**
     * 报警数最高的等级、处理率最高的等级、处理效率最高的等级
     */
    @GetMapping("/alarm-statistics/level/top")
    AlarmLevelTopStatisticsVO getAlarmLevelTopStatistics(AlarmStatisticsQueryParam param) {
        return alarmStatisticsService.getAlarmLevelTopStatistics(param);
    }
}
