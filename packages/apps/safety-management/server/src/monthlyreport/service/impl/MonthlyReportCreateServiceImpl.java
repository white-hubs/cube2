package com.guhecloud.cube.srv.gen.safety.management.monthlyreport.service.impl;

import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.param.FileModifyParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.constant.SafetyManageConstant;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.excel.MonthlyReportExcel;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.service.MarketMonthlyReportService;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.service.MonthlyReportCreateService;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.service.MonthlyReportService;
import com.guhecloud.cube.srv.gen.safety.management.po.MarketConfigPO;
import com.guhecloud.cube.srv.gen.safety.management.service.MarketConfigService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.SpringFileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MonthlyReportCreateServiceImpl implements MonthlyReportCreateService {

    private final MonthlyReportService monthlyReportService;

    private final MarketMonthlyReportService marketMonthlyReportService;

    private final MarketConfigService marketConfigService;

    private final FileClient fileClient;

    /**
     * 月报生成
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createMonthlyReport() {
        SessionContextHolder.clearSessionContext();
        AtomicReference<Map<Integer, List<MarketConfigPO>>> marketConfigMap = new AtomicReference<>(new HashMap<>());
        var marketConfigPOList = this.marketConfigService.list();
        Optional.ofNullable(marketConfigPOList).ifPresent(configList -> marketConfigMap.set(configList.stream()
                                                                                                    .collect(Collectors.groupingBy(MarketConfigPO::getTenant))));

        marketConfigMap.get().forEach((tenant, marketConfigPOS) -> {
            SessionContextHolder.requestTenant(tenant);
            var now = LocalDateTime.now();
            var date = String.format("%d年%d月", now.getYear(), now.getMonthValue());
            try {
                //总部月报数据生成
                var monthlyReportId = createMonthlyReport(now, date);
                //市场月报数据生成
                createMarketMonthlyReport(now, date, monthlyReportId, marketConfigPOS, tenant);
            } catch (Exception e) {
                log.error("生成月报数据失败,失败原因:", e);
                throw new ServiceException("生成月报失败");
            }
        });
        SessionContextHolder.clearSessionContext();
    }

    private Long createMonthlyReport(LocalDateTime now, String date) {
        //生成汇总文件
        var fileName = MonthlyReportExcel.createSummaryFileName(date);
        return this.monthlyReportService.createMonthlyReport(fileName.substring(0, fileName.lastIndexOf(".")), date, DateUtils.beginOfMonth(now));
    }

    private void createMarketMonthlyReport(LocalDateTime now, String date, Long monthlyReportId, List<MarketConfigPO> marketConfigPOS,
                                           Integer tenant
                                          ) {
        for (MarketConfigPO marketConfig : marketConfigPOS) {
            var fileName = MonthlyReportExcel.createMarketFileName(marketConfig.getMarketName(), date);
            this.marketMonthlyReportService.createMonthlyReport(fileName.substring(0, fileName.lastIndexOf(".")), monthlyReportId, date, DateUtils.beginOfMonth(now),
                                                                marketConfig);
        }
    }

    public FileVO uploadFile(String fileName, Integer tenant) throws IOException {
        var fileBytes = SpringFileUtils.getFileBytes(MonthlyReportExcel.getFile(fileName));
        var multipartFile = SpringFileUtils.fileByteConvertMultipartFile(fileName, fileBytes);
        return fileClient.upload(FileModifyParam.builder()
                                         .file(multipartFile)
                                         .businessKey(SafetyManageConstant.FILE_BUCKET_KEY)
                                         .tenant(tenant)
                                         .build());
    }
}
