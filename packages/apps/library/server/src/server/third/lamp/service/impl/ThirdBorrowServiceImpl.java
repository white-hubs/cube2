package com.guhecloud.cube.srv.gen.library.management.server.third.lamp.service.impl;

import com.guhecloud.cube.srv.gen.library.management.server.po.SystemLoginInfoPO;
import com.guhecloud.cube.srv.gen.library.management.server.service.LibraryLoginInfoService;
import com.guhecloud.cube.srv.gen.library.management.server.third.lamp.service.ThirdBorrowService;
import com.guhecloud.cube.srv.gen.library.management.server.third.lamp.utils.ThirdHttpLibraryUtils;
import com.guhecloud.cube.srv.gen.library.management.server.third.lamp.utils.ThirdLibraryServiceUtils;
import com.guhecloud.cube.srv.gen.library.management.server.vo.param.BorrowRecordPageParam;
import com.guhecloud.cube.srv.gen.library.management.server.vo.result.BorrowRecordResult;
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
public class ThirdBorrowServiceImpl implements ThirdBorrowService {
    private final LibraryLoginInfoService libraryLoginInfoService;
    private static final String BORROW_RECORD_URL = "App.Check_Report.Borrowlist";

    /**
     * 借阅记录分页
     *
     * @param param 入参
     *
     * @return 借阅记录列表
     */
    @Override
    public Pagination<BorrowRecordResult> borrowRecordPage(BorrowRecordPageParam param) {

        Map<String, Object> map = new HashMap<>();
        map.put("perpage", param.getPageSize());
        map.put("page", param.getPageNumber());
        if (StringUtils.isNotBlank(param.getStatus())) {
            map.put("catalogbatch", param.getStatus());
        }
        if (StringUtils.isNotBlank(param.getName())) {
            map.put("bookname", param.getName());
        }
        Period period = param.getPeriod();
        if (period != null && period.getBegin() != null && period.getEnd() != null) {
            map.put("borrowtme", DateUtils.format(period.getBegin()));
            map.put("borrowtme2", DateUtils.format(period.getEnd()));
        }
        if (StringUtils.isNotBlank(param.getStatus())) {
            map.put("status", param.getStatus());
        }
        SystemLoginInfoPO systemLoginInfoPO = libraryLoginInfoService.getLoginInfoByLibrary();
        Object obj = ThirdHttpLibraryUtils.sendThirdPost(BORROW_RECORD_URL, map, systemLoginInfoPO);
        if (obj == null) {
            return Pagination.empty();
        }

        Pagination<BorrowRecordDTO> page = ThirdLibraryServiceUtils.getObjPage(obj, BorrowRecordDTO.class);

        return new Pagination<>(page.getTotal(), page.getPages(), page.getRecords().stream().map(d -> {
            BorrowRecordResult result = new BorrowRecordResult();
            result.setBorrowTime(d.getBorrowdtme());
            result.setStatus(d.getStatus());
            result.setReturnTime(d.getYq_date());
            result.setBookAuthor(d.getBookauth());
            result.setBorrowCardNumber(d.getCardnum());
            result.setPublishingHouse(d.getPressname());
            result.setIsbnNumber(d.getIsbn());
            result.setShouldReturnTime(d.getReturntme());
            result.setBookName(d.getBookname());
            return result;
        }).collect(Collectors.toList()));
    }

    /**
     * 借阅记录
     */
    @Data
    static class BorrowRecordDTO {
        private String cardnum;
        private LocalDateTime borrowdtme;
        private LocalDateTime returntme;
        private String bookname;
        private String bookauth;
        private String pressname;
        private String isbn;
        private String status;
        private LocalDateTime yq_date;
    }
}
