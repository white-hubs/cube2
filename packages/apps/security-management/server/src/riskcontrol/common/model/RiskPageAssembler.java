package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.model;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;
import java.util.function.Function;

public final class RiskPageAssembler {
    private RiskPageAssembler() {
    }

    public static <T, R> Pagination<R> toPagination(IPage<T> page, Function<T, R> converter) {
        List<R> records = page.getRecords().stream().map(converter).toList();
        return new Pagination<>(page.getTotal(), page.getPages(), records);
    }
}
