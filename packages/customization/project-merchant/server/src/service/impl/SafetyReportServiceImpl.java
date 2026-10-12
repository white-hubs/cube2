package com.guhecloud.cube.deliver.project.merchant.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.deliver.project.merchant.commons.core.LoginUser;
import com.guhecloud.cube.deliver.project.merchant.entity.FirmManage;
import com.guhecloud.cube.deliver.project.merchant.entity.SafetyReport;
import com.guhecloud.cube.deliver.project.merchant.enums.SafeyReportEnum;
import com.guhecloud.cube.deliver.project.merchant.mapper.FirmManageMapper;
import com.guhecloud.cube.deliver.project.merchant.mapper.SafetyReportMapper;
import com.guhecloud.cube.deliver.project.merchant.service.SafetyReportService;
import com.guhecloud.cube.deliver.project.merchant.utils.SafetyReportUtils;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 安全月报 服务实现类
 * </p>
 *
 * @author zwb
 * @since 2021-06-24
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SafetyReportServiceImpl extends ServiceImpl<SafetyReportMapper, SafetyReport> implements SafetyReportService {

    private final SafetyReportMapper mapper;
    private final FirmManageMapper firmManageMapper;
    //    private final SysProjectApi sysProjectApi;

    private final SafetyReportUtils safetyReportUtils;
    //    private final SysUserApi sysUserApi;

    /**
     * 定时任务生成月报记录
     */
    @Override
    public void generatorCrontab() {
        try {
            Map<String, String> excelFilePathByMonthly = safetyReportUtils.getExcelFilePathByMonthly(LocalDateTime.now());

            List<SafetyReport> list = new ArrayList<>();
            //保存总部记录
            String hqTitle = excelFilePathByMonthly.get(SafetyReportUtils.SUMMARY_NAME);
            if (this.nonExistSafetyReportByFirm("", hqTitle)) {
                //                ProjectDTO hqProject = sysProjectApi.getByCode("hq");
                SafetyReport statements = new SafetyReport();
                //                statements.setProjectId(hqProject.getId());
                statements.setTitle(hqTitle);
                statements.setHqExcelPath(excelFilePathByMonthly.get(SafetyReportUtils.HQ_EXCEL_PATH));
                list.add(statements);
            }

            //保存公司记录
            List<FirmManage> firmManages = firmManageMapper.selectList(Wrappers.lambdaQuery());
            if (firmManages.size() > 0) {
                List<SafetyReport> collect = firmManages.stream().map(firmManage -> {
                    SafetyReport statement = null;
                    String firmTitle = excelFilePathByMonthly.get(firmManage.getName());
                    if (this.nonExistSafetyReportByFirm(firmManage.getId(), firmTitle)) {
                        statement = new SafetyReport();
                        statement.setProjectId(firmManage.getProjectId());
                        statement.setFirmId(firmManage.getId());
                        statement.setFirmName(firmManage.getName());
                        statement.setTitle(firmTitle);
                        statement.setStatus(SafeyReportEnum.STATUS_NOT_FILLED_IN.getCode());
                        statement.setHqExcelPath(excelFilePathByMonthly.get(SafetyReportUtils.HQ_EXCEL_PATH));
                    }
                    return statement;
                }).filter(Objects::nonNull).collect(Collectors.toList());
                list.addAll(collect);
            }

            list.forEach(mapper::insert);
        } catch (Exception e) {
            log.error("月报记录生成异常！！！", e);
            throw new ServiceException("月报记录生成异常！！！");
        }
    }

    @Override
    public void download(String id, HttpServletRequest request, HttpServletResponse response) {
        SafetyReport safetyReport = mapper.selectById(id);
        if (safetyReport != null) {
            XSSFWorkbook workbook = null;
            if (StringUtils.isBlank(safetyReport.getStatus())) {
                try {
                    workbook = safetyReportUtils.getXSSFWorkbook(safetyReport.getHqExcelPath());
                    safetyReportUtils.setFormulaRecalculationByCell(workbook);
                } catch (Exception e) {
                    log.error("工作簿获取异常", e);
                }
            } else {
                workbook = safetyReportUtils.getFirmExcel(safetyReport.getFirmName(), safetyReport.getHqExcelPath());
            }
            if (workbook == null) {
                throw new ServiceException("公司不存在，无法下载");
            }
            safetyReportUtils.downLoadExcel(workbook, safetyReport.getTitle(), request, response);
        }
    }

    @Override
    public void upload(String id, InputStream inputStream, String nickname) {
        if (StringUtils.isBlank(nickname)) {
            throw new ServiceException("填报人不能为空");
        }
        SafetyReport safetyReport = mapper.selectById(id);
        if (safetyReport != null) {
            this.updateHQExcel(safetyReport, inputStream);
            this.updateSafetyReport(safetyReport, nickname, null);
        }

    }

    @Override
    public void upload(String id, InputStream inputStream, LoginUser loginUser) {
        SafetyReport safetyReport = mapper.selectById(id);
        if (safetyReport != null) {
            this.updateHQExcel(safetyReport, inputStream);
            this.updateSafetyReport(safetyReport, null, loginUser);
        }
    }

    private void updateHQExcel(SafetyReport safetyReport, InputStream inputStream) {
        try {
            safetyReportUtils.setHqExcelDataByFirm(safetyReport.getFirmName(), inputStream, safetyReport.getHqExcelPath());
        } catch (Exception e) {
            log.error("文件解析异常！", e);
            throw new ServiceException("文件解析异常！");
        }
    }

    private void updateSafetyReport(SafetyReport safetyReport, String nickname, LoginUser loginUser) {
        if (loginUser != null) {
            //            UserInfoDTO user = this.getUser(loginUser.getId());
            //            if (user != null) {
            //                safetyReport.setUserId(user.getId());
            //                safetyReport.setUsername(user.getUsername());
            //                safetyReport.setNickname(user.getNickname());
            //            }
        } else {
            safetyReport.setNickname(nickname);
        }
        safetyReport.setStatus(SafeyReportEnum.STATUS_FILLED_IN.getCode());
        safetyReport.setFilledTime(LocalDateTime.now());
        mapper.updateById(safetyReport);
    }

    //    private UserInfoDTO getUser(String userId) {
    //        try {
    //            return sysUserApi.getById(userId);
    //        } catch (Exception e) {
    //            log.error("查询用户异常", e);
    //        }
    //        return null;
    //
    //    }

    private boolean nonExistSafetyReportByFirm(String firmId, String title) {
        return this.lambdaQuery().eq(StringUtils.isNotEmpty(firmId), SafetyReport::getFirmId, firmId).eq(SafetyReport::getTitle, title)
                       .one() == null;
    }

}
