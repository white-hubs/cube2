package com.guhecloud.cube.srv.gen.library.management.server.third.lamp.service;

import com.guhecloud.cube.srv.gen.library.management.server.vo.param.BorrowRecordPageParam;
import com.guhecloud.cube.srv.gen.library.management.server.vo.result.BorrowRecordResult;
import com.guhecloud.platform.base.model.Pagination;

public interface ThirdBorrowService {
    Pagination<BorrowRecordResult> borrowRecordPage(BorrowRecordPageParam param);
}
