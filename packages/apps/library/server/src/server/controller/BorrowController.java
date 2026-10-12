package com.guhecloud.cube.srv.gen.library.management.server.controller;

import com.guhecloud.cube.srv.gen.library.management.common.dto.KeyValueDTO;
import com.guhecloud.cube.srv.gen.library.management.server.service.BorrowService;
import com.guhecloud.cube.srv.gen.library.management.server.vo.param.BorrowRecordPageParam;
import com.guhecloud.cube.srv.gen.library.management.server.vo.result.BorrowRecordResult;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 借阅控制层
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BorrowController {
    private final BorrowService borrowService;

    /**
     * 借阅记录分页
     *
     * @param param 入参
     *
     * @return 借阅记录列表
     */
    @GetMapping("/library/borrow/borrowRecordPage")
    public Pagination<BorrowRecordResult> borrowRecordPage(BorrowRecordPageParam param) {
        return borrowService.borrowRecordPage(param);
    }

    /**
     * 借阅记录趋势
     *
     * @param param 条件
     *
     * @return 趋势
     */
    @GetMapping("/library/ue/borrow/borrowRecordTrend")
    public List<KeyValueDTO> getBorrowRecordTrend(BorrowRecordPageParam param) {
        return borrowService.getBorrowRecordTrend(param);
    }

    /**
     * 借阅记录热门top(一年内的)
     *
     * @param param 条件
     *
     * @return 热门
     */
    @GetMapping("/library/ue/borrow/borrowRecordHotTop")
    public List<KeyValueDTO> getBorrowRecordHotTop(BorrowRecordPageParam param) {
        return borrowService.getBorrowRecordHotTop(param);
    }
}
