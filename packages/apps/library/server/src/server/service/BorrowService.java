package com.guhecloud.cube.srv.gen.library.management.server.service;

import com.guhecloud.cube.srv.gen.library.management.common.dto.KeyValueDTO;
import com.guhecloud.cube.srv.gen.library.management.server.vo.param.BorrowRecordPageParam;
import com.guhecloud.cube.srv.gen.library.management.server.vo.result.BorrowRecordResult;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 借阅
 */
public interface BorrowService {
    Pagination<BorrowRecordResult> borrowRecordPage(BorrowRecordPageParam param);

    /**
     * 借阅记录趋势
     *
     * @param param 条件
     *
     * @return 趋势
     */
    List<KeyValueDTO> getBorrowRecordTrend(BorrowRecordPageParam param);

    /**
     * 借阅记录热门top(一个自然年之内的)
     *
     * @param param 条件
     *
     * @return 热门
     */
    List<KeyValueDTO> getBorrowRecordHotTop(BorrowRecordPageParam param);
}
