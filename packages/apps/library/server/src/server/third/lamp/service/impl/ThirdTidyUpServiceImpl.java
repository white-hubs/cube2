package com.guhecloud.cube.srv.gen.library.management.server.third.lamp.service.impl;

import com.guhecloud.cube.srv.gen.library.management.server.po.SystemLoginInfoPO;
import com.guhecloud.cube.srv.gen.library.management.server.service.LibraryLoginInfoService;
import com.guhecloud.cube.srv.gen.library.management.server.third.lamp.service.ThirdTidyUpService;
import com.guhecloud.cube.srv.gen.library.management.server.third.lamp.utils.ThirdHttpLibraryUtils;
import com.guhecloud.cube.srv.gen.library.management.server.third.lamp.utils.ThirdLibraryServiceUtils;
import com.guhecloud.cube.srv.gen.library.management.server.vo.param.TidyUpRecordPageParam;
import com.guhecloud.cube.srv.gen.library.management.server.vo.param.TidyUpStatisticsPageParam;
import com.guhecloud.cube.srv.gen.library.management.server.vo.result.TidyUpRecordResult;
import com.guhecloud.cube.srv.gen.library.management.server.vo.result.TidyUpStatisticsResult;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ThirdTidyUpServiceImpl implements ThirdTidyUpService {
    private final LibraryLoginInfoService libraryLoginInfoService;
    private static final String TIDY_UP_RECORD_PAGE_URL = "App.Check_Report.Workload";
    private static final String TIDY_UP_STATISTICS_URL = "App.Check_Report.Comreportlist";

    /**
     * 整理编目记录列表(操作员工作量)
     *
     * @param param 入参
     *
     * @return 分页列表
     */
    @Override
    public Pagination<TidyUpRecordResult> tidyUpRecordPage(TidyUpRecordPageParam param) {
        Map<String, Object> map = new HashMap<>();
        map.put("perpage", param.getPageSize());
        map.put("page", param.getPageNumber());
        if (StringUtils.isNotBlank(param.getName())) {
            map.put("admin", param.getName());
        }
        if (StringUtils.isNotBlank(param.getBookType())) {
            map.put("bookclass", param.getBookType());
        }
        Period period = param.getPeriod();
        if (period != null && period.getBegin() != null && period.getEnd() != null) {
            map.put("catalogdate", DateUtils.format(period.getBegin()));
            map.put("catalogdate2", DateUtils.format(period.getEnd()));
        }
        SystemLoginInfoPO systemLoginInfoPO = libraryLoginInfoService.getLoginInfoByLibrary();
        Object obj = ThirdHttpLibraryUtils.sendThirdPost(TIDY_UP_RECORD_PAGE_URL, map, systemLoginInfoPO);
        if (obj == null) {
            return Pagination.empty();
        }

        Pagination<ThirdTidyUpDTO> page = ThirdLibraryServiceUtils.getObjPage(obj, ThirdTidyUpDTO.class);

        return new Pagination<>(page.getTotal(), page.getPages(), page.getRecords().stream().map(d -> {
            TidyUpRecordResult result = new TidyUpRecordResult();
            result.setTidyTime(d.getCatalogdate());
            result.setName(d.getAdmin());
            result.setTidyNumber(d.getCatalognums());
            result.setUpdateNumber(d.getCorrectnums());
            result.setBookType(d.getBk_class());
            return result;
        }).collect(Collectors.toList()));
    }

    /**
     * 编目整理统计分页列表
     *
     * @param param 入参
     *
     * @return 统计列表
     */
    @Override
    public Pagination<TidyUpStatisticsResult> tidyUpStatisticsPage(TidyUpStatisticsPageParam param) {
        Map<String, Object> map = new HashMap<>();
        map.put("perpage", param.getPageSize());
        map.put("page", param.getPageNumber());
        if (StringUtils.isNotBlank(param.getTidyBatch())) {
            map.put("catalogbatch", param.getTidyBatch());
        }
        if (StringUtils.isNotBlank(param.getOperator())) {
            map.put("admin", param.getOperator());
        }
        if (param.getStatus() != null) {
            map.put("bookstate", param.getStatus());
        }
        SystemLoginInfoPO systemLoginInfoPO = libraryLoginInfoService.getLoginInfoByLibrary();
        Object obj = ThirdHttpLibraryUtils.sendThirdPost(TIDY_UP_STATISTICS_URL, map, systemLoginInfoPO);
        if (obj == null) {
            return Pagination.empty();
        }

        Pagination<TidyUpStatisticsDTO> page = ThirdLibraryServiceUtils.getObjPage(obj, TidyUpStatisticsDTO.class);

        return new Pagination<>(page.getTotal(), page.getPages(), page.getRecords().stream().map(d -> {
            TidyUpStatisticsResult result = new TidyUpStatisticsResult();
            result.setStatus(d.getBookstate());
            result.setTidyBatch(d.getCatabatch());
            result.setVenues(d.getLocation());
            result.setLanguages(d.getBooklang());
            result.setOperator(d.getAdmin());
            result.setTidyNumber(d.getCatalognums());
            result.setUpdateNumber(d.getOperatenums());
            result.setBookType(d.getBookcate());
            result.setContentType(d.getBookclass());
            return result;
        }).collect(Collectors.toList()));

        //        return Pagination.empty();
    }

    /**
     * 操作员工作量
     */
    @Data
    static class ThirdTidyUpDTO {
        private LocalDateTime catalogdate;
        private String admin;
        private Integer catalognums;
        private Integer correctnums;
        private LocalDateTime workingtime;
        private String Bk_class;
    }

    /**
     * 编目综合统计
     */
    @Data
    static class TidyUpStatisticsDTO {
        private String location;
        private String catabatch;
        private String bookstate;
        private String booklang;
        private String bookcate;
        private String bookclass;
        private String admin;
        private Integer catalognums;
        private Integer operatenums;
    }
}
